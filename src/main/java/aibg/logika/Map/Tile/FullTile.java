package aibg.logika.Map.Tile;

import aibg.logika.Map.Entity.Entity;
import lombok.Getter;

@Getter
public class FullTile extends Tile {
    public FullTile(int q, int r, Entity entity) {
        super(q, r, entity);
        this.setTileType("FULL");
    }
}
