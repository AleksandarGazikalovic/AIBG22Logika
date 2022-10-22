package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainResponseDTO extends DTO{

    private String gameState;

    public TrainResponseDTO(String gameState){
        this.gameState = gameState;
    }

}
