package aibg.logika.Map.Entity;

import aibg.logika.Action.Direction;
import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;


import static aibg.logika.Game.GameParameters.BOSS_POWER;


@Getter
@Setter
public class Boss extends LiveEntity{

    int counter = 0;
    private static int randomDelayCounter = 0;
    boolean finishedPatern1 = true;
    boolean attackedPlayerOnTile = false;

    private Tile[] directions = new Tile[]{
            new Tile(Direction.E.q, Direction.E.r),
            new Tile(Direction.SE.q, Direction.SE.r),
            new Tile(Direction.SW.q, Direction.SE.r),
            new Tile(Direction.W.q, Direction.W.r),
            new Tile(Direction.NW.q, Direction.NW.r),
            new Tile(Direction.NE.q, Direction.NE.r )};

    @Override
    public void stepOn(Player player, Map map, int q, int r) {
        player.illegalAction();
    }


    @Override
    public void attacked(LiveEntity attacker, Map map, int q, int r) {
        // TODO povecaj stats playera
        ((Player)attacker).increaseScore(attacker.getPower());
        ((Player)attacker).increaseExperience(attacker.getPower());
    }

    @Override
    public int getPower() {
        return BOSS_POWER;
    }


    public void turn(Map map, HashMap<Integer, Player> players){
        attackZoneOne(map, players);
        attackZoneTwo(map, players);
    }
    public void attackZoneOne(Map map, HashMap<Integer, Player> players){
        // Attacks all the players in the first zone.
        for(Player player : players.values()){
            if(player.getZone()==1){
                player.attacked(this,map,player.q, player.r);
            }
        }
    }

    public void attackZoneTwo(Map map, HashMap<Integer, Player> players){
        // We have two patterns and a counter that keeps track of which one to call
        // The first pattern lasts for more turns unlike the second which lasts just one turn.
        // In order to keep track which pattern to call, we have the values 'patern' and finishedPatern1
        // pattern1 is called for patern = 0, 1, 2, 3, 4. When the first is finished it resets the finishedPAtern1 to true.

        if((counter == 0 && randomDelayCounter == 0) || finishedPatern1 == false){
            Tile start = map.getTile(-6,-2);
            patern1(map, players,start, counter);
            counter++;
        } else if(randomDelayCounter==0){
            patern2(map, players);
            counter = 0;
        }else{
            randomDelayCounter--;
        }
    }

    public void patern1(Map map, HashMap<Integer, Player> players, Tile start, int counter){

            start.setQ(start.getQ() + counter);
            start.setR(start.getR() - counter);

            ArrayList<Tile> allAttackedTile = new ArrayList<Tile>();
            allAttackedTile.add(start);
            allAttackedTile.add(symmetryPerDiagonal(start));
            allAttackedTile.add(symmetryPerY(start));
            allAttackedTile.add(symmetryPerY(symmetryPerDiagonal(start)));


            for (Tile attackedTile : allAttackedTile){
                if(attackedTile.entity() instanceof Empty){
                    for(Player player : players.values()){
                        if(player.getQ() == attackedTile.getQ() && player.getR() == attackedTile.getR()) {
                            player.attacked(this, map, player.getQ(), player.getR());
                            attackedPlayerOnTile = true;
                            break;
                        }
                    }
                    if(attackedPlayerOnTile != true)
                        attackedTile.entity().attacked(this, map, attackedTile.getQ(), attackedTile.getR());
                    attackedPlayerOnTile = false;
                }
            }

            if(counter == 4) {
                finishedPatern1 = true;
                randomDelayCounter=Random();
            } else{
                finishedPatern1 = false;
            }

        }

    //prsten napad
    public void patern2(Map map, HashMap<Integer, Player> players){

        ArrayList<Tile> ring = cube_ring(map.getTile(0,0),6);

        for (Tile attackedTile : ring){
            if(attackedTile.entity() instanceof Empty){
                for(Player player : players.values()){
                    if(player.getQ() == attackedTile.getQ() && player.getR() == attackedTile.getR()) {
                        player.attacked(this, map, player.getQ(), player.getR());
                        attackedPlayerOnTile = true;
                        break;
                    }
                }
                if(attackedPlayerOnTile != true)
                    attackedTile.entity().attacked(this, map, attackedTile.getQ(), attackedTile.getR());
                attackedPlayerOnTile = false;
            }
        }
        randomDelayCounter=Random();
    }
    //pomocne funkcije


    public Tile pom1(Tile hex, Direction direction) {
        return new Tile(hex.getQ() + direction.q, hex.getR() + direction.r);
    }
    private Tile pom2(Tile hex, int radius){
        return new Tile(hex.getQ() * radius, hex.getR() * radius);
    }
    private Tile pom3(Tile prvi, Tile drugi){
        return new Tile(prvi.getQ() + drugi.getQ(), prvi.getR() + drugi.getR());
    }


    private ArrayList<Tile> cube_ring(Tile centar, int radius) {
        ArrayList<Tile> results = new ArrayList<Tile>();

        Tile hex = pom3(centar, pom2(pom1(centar,Direction.NW), radius));
        for(int i = 0;i<6;i++) {
            for (int j = 0; j < radius; j++) {
                results.add(hex);
                hex = pom3(hex, directions[i]);
            }
        }
        return results;
    }

    private Tile symmetryPerY(Tile start){
        return new Tile(start.getR(), start.getQ());
    }

    private Tile symmetryPerDiagonal(Tile start){
        return new Tile(start.getQ()*(-1), start.getR()*(-1));
    }

    private static int Random(){
        int max = 7;
        int min = 3;
        int range = max - min + 1;

        int rand = (int)(Math.random() * range) + min;
        return rand;
    }

}
