package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO extends DTO {
    String message;
    public ErrorResponseDTO(String message) {
        this.message=message;
    }
}
