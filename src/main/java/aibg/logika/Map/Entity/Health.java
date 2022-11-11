package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;

public class Health implements Entity {
    String type="HEALTH";
    public static int currHP = 0;

    public static void generate(Map map){
        while (currHP < 2){
            int size = map.getSize();
            int r = ((int) (Math.random() * (size/2 + size/2) - size/2));
            int q = ((int) (Math.random() * (size/2 + size/2) - size/2));
            if(r+q >=-size/2 && r+q <= size/2 && Math.abs(r) <=size/2 && Math.abs(q) <= size/2) {
                Tile tile = map.getTile(q, r);
                if(tile.getEntity() instanceof Empty){
                    currHP++;
                    tile.setEntity(new Health());
                }
            }
        }
    }

    @Override //TODO
    public void stepOn(Player player, Map map, int q, int r) {
        player.setQ(q);
        player.setR(r);
        player.heal();
        currHP--;
        if(currHP == 0){
            generate(map);
        }
    }

    @Override //TODO
    public void attacked(Entity attacker, Map map, int q, int r) {

    }
}
