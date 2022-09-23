package aibg.logika.Game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {

    private String gameState;

    public Game(String gameState) {
        this.gameState = gameState;
    }
}
