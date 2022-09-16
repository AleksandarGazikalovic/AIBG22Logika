package aibg.logika.service.implementation;

import aibg.logika.domain.Game;
import aibg.logika.dto.*;
import aibg.logika.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Getter
@Setter
public class GameServiceImplementation implements GameService {

    private Logger LOG = LoggerFactory.getLogger(GameService.class);


    /* treba videti kako prihvatati različite mape */
    @Override
    public DTO startGameState() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            //Map map = new Map(new File());
            //String gameState = mapper.writeValueAsString(map);
            String gameState=null;
            return new GameStateResponseDTO(gameState);
        }
        catch (Exception e) {
            LOG.info("Greška pri učitavanju mape.");
            return new ErrorResponseDTO("Greška pri učitavanju mape.");
        }

       // Game game = new Game("Najjaci gejmStejt");
        // return new GameStateResponseDTO(game.getGameState());
    }

    @Override
    public DTO playerView(PlayerViewRequestDTO dto) {

        return null;

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

