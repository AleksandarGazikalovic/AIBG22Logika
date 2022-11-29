package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class GameStateRequestDTO extends DTO{
    @NotNull
    private int gameId;
    @NotEmpty
    private List<String> playerUsernames;
    private long time;
    private String mapName;

    public GameStateRequestDTO(int gameId, List<String> playerUsernames, long time, String mapName) {
        this.gameId = gameId;
        this.playerUsernames = playerUsernames;
        this.time = time;
        this.mapName = mapName;
    }

}
