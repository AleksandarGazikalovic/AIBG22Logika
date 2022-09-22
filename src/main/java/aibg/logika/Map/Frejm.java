/*package aibg.logika.Map;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// q - gore levo, w - gore desno, a - levo, s - desno, z - dole levo, x - dole desno
// TODO ubaci Exception-i
// TODO sta se radi ako igrac koji je na potezu izabere da se pomeri na zauzetu poziciju

public class Frejm extends Frame {

    private Kanvas kanvas = new Kanvas();
    private Panel commandPanel = new Panel();

    public Frejm() {
        super("Simulator");
        setBounds(150, 0, 1200, 800);
        setResizable(false);

        add(commandPanel, BorderLayout.SOUTH);

        add(kanvas, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);

    }

    public static void main(String[] args) {
        new Frejm();

    }

}
*/