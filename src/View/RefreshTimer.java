package View;

import javax.swing.*;

/**
 * Ennek az osztálynak a felelőssége a grafikus felület periódikus frissítésének a biztosítása.
 * Singleton osztály.
 */
public class RefreshTimer extends Thread {

    /**
     * 100 ms-enként frissítjük a megjelenítő felületet.
     */
    private static final int refreshPeriod = 100;

    /**
     * A szál leállításához szükséges jelzés
     */
    private boolean stopSignal = false;
    /**
     * Az osztály egyetlen példányát tároló attribútum.
     */
    private static RefreshTimer instance = null;

    /**
     * A felületi elem, amit periodikusan újrarajzol.
     */
    private JComponent component;

    /**
     * A konstruktor privát, mivel ez egy singleton osztály, nem lehet kívülről példányosítani.
     */
    private RefreshTimer() {
    }

    /**
     * Az osztály egyetlen példányának a gettere.
     *
     * @return Visszatéríti a {@link RefreshTimer} egyetlen példányát.
     */
    public static RefreshTimer getInstance() {
        if (instance == null) {
            instance = new RefreshTimer();
        }
        return instance;
    }

    /**
     * Ez a függvény beállítja azt a felületelemet, amit periodikusan újrarajzol az időzítő.
     * @param component A felületelem egy {@link JComponent}
     */
    public void setComponent(JComponent component) {
        this.component = component;
    }

    /**
     * Az osztály szálfüggvénye. Ebben valósul meg a periodikus frissítés.
     */
    @Override
    public void run() {
        while(!stopSignal){

            try {
                Thread.sleep(refreshPeriod);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Leállítja az időzítőt futtató szálat.
     */
    public void shutdown(){
        stopSignal = true;
    }

    /**
     * Az időzítő periódikusan meghívódó függénye. Itt végezzük a frissítés műveletét.
     */
    public void Tick(){
        component.revalidate();
    }
}
