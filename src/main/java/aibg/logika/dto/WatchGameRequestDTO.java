package aibg.logika.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WatchGameRequestDTO extends DTO{
    private int gameId;

    public WatchGameRequestDTO(int gameId) {
        this.gameId = gameId;
    }
}
