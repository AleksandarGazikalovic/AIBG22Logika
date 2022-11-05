package aibg.logika.service.implementation;

import aibg.logika.Game.Game;
import aibg.logika.Game.GameTraining;
import aibg.logika.Map.Map;
import aibg.logika.dto.*;
import aibg.logika.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

@Service
@Getter
@Setter
public class GameServiceImplementation implements GameService {

    private Logger LOG = LoggerFactory.getLogger(GameService.class);
    private String MAPS_FOLDER = "./maps";

    private HashMap<Integer,Game> games = new HashMap<>();
    private HashMap<Integer, GameTraining> trainingGames = new HashMap<>();


    /* treba videti kako prihvatati različite mape */
    @Override
    public DTO startGameState(GameStateRequestDTO dto) {

        try {
            //Proverava da li u maps folderu postoji mapa sa odredjenim nazivom
            if (new File(MAPS_FOLDER, dto.getMapName()).exists()) {

                Path mapPath = (new File(MAPS_FOLDER, dto.getMapName())).toPath();
                Game game = new Game(new Map(29, mapPath));
                games.put(dto.getGameId(),game);
                ObjectMapper mapper = new ObjectMapper();
                String gameState = mapper.writeValueAsString(game);
                return new GameStateResponseDTO(gameState);


            } else {
                return new ErrorResponseDTO("U maps folderu ne postoji mapa sa datim imenom.");
            }
        } catch (Exception e) {
            LOG.info("Greška pri učitavanju mape.");
            return new ErrorResponseDTO("Greška pri učitavanju mape.");
        }
    }

    @Override
    public DTO startTrainGameState(TrainGameStateRequestDTO dto) {
        try {
            //Proverava da li u maps folderu postoji mapa sa odredjenim nazivom
            if (new File(MAPS_FOLDER, dto.getMapName()).exists()) {

                Path mapPath = (new File(MAPS_FOLDER, dto.getMapName())).toPath();
                Map map = new Map(29, mapPath);

                GameTraining game = new GameTraining(map, dto.getPlayerIdx(), this);
                //game.assignGameToBots();

                trainingGames.put(dto.getGameId(), game);

                ObjectMapper mapper = new ObjectMapper();
                String gameState = mapper.writeValueAsString(game);
                game.setGameState(gameState);
                return new GameStateResponseDTO(gameState);


            } else {
                return new ErrorResponseDTO("U maps folderu ne postoji mapa sa datim imenom.");
            }
        } catch (Exception e) {
            LOG.info("Greška pri učitavanju mape.");
            return new ErrorResponseDTO("Greška pri učitavanju mape.");
        }
    }

    @Override
    public DTO playerView(PlayerViewRequestDTO dto) {

        return new PlayerViewResponseDTO(dto.getGameState());

           /* if((dto.getGameState().equals("Najjaci gejmStejt") && dto.getPlayerIdx()==1)) {
                return new PlayerViewResponseDTO("Slep si, ne vidiš mapu");
            }else {
                return new ErrorResponseDTO("Baci pogled");
            }*/
    }

    @Override
    public DTO doAction(DoActionRequestDTO dto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Game game = games.get(dto.getGameId());
            String errorMessage = game.update(dto.getAction(), dto.getPlayerIdx());
            //mozda izmeniti da vraca svakog igraca odvojeno a ne hashmapu nzm
            String players=mapper.writeValueAsString(game.getPlayers());
            String gameState = mapper.writeValueAsString(game);
            return new DoActionResponseDTO(errorMessage, gameState, players);
        } catch (JsonProcessingException e) {
            return new ErrorResponseDTO("Greška pri updatovanja gameState-a");
        }

    }

    @Override
    public DTO removePlayer(RemovePlayerRequestDTO dto) {
        return null;
    }

    @Override
    public DTO train(TrainRequestDTO dto) {
        ObjectMapper mapper = new ObjectMapper();
        // dohvati game, player odradi svoje, potom botovi i krajnje stanje vraca
        // prvo dohvati game
        GameTraining game = trainingGames.get(dto.getGameId());

        // odigra jednu celu rundu
        String errorMessage = game.playTheRound(dto.getAction(), dto.getGameId());

        if (game.getGameState() == null){
            return new ErrorResponseDTO("Greška pri igranju trening igre, nesto je poslo po zlu pri azuriranju gameState-a.");
        }
        try {
            String players = mapper.writeValueAsString(game.getPlayers());
            String gameState = game.getGameState();
            return new TrainResponseDTO(errorMessage, gameState, players);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public DTO doActionTrain(Integer gameId, Integer playerIdx, String action) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Game game = trainingGames.get(gameId);
            String errorMessage = game.update(action, playerIdx);
            String players = mapper.writeValueAsString(game.getPlayers());
            String gameState = mapper.writeValueAsString(game);
            return new DoActionResponseDTO(errorMessage, gameState, players);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}

