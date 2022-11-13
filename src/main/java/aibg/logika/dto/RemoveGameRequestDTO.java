package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveGameRequestDTO extends DTO{

    private int gameID;
    private String gameType;

}
