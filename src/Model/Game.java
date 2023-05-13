package Model;

import java.util.Random;

public class Game {
    /**
     * A szabotőrök csapata által megszerzett víz "gyüjtőhelye"
     */
    private static Pool saboteurPool = new Pool();
    /**
     * A szerelők csapata által megszerzett víz "gyüjtőhelye"
     */
    private static Pool mechanicPool = new Pool();

    /**
     * Getter, mely visszaadja a szabotőrök "vízgyűjtőjét"
     * @return referencia a szabotőrök vízgyüjtőjére
     */
    public static Pool getSaboteurPool() { return saboteurPool; }

    /**
     * Getter, mely visszaadja a szerelők "vízgyűjtőjét"
     * @return referencia a szerelők vízgyüjtőjére
     */
    public static Pool getMechanicPool() {
        return mechanicPool;
    }

    /**
     * Generál egy véletlen értéket, amely a cső csúszóssá tételénél használatos.
     * @return véletlen érték
     */
    public static int generateRandomSlipperyTime() {
        return generateRandomBetween(1, 5);
    }

    /**
     * Generál egy véletlen értéket, amely a cső ragadóssá tételénél használatos.
     * @return véletlen érték
     */
    public static int generateRandomStickyTime() {
        return generateRandomBetween(1, 5);
    }

    /**
     * Generál egy véletlen értéket, amely a cső védetté tételénél használatos.
     * @return véletlen érték
     */
    public static int generateRandomProtectedTime() {
        return generateRandomBetween(1, 5);
    }

    /**
     * Generál egy random egész számot a megadott intervallumban.
     * @param low alsó határ
     * @param high felső határ
     * @return véletlen szám
     */
    public static int generateRandomBetween(int low, int high) {
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }
}
