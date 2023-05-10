package Model;

/**
 * Szabotőr játékos
 * A játékos képességein felül képes csövet lyukasztani.
 */
public class Saboteur extends Player{
    /**
     * Konstruktor
     */
    public Saboteur() {

    }

    /**
     * Kilyukasztja a csövet, amin éppen áll.
     */
    public void BreakPipe(){
        on.Leak();
    }
}
