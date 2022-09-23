package aibg.logika.Map.Tile;

import aibg.logika.Map.Entity.Entity;
import lombok.Getter;

@Getter
public class FullTile extends Tile {
    public FullTile(int r, int q, Entity entity) {
        super(r, q, entity);
        this.setTileType("FULL");
    }
}
