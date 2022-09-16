package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerViewResponseDTO extends DTO{

    private String gameState;

    public PlayerViewResponseDTO(String gameState) {
        this.gameState = gameState;
    }
}
