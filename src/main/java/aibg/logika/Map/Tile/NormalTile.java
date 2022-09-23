package aibg.logika.Map.Tile;

import aibg.logika.Map.Entity.Entity;
import lombok.Getter;

@Getter
public class NormalTile extends Tile {
    public NormalTile(int r, int q, Entity entity) {
        super(r, q, entity);
        this.setTileType("NORMAL");
    }

    public NormalTile(int r, int q) {
        super(r,q);
        this.setTileType("NORMAL");
    }
}
