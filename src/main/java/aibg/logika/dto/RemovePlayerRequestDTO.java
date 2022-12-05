package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemovePlayerRequestDTO extends DTO{
    private int gameId;
    private int playerIdx;

    public RemovePlayerRequestDTO(int gameId, int playerIdx) {
        this.gameId = gameId;
        this.playerIdx = playerIdx;
    }
}
