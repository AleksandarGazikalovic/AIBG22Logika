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
    int pattern=0;

    private Tile[] directions = new Tile[]{
            new Tile(Direction.E.q, Direction.E.r),
            new Tile(Direction.SE.q, Direction.SE.r),
            new Tile(Direction.SW.q, Direction.SE.r),
            new Tile(Direction.W.q, Direction.W.r),
            new Tile(Direction.NW.q, Direction.NW.r),
            new Tile(Direction.NE.q, Direction.NE.r )};

    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        player.illegalAction();
    }


    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        // TODO povecati exp i score nekim smislenim brojem :)
        ((Player)attacker).increaseScore(((Player) attacker).getPower());
        ((Player)attacker).increaseExperience(((Player) attacker).getPower());
    }


    public int getPower() {
        return BOSS_POWER;
    }


    public void turn(Game game, HashMap<Integer, Player> players){
        attackZoneOne(game, players);
        attackZoneTwo(game, players);
    }
    public void attackZoneOne(Game game, HashMap<Integer, Player> players){
        // Attacks all the players in the first zone.
        for(Player player : players.values()){
            if(player.isZoneOne()){
                player.attacked(this, game, player.q, player.r);
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
                    Tile start = game.getMap().getTile(-6, -2);    //jesu ovo pocetne
                    patern1(game, players, start);
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

    public void patern1(Game game, HashMap<Integer, Player> players, Tile start){

        start.setQ(start.getQ() + counter);
        start.setR(start.getR() - counter);
        counter++;

        ArrayList<Tile> allAttackedTiles = new ArrayList<Tile>();
        allAttackedTiles.add(start);
        allAttackedTiles.add(symmetryPerDiagonal(game.getMap(),start));
        allAttackedTiles.add(symmetryPerY(game.getMap(),start));
        allAttackedTiles.add(symmetryPerY(game.getMap(),symmetryPerDiagonal(game.getMap(),start)));

        attackTiles(game,players,allAttackedTiles);

        if(counter == 4) {  //valjda treba 7 polja da se predje, ne 4? //granica bi trebalo nekako automatski da se odredi na osnovu pocetne koord
            pattern++;
            randomDelayCounter=Random();
            counter=0;
        }

    }

    //prsten napad
    public void patern2(Game game, HashMap<Integer, Player> players){

        ArrayList<Tile> ring=cube_ring(game.getMap(),6);

        attackTiles(game,players,ring);

        pattern++;
        randomDelayCounter=Random();
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
                results.add(hex);
                hex = pom3(map,hex, directions[i]);
            }
        }
        return results;
    }

    private Tile symmetryPerY(Map map,Tile start){
        return map.getTile(start.getR(), start.getQ());
    }

    private Tile symmetryPerDiagonal(Map map,Tile start){
        return map.getTile(start.getQ()*(-1), start.getR()*(-1));
    }

    private static int Random(){
        int max = 7;
        int min = 3;
        int range = max - min + 1;

        int rand = (int)(Math.random() * range) + min;
        return rand;
    }

}
