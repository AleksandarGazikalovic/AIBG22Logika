package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStateRequestDTO extends DTO{
    private int gameId;
    private String mapName;

    public GameStateRequestDTO(int gameId, String mapName) {
        this.gameId = gameId;
        this.mapName = mapName;
    }
}
