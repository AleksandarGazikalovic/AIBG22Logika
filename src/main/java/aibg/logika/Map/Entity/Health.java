package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class Health implements Entity {
    String type="HEALTH";
    private static HashMap<Integer, Player> players;

    public static void generate(Map map){
        while (true){
            int size = map.getSize();
            int r = ((int) (Math.random() * (size/2 + size/2) - size/2));
            int q = ((int) (Math.random() * (size/2 + size/2) - size/2));
            if(r+q >=-size/2 && r+q <= size/2 && Math.abs(r) <=size/2 && Math.abs(q) <= size/2) {
                Tile tile = map.getTile(q, r);
                if(tile.getEntity() instanceof Empty){
                    //proverava da li su igraci na tom polju
                    for (java.util.Map.Entry<Integer, Player> pair : players.entrySet()) {
                        if (!(pair.getValue().getQ() == q && pair.getValue().getR() == r)){
                            tile.setEntity(new Health());
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
        player.heal();
        generate(map);
        map.getTile(q,r).setEntity(new Empty());
    }

    @Override //TODO
    public void attacked(Entity attacker, Map map, int q, int r) {

    }


    public static void setPlayers(HashMap<Integer, Player> players){
        Health.players = players;
    }
}
