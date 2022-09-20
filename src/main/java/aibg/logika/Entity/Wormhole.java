package aibg.logika.Entity;

import java.awt.*;

// trebalo bi da radi samo ako je postavljeno simetricno u odnosu na horizontalu, glavnu ili sporednu dijagonalu
// ostalo nisam testirao

public class Wormhole extends Entity{

    Wormhole connectedWormhole = null; // Sa kojom crvotocinom je povezana

    public Wormhole(int r, int q) {
        super(r, q);
    }

    //TODO ovo mozda nije potrebno
    //povezuje ovu crvotocinu sa drugom, potrebno je SAMO JEDNU povezati
    public void connect(Wormhole second){
        if(connectedWormhole == null){
            connectedWormhole = second;
            second.connect(this);
        }
    }





















    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.drawOval(xCenter + 4*hexagonRadius*q + 2*hexagonRadius*r - 5, yCenter + 3*hexagonRadius*r - 5, 10, 10);
    }
}
