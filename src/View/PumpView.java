package View;

import Model.Element;
import Model.Pump;

import java.awt.*;
import java.io.Serializable;

/**
 * A pumpa kirajzolását megvalósító osztály
 */
public class PumpView extends ElementView {
    /**
     * A nézethez tartozó pumpa
     */
    private Pump pump;

    /**
     * Az osztály paraméteres konstruktora
     * @param pump
     */
    public PumpView(Pump pump){
        this.pump = pump;
    }

    /**
     * A pumpa kirajzolásért felelős függvény
     * Amennyiben a pumpa törött, akkor egy fekete körrel jelzi azt
     * @param g
     */
    public void Update(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 96, 71));
        Point center = GetCenterCoordinates();
        Point ovalPoint = new Point(center.GetX()-20, center.GetY()-20);
        g2d.fillOval(ovalPoint.GetX(), ovalPoint.GetY(), 40, 40);
        if(pump.GetBrokenness()){
            g2d.setColor(new Color(0, 0, 0));
            g2d.fillOval(ovalPoint.GetX() + 15, ovalPoint.GetY() + 15, 10, 10);
        }
    }

    /**
     * @return Visszatéríti a nézethez tartozó elemet
     */
    @Override
    public Element GetElement() {
        return pump;
    }
}
