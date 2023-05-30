package View;


import java.io.Serializable;

/**
 * A vásznon kirajzolt pontokat reprezentáló osztály
 */
public class Point implements Serializable {

    /**
     *  A pont X koordinátája
     */
    private int x;
    /**
     * A pont Y koordinátája
     */
    private int y;

    /**
     * A Point osztály paraméteres konstruktora
     * @param x az új pont x koordinátájának értéke
     * @param y az új pont y koordinátájának éréke
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter a pont x koordinátájára
     * @return a pont egész típusú x koordinátája
     */
    public int GetX() {return x;}

    /**
     *  Getter a pont y koordinátájára
     * @return a pont egész típusú y koordinátája
     */
    public int GetY() {return y;}
}
