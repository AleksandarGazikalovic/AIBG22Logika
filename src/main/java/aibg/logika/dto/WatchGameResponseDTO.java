package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchGameResponseDTO extends DTO {
    private String gameState;

    public WatchGameResponseDTO(String gameState) {
        this.gameState = gameState;
    }
}
