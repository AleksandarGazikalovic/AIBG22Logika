package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerViewRequestDTO extends DTO{
    private int playerIdx;
    private String gameState;
}
