package aibg.logika.Map.Entity;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

// trebalo bi da radi samo ako je postavljeno simetricno u odnosu na horizontalu, glavnu ili sporednu dijagonalu
// ostalo nisam testirao
@Getter
@Setter
public class Wormhole implements Entity {

    String type = "WORMHOLE";

    Wormhole connectedWormhole = null; // Sa kojom crvotocinom je povezana

   /*public Wormhole(int r, int q) {
        super(r, q);
    }*/

    public Wormhole() {
    }

    //TODO ovo mozda nije potrebno
    //povezuje ovu crvotocinu sa drugom, potrebno je SAMO JEDNU povezati
    public void connect(Wormhole second){
        if(connectedWormhole == null){
            connectedWormhole = second;
            second.connect(this);
        }
    }

}
