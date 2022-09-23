package aibg.logika.Map.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fence implements Entity{
    String type;

    public Fence() {
        type="FENCE";
    }
}
