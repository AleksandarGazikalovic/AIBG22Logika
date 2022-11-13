package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoActionResponseDTO extends DTO{
    String message;
    String gameState;

    public DoActionResponseDTO(String message, String gameState) {
        this.message = message;
        this.gameState = gameState;
    }
}
