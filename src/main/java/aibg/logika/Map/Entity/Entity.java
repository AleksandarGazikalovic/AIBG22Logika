package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;

public interface Entity {

    void stepOn(Player player, Map map, int q, int r);

    void attacked(Entity attacker, Map map, int q, int r);

}
