package aibg.logika.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerAttackDTO {
    private int playerIdx;
    private int q;
    private int r;

    public PlayerAttackDTO(int playerIdx, int q, int r) {
        this.playerIdx = playerIdx;
        this.q = q;
        this.r = r;
    }
}
