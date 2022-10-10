package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoActionResponseDTO extends DTO{

    String gameState;

    public DoActionResponseDTO(String gameState) {
        this.gameState = gameState;
    }
}
