package aibg.logika.Map.Tile;

import aibg.logika.Map.Entity.Entity;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
@Getter
@Setter
public abstract class Tile {
    private int r; // prva koordinata (horizontala)
    private int q; // druga koordinata (glavna dijagonala)
    private Entity entity;

    private String tileType;

    public Tile(int r, int q, Entity entity){
        this.r = r;
        this.q = q;
        this.entity = entity;
    }

    public Tile(int r, int q) {
        this.r = r;
        this.q = q;
    }

    public void setEntity(Entity entity){
        this.entity = entity;
    }

    public boolean isFree(){
        return entity == null;
    }

    public Entity entity(){ return entity;} // TODO da li dohvatati direktno entity sa "tile.entity" ???

}
