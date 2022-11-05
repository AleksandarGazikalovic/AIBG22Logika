package aibg.logika.Map;

import aibg.logika.Game.GameException;
import aibg.logika.Map.Entity.*;
import aibg.logika.Map.Tile.FullTile;
import aibg.logika.Map.Tile.NormalTile;
import aibg.logika.Map.Tile.Tile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.*;

@Getter
@NoArgsConstructor
public class Map implements Serializable {
    // https://www.redblobgames.com/grids/hexagons/
// prva koordinata je r (horizontala), druga je q (glavna dijagonala),

    int size;
    private ArrayList<ArrayList<Tile>> tiles;
    @JsonIgnore
    private ArrayList<FullTile> wormholes;
    @JsonIgnore
    private HashMap<Integer, Tile> tilemap;
    @JsonIgnore
    private ArrayList<FullTile> fence;


    public Map(int n, Path path) throws GameException {
        this.size = n;
        tilemap = new HashMap<>();
        tiles = new ArrayList<>();
        wormholes = new ArrayList<>();
        fence = new ArrayList<>();
        loadMap(path);
    }

    private void loadMap(Path path) throws GameException{
        String mapJsonString;
        int counter = -1;
        int counter2;
        try{
            mapJsonString = Files.readAllLines(path).get(0);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode mapNode = mapper.readTree(mapJsonString);
            counter = -1;
            for(int i = -size/2; i <= size/2; i++) {
                counter++;
                counter2 = -1;
                tiles.add(new ArrayList<>());
                int max = max(-size/2, -i-(size/2));
                int min = min(size/2, -i+(size/2));
                for(int j = max; j <= min; j++) {
                    counter2++;
                    int q = mapNode.get(counter).get(counter2).get("q").asInt();
                    int r = mapNode.get(counter).get(counter2).get("r").asInt();
                    Tile temp = null;
                    String currEntity;

                    switch (mapNode.get(counter).get(counter2).get("type").asText()) {
                        case "NORMAL":
                            currEntity = mapNode.get(counter).get(counter2).get("entity").asText();
                            switch (currEntity){
                                case "NONE":
                                    temp = new NormalTile(q,r, new Empty());
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    break;
                                case "BLACKHOLE":
                                    temp = new NormalTile(q,r, new Blackhole());
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    break;
                                case "WORMHOLE":
                                    temp = new FullTile(q,r, new Wormhole());
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    wormholes.add((FullTile) temp);
                                    break;
                                default:
                                    throw new GameException("Nepostojeca oznaka entiteta pri inicijalizaciji polja mape (" + q + ","+ r+ ")");
                            }
                            break;
                        case "FULL":
                            currEntity = mapNode.get(counter).get(counter2).get("entity").asText();
                            switch (currEntity){
                                case "FENCE":
                                    temp = new FullTile(q,r, new Fence());
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    fence.add((FullTile) temp);
                                    break;
                                case "BOSS":
                                    temp = new FullTile(q,r, new Boss());
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    wormholes.add((FullTile) temp);
                                    break;
                                default:
                                    throw new GameException("Nepoznat tip ostrava");
                            }
                            break;
                        default:
                            throw new GameException("Nepoznat tip polja mape");
                    }

                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public Tile getTile(int q, int r) {
        return tilemap.get(hash(q,r,-q-r));
    }


   /* public void setEntity(int r, int q, Entity entity){
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
    }*/




    private int hash(int q, int r, int s){
        if(abs(q) == 1){
            q=q*17;
        }
        if(abs(q) == 1){
            r=r*19;
        }
        if(abs(q) == 1){
            s=s*23;
        }
        int x = q*11+r*151+s*257;
        return x;
    }

}
