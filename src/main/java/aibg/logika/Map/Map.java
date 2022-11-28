package aibg.logika.Map;

import aibg.logika.Game.GameException;
import aibg.logika.Map.Entity.*;
import aibg.logika.Map.Tile.Tile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Math.*;

@Getter
@NoArgsConstructor
public class Map implements Serializable {
    // https://www.redblobgames.com/grids/hexagons/
// prva koordinata je r (horizontala), druga je q (glavna dijagonala),

    int size;
    private ArrayList<ArrayList<Tile>> tiles;
    @JsonIgnore
    private ArrayList<Tile> wormholes;
    @JsonIgnore
    private HashMap<Integer, Tile> tilemap;
    @JsonIgnore
    private ArrayList<Tile> fence;
    @JsonIgnore
    private Boss hugoBoss;
    @JsonIgnore
    private Empty emptyObj;


    public Map(int n, URL path) throws GameException {
        this.size = n;
        tilemap = new HashMap<>();
        tiles = new ArrayList<>();
        wormholes = new ArrayList<>();
        fence = new ArrayList<>();
        hugoBoss = new Boss();
        emptyObj=new Empty();
        loadMap(path);
    }

    private void loadMap(URL path) throws GameException{
        String mapJsonString;
        int counter = -1;
        int counter2;
        try{
            mapJsonString = new Scanner(path.openStream(), "UTF-8").useDelimiter("\\A").next();

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
                                    temp = new Tile(q,r, "NORMAL", emptyObj);
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    break;
                                case "BLACKHOLE":
                                    temp = new Tile(q,r, "NORMAL",new Blackhole());
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    break;
                                case "WORMHOLE":
                                    int id = mapNode.get(counter).get(counter2).get("id").asInt();
                                    temp = new Tile(q,r, "FULL", new Wormhole(q,r,id));
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    wormholes.add((Tile) temp);
                                    break;
                                default:
                                    throw new GameException("Nepostojeca oznaka entiteta pri inicijalizaciji polja mape (" + q + ","+ r+ ")");
                            }
                            break;
                        case "FULL":
                            currEntity = mapNode.get(counter).get(counter2).get("entity").asText();
                            switch (currEntity){
                                case "FENCE":
                                    temp = new Tile(q,r, "FULL",new Fence());
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
                                    fence.add((Tile) temp);
                                    break;
                                case "BOSS":
                                    temp = new Tile(q, r, "FULL",hugoBoss);
                                    tiles.get(counter).add(temp);
                                    tilemap.put(hash(q,r,-q-r),temp);
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
            connectWormholes();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public Tile getTile(int q, int r) {
        return tilemap.get(hash(q,r,-q-r));
    }

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

    /** Conncects 1st & 3rd and 2nd & 4th loaded wormholes */
    private void connectWormholes(){
        for(int i=0;i<wormholes.size();i++){
            for(int j=i+1;j<wormholes.size();j++){
                if( ((Wormhole)(wormholes.get(i).getEntity())).getId()==((Wormhole)(wormholes.get(j).getEntity())).getId() ){
                    ((Wormhole)(wormholes.get(i).getEntity())).connect((Wormhole)(wormholes.get(j).getEntity()));
                    break;
                }
            }
        }
    }

}
