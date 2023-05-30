package View;

import Model.Element;
import Model.Source;

import java.awt.*;
import java.io.Serializable;

/**
 * A forrás kirajzolását megvalósító osztály
 */
public class SourceView extends ElementView {
    /**
     * A nézethez tartozó forrás
     */
    private Source source;

    /**
     * Az osztály paraméteres konstruktora
     * @param source
     */
    public SourceView(Source source){
        this.source = source;
    }

    /**
     * A forrás kirajzolásért felelős függvény
     * @param g
     */
    public void Update(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(5, 203, 252));
        Point center = GetCenterCoordinates();
        Point ovalPoint = new Point(center.GetX()-20, center.GetY()-20);
        g2d.fillOval(ovalPoint.GetX(), ovalPoint.GetY(), 40, 40);
    }

    /**
     * @return Visszatéríti a nézethez tartozó elemet
     */
    @Override
    public Element GetElement() {
        return source;
    }
}
