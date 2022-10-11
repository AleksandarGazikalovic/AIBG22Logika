package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Empty implements Entity{



    /**
     * Processes attempt of the player to step on the empty tile
     *
     * @param player that is stepping on
     * @param q
     * @param r      coordinates of the tile that is being stepped on
     */
    @Override
    public void stepOn(Player player, Map map, int q, int r) {
        player.setQ(q);
        player.setR(r);
    }

    /**
     * Processes attack on this entity by attacker
     *
     * @param r        coordinates of the tile that is being attacked
     */
    @Override
    public void attacked(LiveEntity attacker, Map map, int q, int r) {
        if(attacker instanceof Player){
            ((Player)attacker).illegalAction();
        }
        // ~~animacija~~
    }
}
