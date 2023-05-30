package View;

import Model.Element;
import Model.Pipe;

import java.awt.*;
import java.io.Serializable;

public class PipeView extends ElementView{
    /**
     * A cső, amit megjelenítünk.
     */
    private Pipe pipe;

    /**
     * A cső végeinek koordinátái, az összeköttetéshez.
     */
    private Point[] endPoints = new Point[2];

    /**
     * Konstruktor
     * @param pipe a megjelenítendő cső.
     */
    public PipeView(Pipe pipe) {
        this.pipe = pipe;
    }

    /**
     * Kirajzolja a csövet.
     * @param g amire rajzol.
     */
    @Override
    public void Update(Graphics g) {
        int mainStroke = 15;

        Graphics2D g2d = (Graphics2D) g;

        // Alap cső
        if(!pipe.GetWaterLevel()) {
            g2d.setColor(new Color(86, 93, 99));
        } else {
            g2d.setColor(new Color(14,135,204));
        }
        g2d.setStroke(new BasicStroke(mainStroke));
        g2d.drawLine(endPoints[0].GetX(), endPoints[0].GetY(), endPoints[1].GetX(), endPoints[1].GetY());

        // Csúszós cső
        if(pipe.GetSlipperyness()) {
            g2d.setColor(new Color(201, 223, 240));
            g2d.setStroke(new BasicStroke(mainStroke / 3));
            g2d.drawLine(endPoints[0].GetX(), endPoints[0].GetY(), endPoints[1].GetX(), endPoints[1].GetY());
        }

        // Ragadós cső
        if(pipe.GetStickyness()) {
            Point e1 = endPoints[0];
            Point e2 = endPoints[1];
            double dX = e2.GetX() - e1.GetX();
            double dY = e2.GetY() - e1.GetY();
            double distance = Math.sqrt(dX * dX + dY * dY);
            double pX = -dY / distance;
            double pY = dX / distance;
            g2d.setColor(new Color(39, 43, 46));
            g2d.setStroke(new BasicStroke(mainStroke / 3));
            g2d.drawLine(
                    endPoints[0].GetX() + (int)(mainStroke / 3 * pX),
                    endPoints[0].GetY() + (int)(mainStroke / 3 * pY),
                    endPoints[1].GetX() + (int)(mainStroke / 3 * pX),
                    endPoints[1].GetY() + (int)(mainStroke / 3 * pY));
        }

        // Lyukas cső
        if(pipe.GetBrokennes()) {
            Point center = GetCenterCoordinates();
            g2d.setColor(new Color(0, 0, 0));
            g2d.fillOval(center.GetX(), center.GetY(), mainStroke / 3,  mainStroke / 3);
        }
    }

    /**
     * Beállítja a cső végeinek koordinátáit.
     * @param p1 az első végének koordinátája.
     * @param p2 a második végének koordinátája.
     */
    public void SetEndPoints(Point p1, Point p2) {
        endPoints[0] = p1;
        endPoints[1] = p2;
    }

    /**
     * Lekérdezi a cső végeinek koordinátáit.
     * @return a koordináták.
     */
    public Point[] GetEndPointsCoordinates() {
        Point[] points = new Point[] {new Point(endPoints[0].GetX(), endPoints[0].GetY()), new Point(endPoints[1].GetX(), endPoints[1].GetY())};
        return points;
    }

    /**
     * Megmondja, hogy egy adott pont a csőhöz tartozik-e.
     * @param p a vizsgált pont
     * @return hozzá tartozik-e.
     */
    @Override
    public boolean ContainsPoint(Point p) {
        int mainStroke = 15;
        double dX = endPoints[1].GetX() - endPoints[0].GetX();
        double dY = endPoints[1].GetY() - endPoints[0].GetY();
        double distance = Math.sqrt(dX * dX + dY * dY);
        dX /= distance;
        dY /= distance;
        double pX = -dY;
        double pY = dX;
        double nX = endPoints[1].GetX() - p.GetX();
        double nY = endPoints[1].GetY() - p.GetY();
        if(nX * dX + nY * dY > distance || nX * dX + nY * dY < 0)
            return false;
        if(Math.abs(nX * pX + nY * pY) > mainStroke)
            return false;
        return true;
    }

    /**
     * Visszaadja a cső középpontjának koordinátáját.
     * @return a középpont.
     */
    @Override
    public Point GetCenterCoordinates() {
        int x = (endPoints[0].GetX() + endPoints[1].GetX()) / 2;
        int y = (endPoints[0].GetY() + endPoints[1].GetY()) / 2;
        return new Point(x, y);
    }

    /**
     * @return Visszatéríti a nézethez tartozó elemet
     */
    @Override
    public Element GetElement() {
        return pipe;
    }
}
