package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import aibg.logika.Game.GameParameters;
import aibg.logika.Map.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.Integer.max;
import static java.lang.Math.abs;

@Getter
@Setter
@NoArgsConstructor
public class Player implements Entity {
    String type = "PLAYER";
    protected int r; // horizontala
    protected int q; // glavna dijagonala
    @JsonIgnore
    protected Spawnpoint spawnpoint;
    protected int playerIdx;
    protected String name;
    @JsonIgnore
    protected Map map;
    @JsonIgnore
    protected int starting_health = 1000;
    protected int health = GameParameters.STARTING_HEALTH;
    @JsonIgnore
    protected int starting_power = 150;
    protected int power = GameParameters.STARTING_POWER;
    protected int level = 1;
    @JsonIgnore
    protected int experience = 0;
    protected int kills = 0;
    protected int deaths = 0;
    protected int score = 0;
    protected double KD = 0;
    protected boolean trapped=false;
    protected int trapDuration = 0;

    public Player(Spawnpoint spawnpoint, int playerIdx, String name, Map map) {
        r = spawnpoint.getR();
        q = spawnpoint.getQ();
        this.spawnpoint = spawnpoint;
        this.playerIdx = playerIdx;
        this.name = name;
        this.map = map;
    }

    public void increaseExperience(int inc){
        experience+=inc;
        levelUp();
    }
    public void increaseScore(int inc){
        score+=inc;
    }

    public void illegalAction(){
            health -= health/10;
    }


    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        player.illegalAction();
    }

    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        if(attacker instanceof Player) {
            if( attacker == this ) {
                this.illegalAction(); //zabranjen napad na sebe
                return;
            }
            health-= ((Player) attacker).getPower();
            ((Player)attacker).increaseExperience(GameParameters.EXP_ON_HIT);
            if(health<=0){
                this.deaths++;
                this.setTrapped(false);
                ((Player)attacker).increaseExperience(GameParameters.EXP_ON_KILL);
                ((Player)attacker).kills++;
                ((Player)attacker).setKD(functionKD(((Player) attacker).getKills(),((Player) attacker).getDeaths()));
                this.setKD(functionKD(this.kills, this.deaths));
                respawn(game);
            }
        } else if (attacker instanceof Boss) {
            this.health-= ((Boss) attacker).getPower();
            if(health<=0){
                this.deaths++;
                this.setKD(functionKD(this.kills, this.deaths));
                respawn(game);
            }
        }
    }

    public void respawn(Game game){
        Spawnpoint sp = this.spawnpoint;
        boolean occupied = false;
        int occCnt = 0;
        do {
            for (Player player : game.getPlayers().values()) {
                if (player.getPlayerIdx() == this.playerIdx)
                    continue;
                if (player.getQ() == sp.getQ() && player.getR() == sp.getR()) {
                    sp.setR(spawnpoint.getR() + (occupied?-1:1)*(sp.getR()<0?1:-1));   //prvo ce da proba da ga stavi na polje iznad, drugi put na polje ispod
                    sp.setQ(spawnpoint.getQ() + (occupied?-1:1)*(sp.getQ()>0?1:-1)*(sp.getR()*sp.getQ()>0?1:0)); //nmg da objasnim imam nacrtano u beleskama, pitaj
                    occupied = true;
                    occCnt++;
                    break;
                }
            }
        }while(occupied && occCnt<2);

        r = sp.getR();
        q = sp.getQ();
        health = starting_health;
    }
    @JsonIgnore
    public boolean isZoneOne(){
        int max = max(abs(q), max(abs(r), abs(-r-q)));
        return ( max < 5 && max>= 2 );
    }

    // treba dodati gde ce se pozivati, mozda posle attack-a ?
    // da li se povecavaju helti ?
    public void levelUp(){
        if (experience >= GameParameters.EXP_TO_LVL_UP) {
            if(level < GameParameters.MAX_LEVEL){
                level++;
                experience -= GameParameters.EXP_TO_LVL_UP; // vraca na nulu (+ visak ako je prebacio granicu)
                this.setStarting_health(starting_health + GameParameters.HEALTH_INCREMENT);
                health = starting_health;
                this.setStarting_power(starting_power + GameParameters.POWER_INCREMENT);
                power = starting_power;
            }
        }
    }

    public void heal(){
        health = starting_health;
    }

    public double functionKD(int kills, int deaths){
        if(deaths!=0) {
            this.KD = (double) kills / (double) deaths;
        }
        else {
            this.KD = (double) kills;
        }
        return KD;
    }

}
