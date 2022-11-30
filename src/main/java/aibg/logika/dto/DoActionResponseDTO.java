package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoActionResponseDTO extends DTO{
    String message;
    String gameState;
    String playerAttack;

    public DoActionResponseDTO(String message, String gameState, String playerAttack) {
        this.message = message;
        this.gameState = gameState;
        this.playerAttack = playerAttack;
    }
}
