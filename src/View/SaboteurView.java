package View;

import Control.Main;
import Model.Saboteur;
import Model.Element;
import java.awt.*;
import java.util.Random;

public class SaboteurView extends PlayerView{

    private Saboteur saboteur;

    public SaboteurView(Saboteur s){
        this.saboteur = s;
    }
    @Override
    public void Update(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);

        Element on = saboteur.GetLocation();
        ElementView ev = Main.getInstance().GetElementView(on);
        Point location = ev.GetCenterCoordinates();

        Random random = new Random();
        int randNumX = random.nextInt(6);
        int randNumY = random.nextInt(6);

        int minusPlusX = random.nextInt(2);
        int minusPlusY = random.nextInt(2);

        if(minusPlusX == 0 && minusPlusY == 0){
            g2d.fillRect(location.GetX() - randNumX, location.GetY() - randNumY, 15, 60);
        }
        else if(minusPlusX == 0 && minusPlusY == 1){
            g2d.fillRect(location.GetX() - randNumX, location.GetY() + randNumY, 15, 60);
        }
        else if(minusPlusX == 1 && minusPlusY == 0){
            g2d.fillRect(location.GetX() + randNumX, location.GetY() - randNumY, 15, 60);
        }
        else if(minusPlusX == 1 && minusPlusY == 1) {
            g2d.fillRect(location.GetX() + randNumX, location.GetY() + randNumY, 15, 60);
        }
    }
}
