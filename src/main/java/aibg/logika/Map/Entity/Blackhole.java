package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO ne sme da se pridje polje do BlackHole, neki zaseban Entity
@Getter
@Setter
@NoArgsConstructor
public class Blackhole implements Entity{

    String type = "BLACKHOLE";

    @JsonIgnore
    Player trappedPlayer=null;


    /** Moves and traps the player for 1 move if blackhole is empty*/
    @Override
    public void stepOn(Player player, Game game, int q, int r) {
        if(trappedPlayer==null) {
            player.setQ(q);
            player.setR(r);
            trappedPlayer = player;
            player.setTrapped(true);
        }else{
            player.illegalAction();
        }
    }

    /** Sends the attack to trapped player, if there is one*/
    @Override
    public void attacked(Entity attacker, Game game, int q, int r) {
        if(trappedPlayer!=null){
            trappedPlayer.attacked(attacker, game, q, r);
        }
        /*else{attacker.illegalAction();}*/
    }


    public void releasePlayer(){ //trenutno jedan potez traje zarobljenost, moze se povecati uvodjenjem brojaca
        if(trappedPlayer!=null){
            trappedPlayer.setTrapped(false);
            trappedPlayer=null;
        }

    }


}
