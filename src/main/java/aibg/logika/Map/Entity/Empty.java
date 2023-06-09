package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Empty implements Entity{
    String type = "EMPTY";

    /**
     * Processes attempt of the player to step on the empty tile
     *
     * @param player that is stepping on
     * @param q
     * @param r      coordinates of the tile that is being stepped on
     */
    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        player.setQ(q);
        player.setR(r);
    }

    /**
     * Processes attack on this entity by attacker
     *
     * @param game
     * @param r    coordinates of the tile that is being attacked
     */
    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        if(attacker instanceof Player) ((Player)attacker).illegalAction();
    }
}
