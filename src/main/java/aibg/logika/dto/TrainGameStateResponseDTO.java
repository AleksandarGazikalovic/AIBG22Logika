package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainGameStateResponseDTO extends DTO{
    private String gameState;

    public TrainGameStateResponseDTO(String gameState) {
        this.gameState = gameState;
    }
}
