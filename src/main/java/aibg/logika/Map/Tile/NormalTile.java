package aibg.logika.Map.Tile;

import aibg.logika.Map.Entity.Entity;
import lombok.Getter;

@Getter
public class NormalTile extends Tile {
    public NormalTile(int q, int r, Entity entity) {
        super(q, r, entity);
        this.setTileType("NORMAL");
    }

    public NormalTile(int q, int r) {
        super(q,r);
        this.setTileType("NORMAL");
    }
}
