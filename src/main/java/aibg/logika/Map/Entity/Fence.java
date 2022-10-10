package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fence implements Entity{
    String type;

    public Fence() {
        type="FENCE";
    }

    @Override
    public void stepOn(Player player, Map map, int q, int r) {

    }

    @Override
    public void attacked(Entity attacker, Map map, int q, int r) {

    }
}
