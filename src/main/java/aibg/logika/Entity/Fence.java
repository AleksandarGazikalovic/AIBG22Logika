package aibg.logika.Entity;

import java.awt.*;

public class Fence extends Entity{

    public Fence(int r, int q){
        super(r,q);
    }




    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(xCenter + 4*hexagonRadius*q + 2*hexagonRadius*r - 5, yCenter + 3*hexagonRadius*r - 5, 10, 10);
    }
}
