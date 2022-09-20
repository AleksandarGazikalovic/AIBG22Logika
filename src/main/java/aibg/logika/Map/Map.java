package aibg.logika.Map;

import aibg.logika.Entity.*;

import java.awt.*;

public class Map {
    // https://www.redblobgames.com/grids/hexagons/
// prva koordinata je r (horizontala), druga je q (glavna dijagonala),
    private int radius = 9; // broj polja od centra do leve ivice (ne racunajuci centralno polje)
    private Tile[][] tiles = new Tile[2*radius+1][2*radius+1];

    public Map(){
        for(int r=-radius; r<=radius; r++){
            for (int q=-radius; q<=radius; q++){
                if(r+q >=-radius && r+q <= radius && Math.abs(r) <=radius && Math.abs(q) <= radius) {
                    tiles[r + radius][q + radius] = new Tile(r, q, null);// +radius je jer matrica pocinje od [0][0]
                }
            }
        }
        tiles[radius][radius-4].setEntity(new Fence(0,-4));
        tiles[radius][radius+4].setEntity(new Fence(0,4));
        tiles[radius-4][radius+2].setEntity(new Fence(-4,2));
        tiles[radius+4][radius-2].setEntity(new Fence(4,-2));

        Wormhole wormhole1 = new Wormhole(0, -6);
        Wormhole wormhole2 = new Wormhole(0, 6);
        wormhole1.connect(wormhole2);
        tiles[radius][radius-6].setEntity(wormhole1);
        tiles[radius][radius+6].setEntity(wormhole2);
    }



    public void setEntity(int r, int q, Entity entity){
        tiles[r+radius][q+radius].setEntity(entity);
    }

    // Proverava da li je polje zauzeto i da li je outOfBound
    public boolean isFree(int r, int q){
        if(!(q+r>=-radius && q+r<=radius && Math.abs(r) <=radius && Math.abs(q) <= radius)) return false;
        return tiles[r+radius][q+radius].isFree();
    }

    public boolean isWormhole(int r, int q){
        if(!(q+r>=-radius && q+r<=radius && Math.abs(r) <=radius && Math.abs(q) <= radius)) return false; // TODO zbog if u player.move
        return (tiles[r+radius][q+radius].entity() instanceof Wormhole);
    }


















    public void paint(Graphics g){
        for(int r=-radius; r<=radius; r++){
            for(int q=-radius; q<=radius; q++){
                if(r+q >=-radius && r+q <= radius) {
                    tiles[r + radius][q + radius].paint(g);
                }
            }
        }
    }
}
