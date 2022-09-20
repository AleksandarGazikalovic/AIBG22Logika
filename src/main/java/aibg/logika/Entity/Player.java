package aibg.logika.Entity;

import aibg.logika.Map.Map;

import java.awt.*;
// TODO posto se Player cuva u Tile onda je bolje da se cuva Tile u kome se nalazi nego koordinate Player-a
public class Player extends Entity{

    private Map map;

    public Player(int r, int q, Map map){
        super(r,q);
        this.map = map;
    }



    public void move(String direction){
        int rInitial = r; //stare vrednosti lokacije, kako bi entity postavili na null
        int qInitial = q;
        switch (direction){
            case "nw":
                r--;
                break;
            case "ne":
                q++;
                r--;
                break;
            case "w":
                q--;
                break;
            case "e":
                q++;
                break;
            case "sw":
                q--;
                r++;
                break;
            case "se":
                r++;
                break;
        }

        // provera da li je igrac usao  wormhole
        if(map.isWormhole(r, q)){
            if(map.isFree(-rInitial, -qInitial)){
                r =-rInitial;
                q = -qInitial;
            }else {
                r = rInitial; // ako je polje zauzeto samo cemo vratiti igraca gde se nalazio
                q = qInitial;
            }
        } else if(!(map.isFree(r,q))){
            r = rInitial;
            q = qInitial;
        }

        map.setEntity(rInitial, qInitial, null); //stara lokacija
        map.setEntity(r, q, this); // nova lokacija


        if(r==rInitial && q==qInitial){
            System.out.println("Polje je zauzeto ili je outOfBound");
        }
    }





















    public void paint(Graphics g){
        g.setColor(Color.white);
        g.fillOval(xCenter + 4*hexagonRadius*q + 2*hexagonRadius*r - 5, yCenter + 3*hexagonRadius*r - 5, 10, 10);
    }
}
