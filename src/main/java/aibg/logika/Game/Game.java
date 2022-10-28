package aibg.logika.Game;

import aibg.logika.Action.Direction;
import aibg.logika.Map.Entity.*;
import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
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
    protected Spawnpoint spawnpoint3 = new Spawnpoint(7, 7);
    protected Player player4;
    @JsonIgnore
    protected Spawnpoint spawnpoint4 = new Spawnpoint(-14, 7);

    @JsonIgnore
    protected Boss hugoBoss;
    private Player winner;
    @JsonIgnore
    protected HashMap<Integer, Player> players;

    public Game(Map map) {
        this.map = map;
        this.player1 = new Player(spawnpoint1, 1, this.map);
        this.player2 = new Player(spawnpoint2, 2, this.map);
        this.player3 = new Player(spawnpoint3, 3, this.map);
        this.player4 = new Player(spawnpoint4, 4, this.map);
        this.players = new HashMap<>();
        this.players.put(player1.getPlayerIdx(), player1);
        this.players.put(player2.getPlayerIdx(), player2);
        this.players.put(player3.getPlayerIdx(), player3);
        this.players.put(player4.getPlayerIdx(), player4);

    }

    public void update(String action, int playerIdx) {
        if (this.winner != null) {
            throw new GameException("Game is finished!");
        }

        Player active = players.get(playerIdx);

        if (action == null) {
            throw new GameException("Action is null");
        } else {
            if(active.isTrapped()){ //ukoliko je u blackhole ne moze da radi akciju
                ((Blackhole)(map.getTile(active.getQ(), active.getR()).getEntity())).releasePlayer();
                return;
            }
            String[] actionParams = action.split("-");//ja sam za to da bude separator ",", ida i move bude preko koordinata
            Tile actionTile = calculateCoords(active.getQ(), active.getR(), actionParams[1]); // TODO nije dobro- valjda se za napad prosledjuju koordinate a ne smer?
            int actQ=actionTile.getQ();
            int actR=actionTile.getR();
            //TODO provera da li je u range ta koordinata; ako nije,vraca poslednju koja jeste u tom smeru; to mopzda moze i u okviru obstacle
            //if(hexDistance(active.getQ(),active.getR(),actQ,actR)>RANGE)
            Entity passiveEntity=actionTile.getEntity();
            //ovaj deo proverava da nije neki player na tom polju, ako jeste nad njime ce se obavljati radnja
            for(java.util.Map.Entry<Integer, Player> pair : players.entrySet()){
                if(pair.getValue().getQ()==actQ && pair.getValue().getR()==actR) passiveEntity = pair.getValue();
            }
            else {
                String[] actionParams = action.split("-");
                Tile actionTile = calculateCoords(active.getQ(), active.getR(), actionParams[1]); // TODO nije dobro- valjda se za napad prosledjuju koordinate a ne smer?
                int actQ = actionTile.getQ();
                int actR = actionTile.getR();
                Entity passiveEntity = actionTile.getEntity();
                //ovaj deo proverava da nije neki player na tom polju, ako jeste nad njime ce se obavljati radnja
                for (java.util.Map.Entry<Integer, Player> pair : players.entrySet()) {
                    if (pair.getValue().getQ() == actQ && pair.getValue().getR() == actR)
                        passiveEntity = pair.getValue();
                }
                case "attack": {
                    if(hexDistance(actQ, actR, active.getQ(), active.getR()) <= GameParameters.RANGE){
                        Entity obstacle=getObstacle(active.getQ(),active.getR(),actQ,actR);
                        if(obstacle!=null){
                            passiveEntity=obstacle;
                        }
                        passiveEntity.attacked(active,this.map,actQ,actR);
                    }else{
                        active.illegalAction();
                    }


                    break;
                }
            }
        }
        if(playerIdx == 4){
            hugoBoss.turn(map,players);
        }
    }


    private Tile calculateCoords(int q, int r, String direction) {
        int newQ = q + Direction.valueOf(direction).q;
        int newR = r + Direction.valueOf(direction).r;

        return this.map.getTile(newQ,newR);

    }
    // pored ovoga, treba proslediti tacne koordinate udara frontu nekako
    private Entity getObstacle(int startQ,int startR,int endQ,int endR){
        int hexDistance=hexDistance(startQ, startR, endQ, endR);
        double q=startQ,r=startR;
        double s=-q-r;
        for(int i=1;i<=hexDistance;i++){
            //a+(b-a)*1/n *i
            q=startQ+(endQ-startQ)*1.0*i/hexDistance;
            r=startR+(endR-startR)*1.0*i/hexDistance;
            s=-q-r;
            //rounding
            int cordQ= (int) Math.round(q);
            int cordR= (int) Math.round(r);
            int cordS= (int) Math.round(s);
            double q_diff=Math.abs(q-cordQ);
            double r_diff=Math.abs(r-cordR);
            double s_diff=Math.abs(s-cordS);

            if(q_diff>r_diff && q_diff>s_diff){
                cordQ=-cordR-cordS;
            } else if (r_diff>s_diff) {
                cordR=-cordQ-cordS;
            }else{
                cordS=-cordR-cordQ;
            }
            for(Player player:players.values()){
                if(player.getQ()==cordQ && player.getR()==cordR){
                    return player;
                }
            }
            if(!(map.getTile((int)q,(int)r).getEntity() instanceof Empty)) {
                return map.getTile((int) q, (int) r).getEntity();
            }
        }
        return null;
    }

    private int hexDistance(int startQ,int startR,int endQ,int endR){
        return ( Math.abs(startQ-endQ) + Math.abs(startR-endR) + Math.abs(startQ+startR-endQ-endR) ) / 2;
    }










}




