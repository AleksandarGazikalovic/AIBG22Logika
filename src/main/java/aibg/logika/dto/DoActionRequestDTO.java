package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoActionRequestDTO extends DTO{
    private int playerIdx;
    private String gameState;
    private String Action;

}
