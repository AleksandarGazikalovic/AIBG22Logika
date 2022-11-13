package aibg.logika.Map.Entity;

import aibg.logika.Game.GameParameters;
import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class Experience implements Entity{
    String type="EXPERIENCE";
    private static HashMap<Integer, Player> players;

    public static void generate(Map map){
        while (true){
            int size = map.getSize();
            int firstFencePosition = 11; //nema expirience van prvog fence-a
            int r = ((int) (Math.random() * (firstFencePosition + firstFencePosition) - firstFencePosition));
            int q = ((int) (Math.random() * (firstFencePosition + firstFencePosition) - firstFencePosition));
            if(r+q >=-firstFencePosition && r+q <= firstFencePosition && Math.abs(r) <=firstFencePosition && Math.abs(q) <= firstFencePosition) {
                Tile tile = map.getTile(q, r);
                if(tile.getEntity() instanceof Empty){
                    //proverava da li su igraci na tom polju
                    for (java.util.Map.Entry<Integer, Player> pair : players.entrySet()) {
                        if (!(pair.getValue().getQ() == q && pair.getValue().getR() == r)){
                            tile.setEntity(new Experience());
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override //TODO
    public void stepOn(Player player, Map map, int q, int r) {
        player.setQ(q);
        player.setR(r);
        player.increaseExperience(GameParameters.EXP_GATHER);
        generate(map);
        map.getTile(q,r).setEntity(new Empty());
    }

    @Override //TODO
    public void attacked(Entity attacker, Map map, int q, int r) {

    }


    public static void setPlayers(HashMap<Integer, Player> players){
        Experience.players = players;
    }
}
