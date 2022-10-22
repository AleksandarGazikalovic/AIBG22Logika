package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainRequestDTO extends DTO{

    //private String mapName; - ovo zasad ne treba
    //private Integer startPosition; - ni ovo ne treba

    private Integer gameId;
    private String action;

}
