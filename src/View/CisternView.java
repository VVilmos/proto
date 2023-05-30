package View;

import Model.Cistern;
import Model.Element;

import java.awt.*;
import java.io.Serializable;

/**
 * A ciszterna kirajzolását megvalósító osztály
 */
public class CisternView extends ElementView  {
    /**
     * A nézethez tartozó ciszterna
     */
    private Cistern cistern;

    /**
     * Az osztály paraméteres konstruktora
     * @param cistern
     */
    public CisternView(Cistern cistern){
        this.cistern = cistern;
    }

    /**
     * A ciszterna kirajzolásért felelős függvény
     * @param g
     */
    public void Update(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0));
        Point center = GetCenterCoordinates();
        Point ovalPoint = new Point(center.GetX()-20, center.GetY()-20);
        g2d.fillOval(ovalPoint.GetX(), ovalPoint.GetY(), 40, 40);
    }

    /**
     * @return Visszatéríti a nézethez tartozó elemet
     */
    @Override
    public Element GetElement() {
        return cistern;
    }
}
