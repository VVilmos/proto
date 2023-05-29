package View;

import java.awt.*;

public abstract class PlayerView {

    private Point place;  //kell-e???

    public abstract  void Update(Graphics g);
    public void SetPlace(Point p){
        this.place = p;
    }

    public Point GetPlace(){
        return  this.place;
    }
}
