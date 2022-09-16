package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStateRequestDTO extends DTO{
    private int playerIdx;
    private String gameState;
}
