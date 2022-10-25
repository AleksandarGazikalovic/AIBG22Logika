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
                switch (actionParams[0]) {
                    case "move": {
                        if (passiveEntity != null) // TODO: dodao sam ovo jer mi baca nullPointerExcception pri testiranju
                            passiveEntity.stepOn(active, this.map, actQ, actR);
                        break;
                    }
                    case "attack": {
                        passiveEntity.attacked(active, this.map, actQ, actR);

                        break;
                    }
                    default:
                        throw new GameException("Action doesn't exist!");
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
}




