package aibg.logika.Map;

import aibg.logika.Entity.Entity;

import java.awt.*;

public class Tile {
    private int xCenter = 600; // x koordinata centra
    private int yCenter = 350; // y koordinata centra
    private int hexagonRadius = 10; // "poluprecnik" - dimenzija za crtanje heksagona
    private int r; // prva koordinata (horizontala)
    private int q; // druga koordinata (glavna dijagonala)
    private Entity entity;

    public Tile(int r, int q, Entity entity){
        this.r = r;
        this.q = q;
        this.entity = entity;
    }


    public void setEntity(Entity entity){
        this.entity = entity;
    }

    public boolean isFree(){
        return entity == null;
    }

    public Entity entity(){ return entity;} // TODO da li dohvatati direktno entity sa "tile.entity" ???



























    public void paint(Graphics g){
        int[] X = new int[6];
        int[] Y = new int[6];
        X[0] = xCenter - 2*hexagonRadius + 4*hexagonRadius*q + 2*hexagonRadius*r ;
        X[1] = xCenter - 2*hexagonRadius + 4*hexagonRadius*q + 2*hexagonRadius*r;;
        X[2] = xCenter + 4*hexagonRadius*q + 2*hexagonRadius*r;;
        X[3] = xCenter + 2*hexagonRadius + 4*hexagonRadius*q + 2*hexagonRadius*r;;
        X[4] = xCenter + 2*hexagonRadius + 4*hexagonRadius*q + 2*hexagonRadius*r;;
        X[5] = xCenter + 4*hexagonRadius*q + 2*hexagonRadius*r;;
        Y[0] = yCenter - hexagonRadius + 3*hexagonRadius*r;
        Y[1] = yCenter + hexagonRadius + 3*hexagonRadius*r;
        Y[2] = yCenter + 2*hexagonRadius + 3*hexagonRadius*r;
        Y[3] = yCenter + hexagonRadius + 3*hexagonRadius*r;
        Y[4] = yCenter - hexagonRadius + 3*hexagonRadius*r;
        Y[5] = yCenter - 2*hexagonRadius + 3*hexagonRadius*r;

        g.setColor(Color.white);
        g.drawPolygon(X, Y, 6);
        if(entity!=null){
            entity.paint(g);
        }
    }
}
