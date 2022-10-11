package aibg.logika.Map.Entity;

import aibg.logika.Game.GameParameters;
import aibg.logika.Map.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Integer.max;
import static java.lang.Math.abs;

@Getter
@Setter
public class Player extends LiveEntity {
    String type = "PLAYER";
    protected int r; // horizontala
    protected int q; // glavna dijagonala
    protected int playerIdx;
    @JsonIgnore
    protected Map map;
    private int health = GameParameters.STARTING_HEALTH;
    private int power = GameParameters.STARTING_POWER;
    private int level = 1;
    @JsonIgnore
    private int experience = 0;
    @JsonIgnore //da li im prosledjivati poene, mozda neko smisli taktiku da jure najboljeg
    private int score = 0;
    private boolean trapped=false;
    private int points = 0;
    @JsonIgnore // Zone kao int od 1 do 3 gde je najdalja zona 3 (pocetna)
    private int zone = 3;

    public Player(int r, int q, int playerIdx, Map map) {
        this.r = r;
        this.q = q;
        this.playerIdx = playerIdx;
        this.map = map;
    }


    public void move(String direction) {
        int rInitial = r; //stare vrednosti lokacije, kako bi entity postavili na null
        int qInitial = q;
        switch (direction) {
            case "nw":
                r--;
                break;
            case "ne":
                q++;
                r--;
                break;
            case "w":
                q--;
                break;
            case "e":
                q++;
                break;
            case "sw":
                q--;
                r++;
                break;
            case "se":
                r++;
                break;
        }

        // provera da li je igrac usao  wormhole
        /*if(map.isWormhole(r, q)){
            if(map.isFree(-rInitial, -qInitial)){
                r =-rInitial;
                q = -qInitial;
            }else {
                r = rInitial; // ako je polje zauzeto samo cemo vratiti igraca gde se nalazio
                q = qInitial;
            }
        } else if(!(map.isFree(r,q))){
            r = rInitial;
            q = qInitial;
        }

        map.setEntity(rInitial, qInitial, null); //stara lokacija
        map.setEntity(r, q, this); // nova lokacija


        if(r==rInitial && q==qInitial){
            System.out.println("Polje je zauzeto ili je outOfBound");
        }*/
    }

    public void increaseExperience(int inc){
        experience+=inc;
    }
    // TODO menjati level i power na osnovu experience-a // mozda spojiti experience i score u jednmu promenjlivu?
    public void increaseScore(int inc){
        score+=inc;
    }

    public void illegalAction(){
        // TODO when illegal action happens
    }


    @Override
    public void stepOn(Player player, Map map, int q, int r) {
        updateZone();
        //illegal ili da ga rani
        player.illegalAction();
    }

    @Override
    public void attacked(LiveEntity attacker, Map map, int q, int r) {
        health-= attacker.getPower();
        if(health<=0){
            //TODO vratiti na njegovo pocetno polje i smanjiti score
        }
    }





    public void updateZone(){
        int max = abs(max(q, max(r, -r-q)));
        if( 14 >= max && max <= 11 )
            zone = 3;
        else if(max <11 && max >= 5)
            zone = 2;
        else if(max < 5 && max>= 2)
            zone= 1;
    }
}
