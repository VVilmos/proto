package View;

import Model.Cistern;

import java.awt.*;

public class CisternView extends ElementView{
    private Cistern cistern;

    public CisternView(Cistern cistern){
        this.cistern = cistern;
    }

    public void Update(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0));
        Point center = GetCenterCoordinates();
        g2d.fillOval(center.GetX(), center.GetY(), 40, 40);
    }
}
