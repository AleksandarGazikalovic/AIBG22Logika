package aibg.logika.Map.Entity;

import aibg.logika.Action.Direction;
import aibg.logika.Game.Game;
import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;


import static aibg.logika.Game.GameParameters.BOSS_POWER;


@Getter
@Setter
public class Boss implements Entity{
    String type = "BOSS";
    @JsonIgnore
    int power=BOSS_POWER;
    @JsonIgnore
    int counter = 0;
    @JsonIgnore
    private static int randomDelayCounter = 0;
    @JsonIgnore
    int pattern=0;
    @JsonIgnore
    private Tile[] directions = new Tile[]{
            new Tile(Direction.E.q, Direction.E.r),
            new Tile(Direction.SE.q, Direction.SE.r),
            new Tile(Direction.SW.q, Direction.SE.r),
            new Tile(Direction.W.q, Direction.W.r),
            new Tile(Direction.NW.q, Direction.NW.r),
            new Tile(Direction.NE.q, Direction.NE.r )
    };

    private boolean bossAction = false;
    private ArrayList<Tile> bossAttackedTiles = new ArrayList<>();
    @JsonIgnore
    private int[] radius = new int[]{6,8,10};
    @JsonIgnore
    int  range=0;

    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        player.illegalAction();
    }


    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        ((Player)attacker).increaseScore(((Player) attacker).getPower());
    }


    public int getPower() {
        return BOSS_POWER;
    }


    public void turn(Game game, HashMap<Integer, Player> players){
        bossAttackedTiles.clear();
        attackZoneOne(game, players);
        attackZoneTwo(game, players);
        bossAction = true;
    }
    public void attackZoneOne(Game game, HashMap<Integer, Player> players){
        // Attacks all the players in the first zone.
        for(Player player : players.values()){
            if(player.isZoneOne()){
                player.attacked(this, game, player.q, player.r);
                bossAttackedTiles.add(game.getMap().getTile(player.q, player.r));
            }
        }
    }

    public void attackZoneTwo(Game game, HashMap<Integer, Player> players){
        //Ako je randomDelayCounter 0, radi se patern odredjen vrednosti promenljive pattern; u samom paternu, kada se zavrsi, se
        //postavljaju nove vrednosti promenljive randomDelayCounter i inkrementira pattern promenljiva

        if(randomDelayCounter==0) {
            switch (pattern) {
                case 0:
                    pattern++; //trenutno nema nultog paterna
                    break;
                case 1:
                    Tile start1 = game.getMap().getTile(-6 + counter, -2- counter);
                    Tile start2 = game.getMap().getTile(-5 +counter,-3 -counter);//jesu ovo pocetne
                    patern1(game, players, start1, start2);
                    break;
                case 2:
                    patern2(game, players);
                    break;
                default:
                    pattern = 1;   //kad prodje sve da pocne od 1 opet
            }
        }else
            randomDelayCounter--;
    }

    public void patern1(Game game, HashMap<Integer, Player> players, Tile start1, Tile start2){
        counter+=2;

        ArrayList<Tile> allAttackedTiles = new ArrayList<Tile>();
        allAttackedTiles.add(start1);
        allAttackedTiles.add(start2);
        allAttackedTiles.add(symmetryPerDiagonal(game.getMap(),start1));
        allAttackedTiles.add(symmetryPerDiagonal(game.getMap(),start2));
        allAttackedTiles.add(symmetryPerY(game.getMap(),start1));
        allAttackedTiles.add(symmetryPerY(game.getMap(),start2));
        allAttackedTiles.add(symmetryPerY(game.getMap(),symmetryPerDiagonal(game.getMap(),start1)));
        allAttackedTiles.add(symmetryPerY(game.getMap(),symmetryPerDiagonal(game.getMap(),start2)));

        attackTiles(game,players,allAttackedTiles);
        bossAttackedTiles.addAll(allAttackedTiles);

        if(counter == 6) {
            pattern++;
            randomDelayCounter=Random(3,7);
            counter=0;
        }
    }

    //prsten napad
    public void patern2(Game game, HashMap<Integer, Player> players){

        ArrayList<Tile> ring=cube_ring(game.getMap(),radius[range]);
        range++;
        if(range == 2){
            range=0;
        }

        attackTiles(game,players,ring);
        bossAttackedTiles.addAll(ring);

        pattern++;
        randomDelayCounter=Random(3,7);
    }

    /** Attacks tiles forwarded as parameter allAttackedTiles */
    private void attackTiles(Game game, HashMap<Integer, Player> players,ArrayList<Tile> allAttackedTiles){
        boolean attackedPlayerOnTile = false;

        for (Tile attackedTile : allAttackedTiles){
            if(attackedTile.getEntity() instanceof Empty || attackedTile.getEntity() instanceof Blackhole){     //iskreno izbacio bih ovaj if svakako, ako nisu polja ovog tipa ignorisace napad
                for(Player player : players.values()){
                    if(player.getQ() == attackedTile.getQ() && player.getR() == attackedTile.getR()) {
                        player.attacked(this, game, player.getQ(), player.getR());
                        attackedPlayerOnTile = true;
                        break;
                    }
                }
                if(attackedPlayerOnTile != true)
                    attackedTile.getEntity().attacked(this, game, attackedTile.getQ(), attackedTile.getR());
                else
                    attackedPlayerOnTile = false;
            }
        }
    }

    //pomocne funkcije
    public Tile pom1(Map map, Tile hex, Direction direction) {
        return map.getTile(hex.getQ() + direction.q, hex.getR() + direction.r);
    }
    private Tile pom2(Map map, Tile hex, int radius){
        return map.getTile(hex.getQ() * radius, hex.getR() * radius);
    }
    private Tile pom3(Map map,Tile prvi, Tile drugi){
        return map.getTile(prvi.getQ() + drugi.getQ(), prvi.getR() + drugi.getR());
    }

    /** Retrurns list of tiles in a ring of specified @radius around center of the map */
    private ArrayList<Tile> cube_ring(Map map, int radius) {
        ArrayList<Tile> results = new ArrayList<Tile>();

        Tile hex = pom3(map ,map.getTile(0,0), pom2(map,pom1(map, map.getTile(0,0),Direction.NW), radius));
        for(int i = 0;i<6;i++) {
            for (int j = 0; j < radius; j++) {
                if(hex.getEntity() instanceof Empty) {
                    results.add(hex);
                }
                hex = pom3(map,hex, directions[i]);
            }
        }
        return results;
    }

    private Tile symmetryPerY(Map map,Tile start){
        return map.getTile(-(start.getQ()+start.getR()), start.getR()); //ovo prvo je s koordinata, q treba zameni mesto sa s, r ostaje isto
    }

    private Tile symmetryPerDiagonal(Map map,Tile start){
        return map.getTile(start.getQ()*(-1), start.getR()*(-1));
    }

    private static int Random(int min,int max){
        int range = max - min + 1;

        return (int)(Math.random() * range) + min;
    }

}
