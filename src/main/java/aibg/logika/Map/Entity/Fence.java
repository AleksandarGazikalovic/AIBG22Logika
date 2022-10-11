package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import lombok.Getter;
import lombok.Setter;

import static aibg.logika.Game.GameParameters.FENCE_HEALTH;

@Getter
@Setter
public class Fence implements Entity{
    String type;
    int health=FENCE_HEALTH;

    public Fence() {
        type="FENCE";
    }

    @Override
    public void stepOn(Player player, Map map, int q, int r) {
        player.illegalAction();
    }

    @Override
    public void attacked(LiveEntity attacker, Map map, int q, int r) {
        if(attacker instanceof Player){
            health-=attacker.getPower();
            if(health<=0){//uklanja sa mape
                map.getTile(q,r).setEntity(new Empty()); // TODO ne mora new, moze da se koristi jedan Empty objekat za sva polja
            }
        }//ignorise napade boss-a
    }
}
