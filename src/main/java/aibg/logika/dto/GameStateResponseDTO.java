package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStateResponseDTO extends DTO{
    private String gameState;

    public GameStateResponseDTO(String gameState) {
        this.gameState = gameState;
    }
}
