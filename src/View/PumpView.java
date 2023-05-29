package View;

import Model.Pump;

import java.awt.*;

public class PumpView extends ElementView{
    private Pump pump;

    public PumpView(Pump pump){
        this.pump = pump;
    }

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

}
