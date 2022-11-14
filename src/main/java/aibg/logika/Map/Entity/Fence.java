package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static aibg.logika.Game.GameParameters.FENCE_HEALTH;

@Getter
@Setter
@NoArgsConstructor
public class Fence implements Entity{
    String type="FENCE";
    int health=FENCE_HEALTH;

    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        player.illegalAction();
    }

    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        if(attacker instanceof Player){
            health-= ((Player) attacker).getPower();
            if(health<=0){//uklanja sa mape
                game.getMap().getTile(q,r).setEntity(game.getMap().getEmptyObj());
                game.getMap().getTile(q,r).setTileType("NORMAL");
            }
        }//ignorise napade boss-a
    }
}
