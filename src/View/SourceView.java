package View;

import Model.Source;

import java.awt.*;

public class SourceView extends ElementView{
    private Source source;

    public SourceView(Source source){
        this.source = source;
    }

    public void Update(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(5, 203, 252));
        Point center = GetCenterCoordinates();
        g2d.fillOval(center.GetX(), center.GetY(), 40, 40);
    }
}
