package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import aibg.logika.Map.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// trebalo bi da radi samo ako je postavljeno simetricno u odnosu na horizontalu, glavnu ili sporednu dijagonalu
// ostalo nisam testirao
@Getter
@Setter
@NoArgsConstructor
public class Wormhole implements Entity {

    String type = "WORMHOLE";
    int id;

    Wormhole connectedWormhole = null; // Sa kojom crvotocinom je povezana
    int q,r;

   public Wormhole(int q, int r,int id) {
       this.q=q;
       this.r=r;
       this.id=id;
   }

    //povezuje ovu crvotocinu sa drugom, potrebno je SAMO JEDNU povezati
    public void connect(Wormhole second){
        if(connectedWormhole == null){
            connectedWormhole = second;
            second.connect(this);
        }
    }

    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        int newQ = connectedWormhole.getQ() + (q - player.getQ());
        int newR = connectedWormhole.getR() + (r - player.getR());
        map.getTile(newQ,newR).getEntity().stepOn(player,map,newQ,newR);
    }

    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        if(attacker instanceof Player) ((Player)attacker).illegalAction();
    }
}
