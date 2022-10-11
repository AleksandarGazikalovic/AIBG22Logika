package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import lombok.Getter;
import lombok.Setter;

import static aibg.logika.Game.GameParameters.BOSS_POWER;

@Getter
@Setter
public class Boss extends LiveEntity{



    @Override
    public void stepOn(Player player, Map map, int q, int r) {
        player.illegalAction();
    }


    @Override
    public void attacked(LiveEntity attacker, Map map, int q, int r) {
        // TODO povecaj stats playera
        ((Player)attacker).increaseScore(attacker.getPower());
        ((Player)attacker).increaseExperience(attacker.getPower());
    }

    @Override
    public int getPower() {
        return BOSS_POWER;
    }
}
