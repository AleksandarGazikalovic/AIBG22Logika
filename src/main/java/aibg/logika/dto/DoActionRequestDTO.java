package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoActionRequestDTO extends DTO{
    private int playerIdx;
    private String gameState;
    private String Action;

    public DoActionRequestDTO(int playerIdx, String gameState, String Action) {
        this.playerIdx = playerIdx;
        this.gameState = gameState;
        this.Action = Action;
    }

}
