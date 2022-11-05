package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoActionResponseDTO extends DTO{
    String message;
    String gameState;
    String players;

    public DoActionResponseDTO(String message, String gameState, String players) {
        this.message = message;
        this.gameState = gameState;
        this.players = players;
    }
}
