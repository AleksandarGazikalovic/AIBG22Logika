package aibg.logika.Map;

import aibg.logika.Entity.Player;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Kanvas extends Canvas {

    private Map map = new Map();
    private Player player;


    public Kanvas() {
        setBackground(Color.black);
        player =  new Player(0, 0, map);
        map.setEntity(0, 0, player);

        addKeyListener(new KeyAdapter(){
            //TODO na svaki potez se menjaju igraci
            //TODO ovde bi trebala while petlja za slucaj da se ne moze pomeriti na polje

            @Override
            public void keyTyped(KeyEvent e) {


                char key = Character.toUpperCase(e.getKeyChar());
                switch (key){
                    case KeyEvent.VK_Q: {
                        player.move("nw");
                        break;
                    }
                    case KeyEvent.VK_W: {
                        player.move("ne");
                        break;
                    }
                    case KeyEvent.VK_A: {
                        player.move("w");
                        break;
                    }
                    case KeyEvent.VK_S: {
                        player.move("e");
                        break;
                    }
                    case KeyEvent.VK_Z: {
                        player.move("sw");
                        break;
                    }
                    case KeyEvent.VK_X: {
                        player.move("se");
                        break;
                    }
                    default:
                        System.out.println("Pogresna komanda");
                        break;
                }
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        map.paint(g);
    }




}
