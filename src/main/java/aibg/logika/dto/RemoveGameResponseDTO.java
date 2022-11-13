package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveGameResponseDTO extends DTO{
    private boolean success;

    public RemoveGameResponseDTO(boolean b) {
        success=b;
    }
}
