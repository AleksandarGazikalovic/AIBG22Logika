package aibg.logika.service.implementation;

import aibg.logika.Game.Game;
import aibg.logika.Map.Map;
import aibg.logika.dto.*;
import aibg.logika.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
@Getter
@Setter
public class GameServiceImplementation implements GameService {

    private Logger LOG = LoggerFactory.getLogger(GameService.class);
    private String MAPS_FOLDER = "./maps";


    /* treba videti kako prihvatati različite mape */
    @Override
    public DTO startGameState(GameStateRequestDTO dto) {

        try {
            //Proverava da li u maps folderu postoji mapa sa odredjenim nazivom
            if (new File(MAPS_FOLDER, dto.getMapName()).exists()) {
                Path mapPath = (new File(MAPS_FOLDER, dto.getMapName())).toPath();
                Game game = new Game(new Map(29, mapPath));
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
        return null;
    }

    @Override
    public DTO removePlayer(RemovePlayerRequestDTO dto) {
        return null;
    }


}

