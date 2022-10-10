package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import lombok.Getter;
import lombok.Setter;

// TODO ne sme da se pridje polje do BlackHole, neki zaseban Entity
@Getter
@Setter
public class Blackhole implements Entity{

    String type = "BLACKHOLE";

    public Blackhole() {
    }

    @Override
    public void stepOn(Player player, Map map, int q, int r) {

    }

    @Override
    public void attacked(Entity attacker, Map map, int q, int r) {

    }
//    @Override
//    public void paint(Graphics g) {
//
//    }
}
