package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainGameStateRequestDTO extends DTO{
    private String mapName;
    private Integer gameId;
    private Integer playerIdx;
}
