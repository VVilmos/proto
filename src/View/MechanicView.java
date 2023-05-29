package View;

import Control.Main;
import Model.Mechanic;
import Model.Element;
import java.awt.*;
import java.util.Random;

/**
 * A játékban szereplő szerelők megjelenítésérrt felelős osztály
 */
public class MechanicView extends PlayerView{
    /**
     * A View-hoz tartozó szerelő
     */
    private Mechanic mechanic;

    /**
     * paraméteres konstruktor
     * @param m a szerelő, amely a View létrehozásához szükséges
     */
    public MechanicView(Mechanic m){
        this.mechanic = m;
    }

    /**
     * A szerelő újra kirajzolásáért, frissítéséért felelős függvény
     */
    @Override
    public void Update(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);

        Element on = mechanic.GetLocation();
        ElementView ev = Main.getInstance().GetElementView(on);
        Point location = ev.GetCenterCoordinates();

        Random random = new Random();
        int randNumX = random.nextInt(6);
        int randNumY = random.nextInt(6);

        int minusPlusX = random.nextInt(2);
        int minusPlusY = random.nextInt(2);

        if(minusPlusX == 0 && minusPlusY == 0){
            g2d.fillRect(location.GetX() - randNumX, location.GetY() - randNumY, 10, 35);
        }
        else if(minusPlusX == 0 && minusPlusY == 1){
            g2d.fillRect(location.GetX() - randNumX, location.GetY() + randNumY, 10, 35);
        }
        else if(minusPlusX == 1 && minusPlusY == 0){
            g2d.fillRect(location.GetX() + randNumX, location.GetY() - randNumY, 10, 35);
        }
        else if(minusPlusX == 1 && minusPlusY == 1) {
            g2d.fillRect(location.GetX() + randNumX, location.GetY() + randNumY, 10, 35);
        }
    }
}
