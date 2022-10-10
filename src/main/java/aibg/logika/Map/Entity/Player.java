package aibg.logika.Map.Entity;

import aibg.logika.Game.GameParameters;
import aibg.logika.Map.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player implements Entity {
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
    private int points = 0;

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

    @Override
    public void stepOn(Player player, Map map, int q, int r) {

    }

    @Override
    public void attacked(Entity attacker, Map map, int q, int r) {

    }
}
