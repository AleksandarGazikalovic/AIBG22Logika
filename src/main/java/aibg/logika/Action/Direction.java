package aibg.logika.Action;

public enum Direction {

    E(+1,0),
    SE(0,+1),
    SW(-1,+1),
    W(-1,0),
    NW(0,-1),
    NE(+1,-1);



    public int q;
    public int r;



    Direction(int q, int r) {
        this.q = q;
        this.r = r;
    }


}
