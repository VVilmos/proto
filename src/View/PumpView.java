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
        g2d.fillOval(center.GetX(), center.GetY(), 40, 40);
        if(pump.GetBrokenness()){
            g2d.setColor(new Color(0, 0, 0));
            g2d.fillOval(center.GetX() + 15, center.GetY() + 15, 10, 10);
        }
    }

}
