package Model;

import java.util.HashMap;

public class Game {
    /**
     * A játékban lévő csövek, pumpák, ciszternák, források, szerelők és szabotőrök.
     */
    private HashMap<String, Pipe> pipes = new HashMap<>();
    private HashMap<String, Pump> pumps = new HashMap<>();
    private HashMap<String, Cistern> cisterns = new HashMap<>();
    private HashMap<String, Source> sources = new HashMap<>();
    private HashMap<String, Mechanic> mechanics = new HashMap<>();
    private HashMap<String, Saboteur> saboteurs = new HashMap<>();

    /**
     * A játék determinisztikusságát határozza meg.
     */
    private boolean determinism;

    /**
     * A játék időzítője.
     */
    private Timer timer;

    public HashMap<String, Pipe> getPipes(){
        return pipes;
    }

    /**
     * A szabotőrök csapata által megszerzett víz "gyüjtőhelye"
     */
    protected static Pool saboteurPool = new Pool(); //protected??
    /**
     * A szerelők csapata által megszerzett víz "gyüjtőhelye"
     */
    protected static Pool mechanicPool = new Pool();

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
}
