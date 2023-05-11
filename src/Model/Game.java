package Model;

import java.util.HashMap;
import java.util.Objects;

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

    //ide jöhetnek a parancsok

    public void Add(String type, String name){
        if(Objects.equals(type, "Pipe")){
            Pipe p = new Pipe(null);
            pipes.put(name, p);
        }
        if(Objects.equals(type, "Pump")){
            Pump p = new Pump();
            pumps.put(name, p);
        }
        if(Objects.equals(type, "Cistern")){
            Cistern c = new Cistern();
            cisterns.put(name, c);
        }
        if(Objects.equals(type, "Source")){
            Source s = new Source();
            sources.put(name, s);
        }
        if(Objects.equals(type, "Mechanic")){
            Mechanic m = new Mechanic();
            mechanics.put(name, m);
        }
        if(Objects.equals(type, "Saboteur")){
            Saboteur s = new Saboteur();
            saboteurs.put(name, s);
        }
    }

    public void Drain(String name){
        if(pipes.containsKey(name)){
            pipes.get(name).RemoveWater();
        }
        else if(pumps.containsKey(name)){
            pumps.get(name).EmptyWaterTank();
        }
    }

    public void HoldPipe(String pipename, String mechanicname){
        if(pipes.containsKey(pipename) && mechanics.containsKey(mechanicname)){
            mechanics.get(mechanicname).HoldPipe(pipes.get(pipename));
        }
    }

    public void HoldPump(String pumpname, String mechanicname){
        if(pumps.containsKey(pumpname) && mechanics.containsKey(mechanicname)){
            mechanics.get(mechanicname).HoldPump(pumps.get(pumpname));
        }
    }

    public void Step(String name){
        if(pipes.containsKey(name)){
            pipes.get(name).Step();
        }
        else if(pumps.containsKey(name)){
            pumps.get(name).Step();
        }
        else if(cisterns.containsKey(name)){
            cisterns.get(name).Step();
        }
        else if(sources.containsKey(name)){
            sources.get(name).Step();
        }
    }

}
