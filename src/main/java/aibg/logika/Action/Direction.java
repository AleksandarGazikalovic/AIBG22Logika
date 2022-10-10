package aibg.logika.Action;

public enum Direction {

    NE(+1,-1),
    NW(0,-1),
    W(-1,0),
    SW(-1,+1),
    SE(0,+1),
    E(+1,0);

    public int q;
    public int r;



    Direction(int q, int r) {
        this.q = q;
        this.r = r;
    }


}
