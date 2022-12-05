package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemovePlayerResponseDTO extends DTO{
    private String gameState;

    public RemovePlayerResponseDTO(String gameState) {
        this.gameState = gameState;
    }
}
