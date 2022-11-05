package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainResponseDTO extends DTO{

    private String message;
    private String gameState;
    private String players;

    public TrainResponseDTO(String message, String gameState, String players) {
        this.message = message;
        this.gameState = gameState;
        this.players = players;
    }
}
