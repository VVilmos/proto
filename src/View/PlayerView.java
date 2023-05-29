package View;

import java.awt.*;

/**
 * A játékosok megjelenítéséért felelős absztrakt osztály
 */
public abstract class PlayerView {

    /**
     * A játékosok frissítéséért, újra kirajzolásáért felelős absztrakt függvény
     */
    public abstract  void Update(Graphics g);
}
