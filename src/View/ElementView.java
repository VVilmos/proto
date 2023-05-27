package View;


/**
 * A játékban szereplő mezők megjelenítéséért felelős (ős)osztály
 */
public abstract class ElementView {

    /**
     * Az adott objektumhoz tartozó grafika (kör vagy szakasz) középpontja
     */
    private Point center;

    /**
     * Lekérdező függvény a kirajzolt objektum grafikájának középpontjára
     * @return egy kör vagy egy szakasz középpontja (a megjelenített mezőtől függően)
     */
    public Point GetCenterCoordinates() {return center;}

    /**
     * Setter függvény, amely belállítja a megjelenített mező középponját
     * @param c a kirajzolt mező új középponja
     */
    public void SetCenter(Point c) {center = c;}

    /**
     * A megjelenített grafika frissítése
     * A kirajzolt mező állapotának lekérdezése, majd az eredménytől függően a megfelelő grafika kirajzolása
     * Absztrakt függvény, azaz minden olyan leszármazottnak meg kell valósítania, amely tényleges megjelenítést végez
     */
    public abstract void Update();

    private int radius = 3;

    /**
     * A paraméterként kapott pont a megjelenített mezőhöz tartozik-e
     * a megvalósításban feltételeztük, hogy a mező kör alakú, -
     * ezért a nem kör alakú mezőket kirajzoló leszármazottak ezt a függvényt felüldefiniálják
     * @param p a vizsgált pont
     * @return a vizsgált pont a kirajzolt mező területén belül helyezkedik-e el
     */
    public boolean ContainsPoint(Point p) {
            return DesertMath.Distance(center, p) <= radius;
    }





}
