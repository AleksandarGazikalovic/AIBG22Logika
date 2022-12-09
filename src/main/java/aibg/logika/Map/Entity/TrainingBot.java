package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import aibg.logika.Game.GameParameters;
import aibg.logika.Game.GameTraining;
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

    //@JsonIgnore
    //private GameService gameService;

    //@JsonIgnore
    //private String id;


    public TrainingBot(Spawnpoint spawnpoint, int playerIdx, Map map, GameService gameService) {
        super(spawnpoint, playerIdx,"Bot" + playerIdx, map);
        //this.gameService = gameService;
        this.playerIdx = playerIdx;

    }

    public void runBot(GameTraining game) { // Odradi se jednom po potezu, zove se od spolja, azurira gameState kada zavrsi


        // ovde potom bot odigra svoj potez i azurira stanje igre
        String someAction = "";
        // formiranje someAction kao akcije koju bot zeli da uradi - ovde AI bota treba da odradi posao
        // Naredne dve linije su zakomentarisane jer baca error pri pokusaju debagovanja
        //DoActionResponseDTO dto = (DoActionResponseDTO)gameService.doAction(new DoActionRequestDTO(this.playerIdx, gameState, someAction));

        do{
            int newQ = this.q + (int)(Math.random() * 3) - 1;
            int newR = this.r + (int)(Math.random() * 3) - 1;
            someAction="move," + newQ+"," + newR;
        }
        while(game.update(someAction,this.playerIdx)!=null);

        LOG.info(String.valueOf("Training bot " + playerIdx + " did his thing: " + someAction));
        //return dto.getGameState(); ovo treba da vraca, nakon sto se srede bagovi

    }



}
