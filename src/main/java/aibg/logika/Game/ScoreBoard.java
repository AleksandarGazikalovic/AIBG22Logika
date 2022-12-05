package aibg.logika.Game;

import aibg.logika.Map.Entity.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

@Getter
@Setter
public class ScoreBoard {

    Player players[] = new Player[4];
    @JsonIgnore
    CustomComparator comparator = new CustomComparator();
    protected ScoreBoard(Player player1, Player player2, Player player3, Player player4){
        players[0] = player1;
        players[1] = player2;
        players[2] = player3;
        players[3] = player4;
    }

    public void update(){
        Arrays.sort(players, comparator);
    }

    public static class CustomComparator implements Comparator<Player> {
        @Override
        public int compare(Player player1, Player player2) {
            if(player1.getScore() > player2.getScore()){
                return -1;
            }else if(player1.getScore() == player2.getScore()){
                if(player1.getKD() > player2.getKD()){
                    return -1;
                }else if(player1.getKD() == player2.getKD()){
                    if(player1.getKills() > player2.getKills()){
                        return -1;
                    }else if(player1.getKills() == player2.getKills()){
                        if(player1.getDeaths() < player2.getDeaths()){
                            return -1;
                        }else if(player1.getDeaths() == player2.getDeaths()) {
                            if(player1.getLevel() > player2.getLevel()){
                                return -1;
                            }else if(player1.getLevel() == player2.getLevel()){
                                if (player1.getHealth() > player2.getHealth()) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }else{
                                return 1;
                            }
                        }else{
                            return 1;
                        }
                    }else{
                        return 1;
                    }
                }else{
                    return 1;
                }
            }else{
                return 1;
            }
        }

    }





//    private int score;
//    private int KDratio;
//    private int kills;
//    private int deaths;
//    private int health;







}
