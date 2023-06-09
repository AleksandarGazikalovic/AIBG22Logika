package aibg.logika.service.implementation;

import aibg.logika.Game.Game;
import aibg.logika.Map.Entity.Entity;
import aibg.logika.Game.GameTraining;
import aibg.logika.Map.Entity.Player;
import aibg.logika.Map.Map;
import aibg.logika.dto.*;
import aibg.logika.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.HashMap;

@Service
@Getter
@Setter
public class GameServiceImplementation implements GameService {

    private Logger LOG = LoggerFactory.getLogger(GameService.class);
    private String MAPS_FOLDER = "maps/";

    private TreeMap<Integer,Game> games = new TreeMap<>();
    private TreeMap<Integer, GameTraining> trainingGames = new TreeMap<>();

    /* treba videti kako prihvatati različite mape */
    @Override
    public DTO startGameState(GameStateRequestDTO dto) {

        try {
            //Proverava da li u maps folderu postoji mapa sa odredjenim nazivom
            if (this.getClass().getClassLoader().getResource(MAPS_FOLDER+dto.getMapName())!=null) {
                URL mapsURL = this.getClass().getClassLoader().getResource(MAPS_FOLDER+dto.getMapName());
                Game game = new Game(new Map(29, mapsURL),dto.getPlayerUsernames());
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
            if (this.getClass().getClassLoader().getResource(MAPS_FOLDER+dto.getMapName())!=null) {
                URL mapsURL = this.getClass().getClassLoader().getResource(MAPS_FOLDER+dto.getMapName());
                Map map = new Map(29, mapsURL);

                GameTraining game = new GameTraining(map, dto.getPlayerIdx(), this, dto.getUsername());
                //game.assignGameToBots();

                trainingGames.put(dto.getGameId(), game);

                ObjectMapper mapper = new ObjectMapper();
                String gameState = mapper.writeValueAsString(game);
                game.setGameState(gameState); // sto se radi ovo? zasto se cuva gameState kao string kad moze uvek samo da se serijalizuje gejm
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
            String gameState = mapper.writeValueAsString(game);
            String playerAttack = mapper.writeValueAsString(game.getPlayerAttack());
            return new DoActionResponseDTO(errorMessage, gameState, playerAttack);
        } catch (JsonProcessingException e) {
            return new ErrorResponseDTO("Greška pri updatovanja gameState-a");
        }

    }

    @Override
    public DTO removePlayer(RemovePlayerRequestDTO dto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Game game = games.get(dto.getGameId());
            game.removePlayer(dto.getPlayerIdx());
            String gameState = mapper.writeValueAsString(game);
            return new RemovePlayerResponseDTO(gameState);
        } catch (JsonProcessingException e) {
            return new ErrorResponseDTO("Greška pri izbacivanju igrača " + dto.getPlayerIdx() + " iz igre.");
        }
    }

    //ovo poziva server
    @Override
    public DTO train(TrainRequestDTO dto) {
        // dohvati game, player odradi svoje, potom botovi i krajnje stanje vraca
        // prvo dohvati game
        ObjectMapper mapper = new ObjectMapper();
        GameTraining game = trainingGames.get(dto.getGameId());

        // odigra jednu celu rundu
        String errorMessage = game.playTheRound(dto.getAction(), dto.getGameId());

        if (game.getGameState() == null){
            return new ErrorResponseDTO("Greška pri igranju trening igre, nesto je poslo po zlu pri azuriranju gameState-a.");
        }
        try {
            String gameState = mapper.writeValueAsString(game);
            return new TrainResponseDTO(errorMessage, gameState);
        }catch (JsonProcessingException e) {
            return new ErrorResponseDTO("train: Greska pri serijalizaciji game-a");
        }

    }

    //ovo se poziva iz  rounda
    @Override
    public DTO doActionTrain(Integer gameId, Integer playerIdx, String action) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Game game = trainingGames.get(gameId);
            String errorMessage = game.update(action, playerIdx);//ne moze?
            String gameState = mapper.writeValueAsString(game);
            String playerAttack = mapper.writeValueAsString(game.getPlayerAttack());
            return new DoActionResponseDTO(errorMessage, gameState, playerAttack);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    //TODO izmeniti tako da mogu da se gledaju i train igre
    @Override
    public DTO watchGame(WatchGameRequestDTO dto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Game game = games.get(dto.getGameId());
            String gameState = mapper.writeValueAsString(game);
            return new WatchGameResponseDTO(gameState);
        } catch (JsonProcessingException e) {
            return new ErrorResponseDTO("Greška pri pozivanju sa frontom");
        }
    }


    //treba se pozove odakle god odlucimo da zavrsimo igru
    /** Removes game from list of games, makes that ID available again, removes players from game and sets their gameID field to -1
     */
    private void endGame(int gameID){
        Game game = games.get(gameID);
        for(Player player : game.getPlayers().values()){
            player.setPlayerIdx(-1);
        }
        game.setPlayers(null);
        games.remove(gameID);
    }

    private void endGameTraining(int gameID){
        Game game = trainingGames.get(gameID);
        for(Player player : game.getPlayers().values()){
            player.setPlayerIdx(-1);
        }
        game.setPlayers(null);
        trainingGames.remove(gameID);
    }

    public DTO removeGame(RemoveGameRequestDTO dto){
        try {
            if (dto.getGameType() == "Training") {
                endGameTraining(dto.getGameID());
            } else {
                endGame(dto.getGameID());
            }
            return new RemoveGameResponseDTO(true);
        }catch(Exception e){
            return new RemoveGameResponseDTO(false);
        }

    }




}

