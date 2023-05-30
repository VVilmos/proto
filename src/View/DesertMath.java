package View;

/**
 * Statikus segédosztály, amely a játék kirajzolásában használt matematikai műveleteket végzi
 */
public class DesertMath {

    /**
     * Két pont közötti távolság kiszámítása
     * @param p1 a rajzolófelület egy pontja
     * @param p2 a rajzolófelület egy pontja
     * @return a két pont közötti távolság egész értékben
     */
    public static int Distance(Point p1, Point p2) {
        var result = Math.sqrt(Math.pow(p1.GetX() - p2.GetX(), 2) + Math.pow(p1.GetY() - p2.GetY(), 2));
        return (int)result;
    }
}
