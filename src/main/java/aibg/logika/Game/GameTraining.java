package aibg.logika.Game;

import aibg.logika.Map.Entity.*;
import aibg.logika.Map.Map;
import aibg.logika.dto.DoActionRequestDTO;
import aibg.logika.dto.DoActionResponseDTO;
import aibg.logika.dto.TrainGameStateRequestDTO;
import aibg.logika.dto.TrainRequestDTO;
import aibg.logika.service.GameService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

@Setter
@Getter
public class GameTraining extends Game{

    @JsonIgnore
    private Logger LOG = LoggerFactory.getLogger(GameService.class);
    @JsonIgnore
    private String gameState;
    @JsonIgnore
    private GameService gameService;
    @JsonIgnore
    private int playerIdx;
    @JsonIgnore
    private int currPlayerIdx;
    @JsonIgnore
    private String errorMessage;

    // konstruktor za slucaj train-a
    public GameTraining(Map map, int playerIdx, GameService gameService, String username) {
        this.map = map;
        this.gameService = gameService;
        this.playerIdx = playerIdx;
        this.currPlayerIdx = 1;
        this.player1 = new TrainingBot(spawnpoint1, 1, this.map, this.gameService); //dodati imena za botove
        this.player2 = new TrainingBot(spawnpoint2, 2, this.map, this.gameService);
        this.player3 = new TrainingBot(spawnpoint3, 3, this.map, this.gameService);
        this.player4 = new TrainingBot(spawnpoint4, 4, this.map, this.gameService);
        this.boss = map.getBoss();
        switch(playerIdx) {
            case 1:
                this.player1 = new Player(spawnpoint1, 1, username, this.map);
                break;
            case 2:
                this.player2 = new Player(spawnpoint2, 2,username, this.map);
                break;
            case 3:
                this.player3 = new Player(spawnpoint3, 3, username, this.map);
                break;
            case 4:
                this.player4 = new Player(spawnpoint4, 4, username, this.map);
                break;
        }
        this.players = new HashMap<>();
        this.players.put(player1.getPlayerIdx(), player1);
        this.players.put(player2.getPlayerIdx(), player2);
        this.players.put(player3.getPlayerIdx(), player3);
        this.players.put(player4.getPlayerIdx(), player4);
        scoreBoard = new ScoreBoard(player1, player2, player3, player4);
        bossCounter=0;
        Health.generate(this.map, this.players);
        Experience.generate(this.map,this.players);
    }

    /*
    public void playBot() { // odigra potez bota i azurira gameState
        TrainingBot currPlayer = (TrainingBot) players.get(currPlayerIdx);
        gameState = currPlayer.runBot(gameState);
    }
    */

    // Odigra jednu celu rundu trening igre, redosled zavisi od playerIdx-a igraca
    // i na kraju vrati gameState
    public String playTheRound(String action, Integer gameId) {


        // dohvati onog koji trenutno treba da odigra
        // ako je bot, odradi mu akciju i predji na sledeceg, isto i za bossa a on resetuje currPlayerIdx
        // ako je igrac, njemu vrati trenutni gameState
        boolean breakCycle = false;
        while (true) {
            if (currPlayerIdx == playerIdx) { // igra igrac ili prekida while ako je ponovo dosao red na igraca
                if (breakCycle){
                    breakCycle = false;
                    return errorMessage;
                }
                LOG.info("Player did his thing: " + LocalDateTime.now());
                DoActionResponseDTO actionResponse=((DoActionResponseDTO)this.gameService.doActionTrain(gameId, playerIdx, action));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gameState = actionResponse.getGameState();
                errorMessage = actionResponse.getMessage();
                currPlayerIdx++;
                breakCycle = true;
            } else { // igraju botovi i boss

                if (currPlayerIdx != 5) { // igra bot
                    ((TrainingBot)players.get(currPlayerIdx)).runBot(this);    //poziva update gejma
                    currPlayerIdx++;
                } else { // igra boss
                    currPlayerIdx = 1;
                }

            }
        }



    }


}
