package aibg.logika.Game;

import aibg.logika.Action.Direction;
import aibg.logika.Map.Entity.Blackhole;
import aibg.logika.Map.Entity.Boss;
import aibg.logika.Map.Entity.Entity;
import aibg.logika.Map.Entity.Player;
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
    private Map map;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    @JsonIgnore
    private Boss hugoBoss;
    private Player winner;
    @JsonIgnore
    private HashMap<Integer, Player> players;

    public Game(Map map) {
        this.map = map;
        this.player1 = new Player(-7, -7, 1, this.map);
        this.player2 = new Player(14, -7, 2, this.map);
        this.player3 = new Player(7, 7, 3, this.map);
        this.player4 = new Player(-14, 7, 4, this.map);
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
            String[] actionParams = action.split("-");
            Tile actionTile = calculateCoords(active.getQ(), active.getR(), actionParams[1]); // TODO nije dobro- valjda se za napad prosledjuju koordinate a ne smer?
            int actQ=actionTile.getQ();
            int actR=actionTile.getR();
            Entity passiveEntity=actionTile.getEntity();
            //ovaj deo proverava da nije neki player na tom polju, ako jeste nad njime ce se obavljati radnja
            for(java.util.Map.Entry<Integer, Player> pair : players.entrySet()){
                if(pair.getValue().getQ()==actQ && pair.getValue().getR()==actR) passiveEntity = pair.getValue();
            }
            switch (actionParams[0]) {
                case "move": {
                    passiveEntity.stepOn(active,this.map,actQ,actR);
                    break;
                }
                case "attack": {
                    passiveEntity.attacked(active,this.map,actQ,actR);

                    break;
                }
                default:
                    throw new GameException("Action doesn't exist!");
            }
        }
    }


    private Tile calculateCoords(int q, int r, String direction) {
        int newQ = q + Direction.valueOf(direction).q;
        int newR = r + Direction.valueOf(direction).r;

        return this.map.getTile(newQ,newR);

    }
}




