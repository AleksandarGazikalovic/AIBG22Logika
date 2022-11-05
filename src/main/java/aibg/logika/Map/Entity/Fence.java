package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
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
    public void stepOn(Player player, Map map, int q, int r) {
        player.illegalAction();
    }

    @Override
    public void attacked(Entity attacker, Map map, int q, int r) {
        if(attacker instanceof Player){
            health-= ((Player) attacker).getPower();
            if(health<=0){//uklanja sa mape
                map.getTile(q,r).setEntity(new Empty()); // TODO ne mora new, moze da se koristi jedan Empty objekat za sva polja
            }
        }//ignorise napade boss-a
    }
}
