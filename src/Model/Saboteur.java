package Model;

import java.io.Serializable;

/**
 * Szabotőr játékos
 * A játékos képességein felül képes csövet lyukasztani.
 */
public class Saboteur extends Player implements Serializable {
    /**
     * Csúszóssá teszi a csövet, amin éppen áll.
     */
    public void MakeSlipperyPipe() {
        on.MakeSlippery();
    }
}