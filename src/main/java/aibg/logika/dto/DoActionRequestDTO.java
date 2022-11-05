package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoActionRequestDTO extends DTO{
    private int playerIdx;
    private String action;
    private int gameId;

    public DoActionRequestDTO(int playerIdx, String action, int gameId) {
        this.playerIdx = playerIdx;
        this.action = action;
        this.gameId= gameId;
    }

}
