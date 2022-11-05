package aibg.logika.Map.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Spawnpoint {
    private int r;
    private int q;

    public Spawnpoint(int q, int r) {
        this.r = r;
        this.q = q;
    }

}
