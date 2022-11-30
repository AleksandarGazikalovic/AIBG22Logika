package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import aibg.logika.Game.GameParameters;
import aibg.logika.Map.Map;
import aibg.logika.dto.DoActionRequestDTO;
import aibg.logika.dto.DoActionResponseDTO;
import aibg.logika.service.GameService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

import static java.lang.Integer.max;
import static java.lang.Math.abs;

@Getter
@Setter
public class TrainingBot extends Player{
    @JsonIgnore
    private Logger LOG = LoggerFactory.getLogger(GameService.class);

    String type = "BOT";

    @JsonIgnore
    private GameService gameService;

    @JsonIgnore
    private String id;


    public TrainingBot(Spawnpoint spawnpoint, int playerIdx, Map map, GameService gameService) {
        super(spawnpoint, playerIdx,"Bot",map);
        this.gameService = gameService;
        this.id = String.valueOf(playerIdx);

    }



    public String runBot(String gameState) { // Odradi se jednom po potezu, zove se od spolja, azurira gameState kada zavrsi

        LOG.info(String.valueOf("Training bot " + id + " did his thing: " + LocalDateTime.now()));

        // ovde potom bot odigra svoj potez i azurira stanje igre

        String someAction = "";
        // formiranje someAction kao akcije koju bot zeli da uradi - ovde AI bota treba da odradi posao
        // Naredne dve linije su zakomentarisane jer baca error pri pokusaju debagovanja
        //DoActionResponseDTO dto = (DoActionResponseDTO)gameService.doAction(new DoActionRequestDTO(this.playerIdx, gameState, someAction));


        //return dto.getGameState(); ovo treba da vraca, nakon sto se srede bagovi
        return gameState;
    }



}
