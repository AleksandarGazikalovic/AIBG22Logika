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
    protected int health = GameParameters.STARTING_HEALTH;
    protected int power = GameParameters.STARTING_POWER;
    protected int level = 1;
    @JsonIgnore
    protected int experience = 0;
    protected int kills = 0;
    protected int deaths = 0;
    protected int score = 0;
    protected float KD = 0;
    protected boolean trapped=false;

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
        // TODO when illegal action happens
    }


    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        //illegal ili da ga rani
        player.illegalAction();
    }

    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        if(attacker instanceof Player) {
            health-= ((Player) attacker).getPower();
            if(health<=0){
                deaths++;
                ((Player)attacker).increaseExperience(100);
                ((Player)attacker).kills++;
                ((Player)attacker).setKD(functionKD(((Player) attacker).getKills(),((Player) attacker).getDeaths()));
                this.setKD(functionKD(this.kills, this.deaths));
                respawn(game);
            }
        } else if (attacker instanceof Boss) {
            health-= ((Boss) attacker).getPower();
            if(health<=0){
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
        health = GameParameters.STARTING_HEALTH;
    }

    public boolean isZoneOne(){
        int max = abs(max(q, max(r, -r-q)));
        return ( max < 5 && max>= 2 );
    }

    // treba dodati gde ce se pozivati, mozda posle attack-a ?
    // da li se povecavaju helti ?
    public void levelUp(){
        if (experience >= GameParameters.EXP_TO_LVL_UP) {
            if(level < GameParameters.MAX_LEVEL){
                level++;
                experience -= GameParameters.EXP_TO_LVL_UP; // vraca na nulu (+ visak ako je prebacio granicu)
                switch (level)   {
                    case 2:{
                        power += GameParameters.LEVEL_2_POWER_INCREMENT;
                        break;
                    }
                    case 3:{
                        power += GameParameters.LEVEL_3_POWER_INCREMENT;
                        break;
                    }
                    case 4:{
                        power += GameParameters.LEVEL_4_POWER_INCREMENT;
                        break;
                    }
                    case 5:{
                        power += GameParameters.LEVEL_5_POWER_INCREMENT;
                        break;
                    }
                }
            }
        }
    }

    public void heal(){
        health = health + GameParameters.HEAL;
        if(health > GameParameters.STARTING_HEALTH) health = GameParameters.STARTING_HEALTH;
    }

    public float functionKD(int kills, int deaths){
        if(deaths!=0) {
            float KD = (float) kills / (float) deaths;
        }
        else {
            float KD = (float) kills;
        }
        return KD;
    }

}
