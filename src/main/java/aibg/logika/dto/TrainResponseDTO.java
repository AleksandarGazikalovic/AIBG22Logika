package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainResponseDTO extends DTO{

    private String message;
    private String gameState;

    public TrainResponseDTO(String message, String gameState) {
        this.message = message;
        this.gameState = gameState;
    }
}
