package aibg.logika.Entity;

import java.awt.*;
// Entity je sve sta moze da bude u jedan Tile, npr crna rupa, igrac, ograda...
public abstract class Entity {
    protected int xCenter = 600; // koordinata centra
    protected int yCenter = 350; // koordinata centra
    protected int hexagonRadius = 10; // "poluprecnik" - dimenzija za crtanje heksagona
    protected int r; // horizontala
    protected int q; // glavna dijagonala

    public Entity(int r, int q){
        this.r = r;
        this.q = q;
    }


//TODO da li da samo player ima koordinate, a ovo Entity da bude Interface
// TODO  TREBA KOORDINATE ZBOG ISCRTAVANJA !!!











    public abstract void paint(Graphics g);
}
