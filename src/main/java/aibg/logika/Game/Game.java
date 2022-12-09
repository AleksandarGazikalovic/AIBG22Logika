package aibg.logika.Game;

import aibg.logika.Map.Entity.*;
import aibg.logika.Map.Map;
import aibg.logika.dto.PlayerAttackDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Game implements Serializable {
    protected Map map;
    protected Player player1;
    @JsonIgnore
    protected Spawnpoint spawnpoint1 = new Spawnpoint(-7, -7);
    protected Player player2;
    @JsonIgnore
    protected Spawnpoint spawnpoint2 = new Spawnpoint(14, -7);
    protected Player player3;
    @JsonIgnore
    protected Spawnpoint spawnpoint3 = new Spawnpoint(-14, 7);
    protected Player player4;
    @JsonIgnore
    protected Spawnpoint spawnpoint4 = new Spawnpoint(7, 7);


    protected Boss boss;
    /** Used to store a valid player attack for front to draw*/
    @JsonIgnore
    protected PlayerAttackDTO playerAttack;

    @JsonIgnore
    protected HashMap<Integer, Player> players;
    protected ScoreBoard scoreBoard;
    @JsonIgnore
    protected int bossCounter;
    @JsonIgnore
    protected String errorMessage = null;

    public Game(Map map, List<String> playerNames) {
        this.map = map;
        this.player1 = new Player(spawnpoint1, 1, playerNames.get(0), this.map);
        this.player2 = new Player(spawnpoint2, 2, playerNames.get(1), this.map);
        this.player3 = new Player(spawnpoint3, 3, playerNames.get(2), this.map);
        this.player4 = new Player(spawnpoint4, 4, playerNames.get(3), this.map);
        this.players = new HashMap<>();
        this.players.put(player1.getPlayerIdx(), player1);
        this.players.put(player2.getPlayerIdx(), player2);
        this.players.put(player3.getPlayerIdx(), player3);
        this.players.put(player4.getPlayerIdx(), player4);
        this.boss = map.getBoss();
        scoreBoard = new ScoreBoard(player1, player2, player3, player4);
        bossCounter=0;
        Health.generate(this.map, this.players);
        Experience.generate(this.map,this.players);
    }

    public String update(String action, int playerIdx) {
        errorMessage = null;
        boss.setBossAction(false); //uvek je false, osim u potezu kad boss odigra svoj napad
        scoreBoard.update();
        playerAttack = null;
        Player active = players.get(playerIdx);

        if (action == null) {
            bossCounter++;
            active.illegalAction();
            return "Action is null";
        } else {
            while (bossCounter != 4) {
                if (active.isTrapped()) { //ukoliko je u blackhole ne moze da radi akciju
                    ((Blackhole) (map.getTile(active.getQ(), active.getR()).getEntity())).releasePlayer();
                    bossCounter++;
                    errorMessage = "Zarobljeni ste u crnoj rupi";
                    active.illegalAction();
                    break;
                } else {
                    action = action.replaceAll(" ","");
                    String[] actionParams = action.split(",");//ja sam za to da bude separator ",", ida i move bude preko koordinata
                    if (Arrays.stream(actionParams).count() != 3) {
                        bossCounter++;
                        errorMessage = "Nemate tri parametra vaše akcije";
                        active.illegalAction();
                        break;
                    }
                    try {
                        int actQ = Integer.parseInt(actionParams[1]);
                        int actR = Integer.parseInt(actionParams[2]);

                        if (!(actQ + actR >= -map.getSize() / 2 && actQ + actR <= map.getSize() / 2
                                && Math.abs(actQ) <= map.getSize() / 2 && Math.abs(actR) <= map.getSize() / 2)) {
                            bossCounter++;
                            errorMessage = "Pokušavate da predjete granice mape";
                            active.illegalAction();
                            break;
                        }
                        //TODO provera da li je u range ta koordinata; ako nije,vraca poslednju koja jeste u tom smeru; to mopzda moze i u okviru obstacle
                        Entity passiveEntity = this.map.getTile(actQ, actR).getEntity();
                        //ovaj deo proverava da nije neki player na tom polju, ako jeste nad njime ce se obavljati radnja
                        for (java.util.Map.Entry<Integer, Player> pair : players.entrySet()) {
                            if (pair.getValue().getQ() == actQ && pair.getValue().getR() == actR)
                                passiveEntity = pair.getValue();
                        }
                        switch (actionParams[0]) {
                            case "attack": {
                                errorMessage = checkAttack(active, actQ, actR, passiveEntity);
                                break;
                            }
                            case "move": {
                                errorMessage = checkMove(active, actQ, actR, passiveEntity);
                                break;
                            }
                            default: {
                                bossCounter++;
                                errorMessage = "Poslata pogrešna akcija";
                                active.illegalAction();
                                break;
                            }
                        }
                        break;
                    } catch (Exception e) {
                        errorMessage = "Niste uneli koordinate u pravom formatu";
                        active.illegalAction();
                    }
                }
            }
            if(bossCounter==4){
                boss.turn(this,this.players);
                bossCounter=0;
            }

            return errorMessage;
        }
    }

    private String checkMove(Player active, int actQ, int actR, Entity passiveEntity) {
        if (hexDistance(actQ, actR, active.getQ(), active.getR()) == GameParameters.MOVE_RANGE) {
            int oldQ= active.getQ();
            int oldR= active.getR();
            passiveEntity.stepOn(active, this, actQ, actR);
            bossCounter++;
            if(oldQ== active.getQ() && oldR== active.getR()) {
                active.illegalAction();
                return "Pokušali ste da se pomerite na polje koje je tipa FULL ili player";
            }
        } else {
            bossCounter++;
            active.illegalAction();
            return "Pokušavate da predjete na polje koje nije pored vas";
        }
        return null;
    }

    private String checkAttack(Player active, int actQ, int actR, Entity passiveEntity) {
        if (hexDistance(actQ, actR, active.getQ(), active.getR()) <= GameParameters.RANGE) {
            bossCounter++;
            if(!(passiveEntity instanceof Player || passiveEntity instanceof Boss || passiveEntity instanceof Asteroid)) {
                passiveEntity.attacked(active,this, actQ, actR);
                return "Pokušavate da napadnete polje koje nije namenjeno za napad";
            }

             //ako ima obstacle, napada obstacle, inače napada staru metu
            if (!(attackObstacle(active, active.getQ(), active.getR(), actQ, actR))) {
                playerAttack = new PlayerAttackDTO(active.getPlayerIdx(), actQ, actR); //ako nema obstacle ni problema, cuva prosledjene vrednosti
                passiveEntity.attacked(active,this, actQ, actR);
            }

        } else {
            bossCounter++;
            active.illegalAction();
            return "Pokušavate napasti igraca koji vam nije u range-u";
        }
        return null;
    }

    // pored ovoga, treba proslediti tacne koordinate udara frontu nekako
    /** Checks for obstacles between these coordinates; if obstacle is detected, returns that Entity obstacle and sets playerAttack to obstacle coordinates */
    private boolean attackObstacle(Player active, int startQ, int startR, int endQ, int endR) {
        int hexDistance = hexDistance(startQ, startR, endQ, endR);
        double q = startQ, r = startR;
        double s = -q - r;
        for (int i = 1; i < hexDistance; i++) {
            //a+(b-a)*1/n *i
            q = startQ + (endQ - startQ) * 1.0 * i / hexDistance;
            r = startR + (endR - startR) * 1.0 * i / hexDistance;
            s = -q - r;
            //rounding
            int cordQ = (int) Math.round(q);
            int cordR = (int) Math.round(r);
            int cordS = (int) Math.round(s);
            double q_diff = Math.abs(q - cordQ);
            double r_diff = Math.abs(r - cordR);
            double s_diff = Math.abs(s - cordS);

            if (q_diff > r_diff && q_diff > s_diff) {
                cordQ = -cordR - cordS;
            } else if (r_diff > s_diff) {
                cordR = -cordQ - cordS;
            } else {
                cordS = -cordR - cordQ;
            }
            for (Player player : players.values()) {
                if (player.getQ() == cordQ && player.getR() == cordR) {
                    playerAttack = new PlayerAttackDTO(active.getPlayerIdx(), cordQ, cordR);
                    player.attacked(active,this, cordQ, cordR);
                    return true;
                }
            }
            if (!(map.getTile(cordQ, cordR).getEntity() instanceof Empty)) { // if (!(map.getTile((int) q, (int) r).getEntity() instanceof Empty))
                playerAttack = new PlayerAttackDTO(active.getPlayerIdx(), cordQ, cordR);
                map.getTile(cordQ, cordR).getEntity().attacked(active, this, cordQ, cordR);
                return true;
            }
        }
        return false;
    }

    private int hexDistance(int startQ, int startR, int endQ, int endR) {
        return (Math.abs(startQ - endQ) + Math.abs(startR - endR) + Math.abs(startQ + startR - endQ - endR)) / 2;
    }


    public void removePlayer(int playerIdx) {
        for (Player player : players.values()) {
            if (player.getPlayerIdx() == playerIdx ) {
                if(player1 == player) {
                    player1 = null;
                }else if(player2 == player){
                    player2 = null;
                }else if(player3 == player){
                    player3 = null;
                } else if(player4 == player){
                    player4 = null;
                }
                player=null;
            }
        }
        players.remove(playerIdx);
    }
}