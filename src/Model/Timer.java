package Model;

import java.util.ArrayList;

/**
 * Timer osztály
 * Tartalmazza az időben léptethető elemeket (amik megvalósítják az {@link ISteppable} interfészt). \n
 * Ezeket periodikus időben lépteti. \n
 * Mivel az időzítő külön szálon kell, hogy fusson, a {@link Thread} osztályból származik le. \n
 * Singleton osztály
 */
public class Timer extends Thread {
    /**
     * Az időzítő szünetelésjelzője. Ha igazra állítódik, a szál elkezd várakozni.
     */
    private boolean pauseSignal = false;
    /**
     * Az időzítő leállításjelzője. Ha igazra állítódik, a szálfüggvény véget ér.
     */
    private boolean stopSignal = false;
    /**
     * Az időzítő periódusa
     */
    private static int interval;
    /**
     * A léptethető elemeket ({@link ISteppable}) tartalmazó lista.
     */
    private ArrayList<ISteppable> iSteppables;
    /**
     * Az osztály singleton példánya
     */
    private static Timer instance = null;

    /**
     * Az osztály konstruktora. Inicialilzálja a léptethető objektumok listáját.
     * A konstruktor privát, hogy csak az osztály saját maga tudja meghívni, mivel singleton.
     */
    private Timer() {
        iSteppables = new ArrayList<ISteppable>();
    }

    /**
     * Az osztályt létrehozó függvény.
     *
     * @return Visszatéríti az osztály az egyetlen példányát.
     */
    public static Timer getInstance() {
        if (instance == null) {
            instance = new Timer();
        }
        return instance;
    }

    /**
     * Hozzáadja a léptethető dolgok listájához a paraméterként megkapott objektumot.
     *
     * @param iSteppable Léptethető objektum.
     */
    public synchronized void addISteppable(ISteppable iSteppable) {
        iSteppables.add(iSteppable);
    }

    /**
     * Eltávolítja a léptethető dolgok listájából a paraméterként megkapott objektumot.
     *
     * @param iSteppable Léptethető objektum.
     */
    public synchronized void removeISteppable(ISteppable iSteppable) {
        iSteppables.remove(iSteppable);
    }

    /**
     * Kiüríti a léptethető elemek listáját
     */
    public synchronized void clear() {
        iSteppables.clear();
    }

    /**
     * Lépteti az eltárolt elemeket úgy, hogy mindegyiknek meghívja a step metódusát.
     */
    public synchronized void tick() {
        for (ISteppable steppable : iSteppables) {
            steppable.Step();
        }
    }

    /**
     * Az időzítő periódusát beállító statikus függvény.
     *
     * @param time Az időzítő új periódusideje.
     */
    public static void setInterval(int time) {
        interval = time;
    }

    /**
     * Az időzítő szálfüggvénye.
     * Egy végtelen ciklusban periodikusan hívogatja a {@link Timer#tick()} metódust.
     */
    @Override
    public synchronized void run() {
        try {
            while (!stopSignal) {
                tick();
                sleep(interval);
                if (pauseSignal) {
                    wait();
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Az órajelet leállító függvény. Meghívása esetén leáll a szálfüggvény és következésképpen a periodikus működés.
     */
    public void pause() {
        pauseSignal = true;
    }

    /**
     * Az időzítő indító függvénye. Ha a szál még nem volt elindítva, elindítja, ha meg van állítva, felébreszti.
     */
    @Override
    public synchronized void start() {
        if (pauseSignal) {
            notify();
            pauseSignal = false;
        } else
            super.start();
    }

    /**
     * Leállítja az időzítőt végérvényesen. \n
     * Az alkalmazás bezárásakor kell meghívni
     */
    public void terminate() {
        stopSignal = true;
        if (pauseSignal) {
            pauseSignal = false;
            interrupt();
        }
    }
}
