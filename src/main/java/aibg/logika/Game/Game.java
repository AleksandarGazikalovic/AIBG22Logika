package aibg.logika.Game;

import aibg.logika.Map.Entity.Boss;
import aibg.logika.Map.Entity.Player;
import aibg.logika.Map.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

@Getter
@Setter
public class Game implements Serializable {
    private Map map;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    @JsonIgnore
    private Boss hugoBoss;

    public Game(Map map) {
        this.map = map;
        player1 = new Player(-7, -7,1, this.map);
        player2 = new Player(14, -7,2, this.map);
        player3 = new Player(7, 7,3, this.map);
        player4 = new Player(-14, 7,4, this.map);

    }

}
