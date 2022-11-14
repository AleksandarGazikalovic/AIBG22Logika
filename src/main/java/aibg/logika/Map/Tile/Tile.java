package aibg.logika.Map.Tile;

import aibg.logika.Map.Entity.Entity;
import aibg.logika.Map.Entity.Player;
import aibg.logika.Map.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
@Getter
@Setter
@NoArgsConstructor
public class Tile{
    private int r; // prva koordinata (horizontala)
    private int q; // druga koordinata (glavna dijagonala)
    private Entity entity;

    private String tileType;

    public Tile(int q, int r, String tileType, Entity entity){
        this.r = r;
        this.q = q;
        this.entity = entity;
        this.tileType = tileType;
    }

    public Tile(int q, int r) {
        this.r = r;
        this.q = q;
    }

}
