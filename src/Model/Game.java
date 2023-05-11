package Model;

import java.util.HashMap;
import java.util.Map;
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
    private HashMap<String, Element> elements = new HashMap<>();

    private HashMap<String, Node> nodes = new HashMap<>();

    private HashMap<String, Player> players = new HashMap<>();

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
    //Paraméterként adott objektum nem létezik: "Unknown object! Note that all referred objects are to be added to the running model."

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

    /**
     * Egy cső lecsatlakoztatását végzi egy karakteren keresztül.
     * Ha a paraméterként megadott karakter vagy cső nem létezik, hibát jelez.
     * Ha a megadott csövet a karakter nem tudja lecsatlakoztatni, akkor a modell nem változik, de nem jelez hibát.
     * @param playername a szerelő vagy szabotőr objektum neve, akivel le akarjuk csatlakoztatni a csövet
     * @param pipename a lecsatlakoztatni kívánt cső objektum neve
     */
    public void DisconnectPipe(String playername, String pipename) {
        if (mechanics.containsKey(playername) && pipes.containsKey(pipename)) {
            Mechanic m = mechanics.get(playername);
            Pipe pip = pipes.get(pipename);
            var pipeEnds = pip.GetEnds();
            m.DisconnectPipe(pipeEnds.get(0));      //ha nem az on felőli vége, akkor nem csinál semmit
            m.DisconnectPipe(pipeEnds.get(1));
        }
        else if (saboteurs.containsKey(playername) && pipes.containsKey(pipename)) {
            Saboteur s = saboteurs.get(playername);
            Pipe pip = pipes.get(pipename);
            var pipeEnds = pip.GetEnds();
            s.DisconnectPipe(pipeEnds.get(0));      //ha nem az on felőli vége, akkor nem csinál semmit
            s.DisconnectPipe(pipeEnds.get(1));
        }
        else {
            System.out.println("Unknown object! Note that all referred objects are to be added to the running model.");
        }

    }

    /**
     * A paramáterként kapott csőnévnek megfelelő cső objektumot kilyukasztja. Ha a keresett cső nem létezik, hibát jelez.
     * @param pipename a kilyukasztandó cső neve
     */
    public void Leak(String pipename) {
        if (pipes.containsKey(pipename)) {
            Pipe p = pipes.get(pipename);
            p.Leak();
        }
        else {
            System.out.println("Unknown object! Note that all referred objects are to be added to the running model.");
        }

    }

    /**
     * Állapotlekérdező parancs, amely kiírja a paraméterként megadott névez tartozó objektum állapotát a modellben.
     * Ha a keresett névhez nem tartozik objektum, a függvény hibát jelez.
     * @param objectname    a lekérdezendő modellbeli objektum. Lehet: pumpa, cső, ciszterna, forrás, szerelő, szabotőr
     * @param args  a lekérdezendő tulajdonságok betűnként kódólva (csak a pumpa és cső lekérdezésénél vesszük figyelembe)
     */
    public void State (String objectname, String args) {
        if (mechanics.containsKey(objectname)) {
            Mechanic m = mechanics.get(objectname);
            Element on = m.GetLocation();                                               //nem létező függvény!!!

            System.out.println("On: ");

            //kulcs keresése az érték alapján --> tartózkodási hely keresése
            for (Map.Entry<String, Element> entry: elements.entrySet()) {
                if (entry.getValue().equals(on)) System.out.println(entry.getKey());
                break;
            }

            System.out.print("holdingPipe: ");

            if (m.holdingPipeEnd == null) {                                             //ha nincs a kezében semmi
                System.out.println("null");
                return;
            }

            //kulcs keresése az érték alapján --> kezében lévő cső keresése
            Pipe holdpipe = m.holdingPipeEnd.GetOwnPipe();                               //elérhetem a holdingPipeEndet?????
            for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                if (entry.getValue().equals(holdpipe)) System.out.println(entry.getKey());
                break;
            }
        }
        else if (saboteurs.containsKey(objectname)) {
            Saboteur s = saboteurs.get(objectname);
            Element on = s.GetLocation();                                               //nem létező függvény!!!

            System.out.println("On: ");

            //kulcs keresése az érték alapján --> tartózkodási hely keresése
            for (Map.Entry<String, Element> entry: elements.entrySet()) {
                if (entry.getValue().equals(on)) System.out.println(entry.getKey());
                break;
            }

            System.out.print("holdingPipe: ");

            if (s.holdingPipeEnd == null) {                                             //ha nincs a kezében semmi
                System.out.println("null");
                return;
            }

            //kulcs keresése az érték alapján --> kezében lévő cső keresése
            Pipe holdpipe = s.holdingPipeEnd.GetOwnPipe();                               //elérhetem a holdingPipeEndet?????
            for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                if (entry.getValue().equals(holdpipe)) System.out.println(entry.getKey());
                break;
            }
        }
        else if (pipes.containsKey(objectname)) {
            Pipe pip = pipes.get(objectname);
            if (args.equals("")) {
                var list = pip.GetPlayers();
                //Megkeressük az értékhez tartozó kulcsot --> helyben lévő karakterek listázása
                System.out.print("Player: ");
                for (Map.Entry<String, Player> entry: players.entrySet()) {
                    for (Player p : list) {
                        if (entry.getValue().equals(p)) System.out.print(entry.getKey() + " ");
                    }
                }
                System.out.println();
                var list2 = pip.GetNeighbours();
                System.out.print("End1: ");
                for (Map.Entry<String, Element> entry: elements.entrySet()) {
                    if (entry.getValue().equals(list2.get(0))) System.out.print(entry.getKey() + "\n");
                }
                System.out.print("End2: ");
                for (Map.Entry<String, Element> entry: elements.entrySet()) {
                    if (entry.getValue().equals(list2.get(1))) System.out.print(entry.getKey() + "\n");
                }
                System.out.print("isBroken: ");
                if (pip.GetBrokennes()) System.out.println("true");
                else System.out.println("false");
                System.out.print("hasWater: ");
                if (pip.GetWaterLevel()) System.out.println("true");
                else System.out.println("false");
                System.out.print("isSticky: ");
                if (pip.GetStickyness()) System.out.println("true");
                else System.out.println("false");
                System.out.print("isSlippery: ");
                if (pip.GetSlipperyness()) System.out.println("true");
                else System.out.println("false");
                System.out.print("isProtected: ");
                if (pip.GetProtectednedd()) System.out.println("true");
                else System.out.println("false");

                System.out.println();

            }
            else {
                if (args.contains("o")) {
                    var list = pip.GetPlayers();

                    //Megkeressük az értékhez tartozó kulcsot --> helyben lévő karakterek listázása
                    System.out.print("Player: ");
                    for (Map.Entry<String, Player> entry: players.entrySet()) {
                        for (Player p : list) {
                            if (entry.getValue().equals(p)) System.out.print(entry.getKey() + " ");
                        }
                    }
                    System.out.println();
                }
                if (args.contains("e")) {
                    var list = pip.GetNeighbours();
                    System.out.print("End1: ");
                    for (Map.Entry<String, Element> entry: elements.entrySet()) {
                        if (entry.getValue().equals(list.get(0))) System.out.print(entry.getKey() + "\n");
                    }
                    System.out.print("End2: ");
                    for (Map.Entry<String, Element> entry: elements.entrySet()) {
                        if (entry.getValue().equals(list.get(1))) System.out.print(entry.getKey() + "\n");
                    }
                }
                if (args.contains("b")) {
                    System.out.print("isBroken: ");
                    if (pip.GetBrokennes()) System.out.println("true");
                    else System.out.println("false");
                }
                if (args.contains("w")) {
                    System.out.print("hasWater: ");
                    if (pip.GetWaterLevel()) System.out.println("true");
                    else System.out.println("false");
                }
                if (args.contains("s")) {
                    System.out.print("isSticky: ");
                    if (pip.GetStickyness()) System.out.println("true");
                    else System.out.println("false");
                }
                if (args.contains("l")) {
                    System.out.print("isSlippery: ");
                    if (pip.GetSlipperyness()) System.out.println("true");
                    else System.out.println("false");
                }
                if (args.contains("p")) {
                    System.out.print("isProtected: ");
                    if (pip.GetProtectednedd()) System.out.println("true");
                    else System.out.println("false");
                }

            }
            System.out.println();

        }
        else if (pumps.containsKey(objectname)) {
            Pump pu = pumps.get(objectname);
            if (args.equals("")) {
                var list = pu.GetPlayers();

                //játékosok
                //Megkeressük az értékhez tartozó kulcsot --> helyben lévő karakterek listázása
                System.out.print("Player: ");
                for (Map.Entry<String, Player> entry: players.entrySet()) {
                    for (Player p : list) {
                        if (entry.getValue().equals(p)) System.out.print(entry.getKey() + " ");
                    }
                }
                System.out.println();

                //csatlakoztatott csövek
                //Értékhez kulcs keresése a hashmapben --> Csatlakoztatott csövek listázása
                System.out.print("\nConnectedPipes: ");
                PipeEnd[] ends = pu.GetPipeEnds();
                for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                    for (PipeEnd pe : ends) {
                        if (entry.getValue().equals(pe.GetOwnPipe())) System.out.print(entry.getKey() + " ");
                    }
                }
                //Bemeneti cső
                System.out.print("inPipe: ");
                if (pu.GetInPipeNumber() == -1) System.out.print("null\n");
                else {
                    for (int i = 0; i < ends.length; i++) {
                        if (i == pu.GetInPipeNumber()) {
                            Pipe pip = ends[i].GetOwnPipe();
                            for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                                if (entry.getValue().equals(pip)) {
                                    System.out.print(entry.getKey() + "\n");
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                //Kimeneti cső
                System.out.print("outPipe: ");
                if (pu.GetOutPipeNumber() == -1) System.out.print("null\n");
                else {
                    for (int i = 0; i < ends.length; i++) {
                        if (i == pu.GetOutPipeNumber()) {
                            Pipe pip = ends[i].GetOwnPipe();
                            for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                                if (entry.getValue().equals(pip)) {
                                    System.out.print(entry.getKey() + "\n");
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                //Működőképesség
                System.out.print("isBroken: ");
                if (pu.GetBrokenness()) System.out.println("true");
                else System.out.println("false");
                //Átmeneti tároló szinjte
                System.out.print("TankFull: ");
                if (pu.GetTankLevel()) System.out.println("true");
                else System.out.println("false");

            }
            else {
                if (args.contains("o")) {
                    var list = pu.GetPlayers();

                    //Megkeressük az értékhez tartozó kulcsot --> helyben lévő karakterek listázása
                    System.out.print("Player: ");
                    for (Map.Entry<String, Player> entry: players.entrySet()) {
                        for (Player p : list) {
                            if (entry.getValue().equals(p)) System.out.print(entry.getKey() + " ");
                        }
                    }
                    System.out.println();
                }
                if (args.contains("p")) {
                    //Értékhez kulcs keresése a hashmapben --> Csatlakoztatott csövek listázása
                    System.out.print("\nConnectedPipes: ");
                    PipeEnd[] ends = pu.GetPipeEnds();
                    for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                        for (PipeEnd pe : ends) {
                            if (entry.getValue().equals(pe.GetOwnPipe())) System.out.print(entry.getKey() + " ");
                        }
                    }
                }
                if (args.contains("f")) {
                    PipeEnd[] ends = pu.GetPipeEnds();
                    System.out.print("inPipe: ");
                    if (pu.GetInPipeNumber() == -1) System.out.print("null\n");
                    else {
                        for (int i = 0; i < ends.length; i++) {
                            if (i == pu.GetInPipeNumber()) {
                                Pipe pip = ends[i].GetOwnPipe();
                                for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                                    if (entry.getValue().equals(pip)) {
                                        System.out.print(entry.getKey() + "\n");
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (args.contains("t")) {
                    PipeEnd[] ends = pu.GetPipeEnds();
                    System.out.print("outPipe: ");
                    if (pu.GetOutPipeNumber() == -1) System.out.print("null\n");
                    else {
                        for (int i = 0; i < ends.length; i++) {
                            if (i == pu.GetOutPipeNumber()) {
                                Pipe pip = ends[i].GetOwnPipe();
                                for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                                    if (entry.getValue().equals(pip)) {
                                        System.out.print(entry.getKey() + "\n");
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if (args.contains("b")) {
                    System.out.print("isBroken: ");
                    if (pu.GetBrokenness()) System.out.println("true");
                    else System.out.println("false");
                }
                if (args.contains("a")) {
                    System.out.print("TankFull: ");
                    if (pu.GetTankLevel()) System.out.println("true");
                    else System.out.println("false");
                }

            }
            System.out.println();

        }
        else if (cisterns.containsKey(objectname)) {
            Cistern cis = cisterns.get(objectname);
            var list = cis.GetPlayers();

            //Megkeressük az értékhez tartozó kulcsot --> helyben lévő karakterek listázása
            System.out.print("Players: ");
            for (Map.Entry<String, Player> entry: players.entrySet()) {
                for (Player p : list) {
                    if (entry.getValue().equals(p)) System.out.print(entry.getKey() + " ");
                }
            }
            //Értékhez kulcs keresése a hashmapben --> Csatlakoztatott csövek listázása
            System.out.print("\nConnectedPipes: ");
            PipeEnd[] ends = cis.GetPipeEnds();
            for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                for (PipeEnd pe : ends) {
                    if (entry.getValue().equals(pe.GetOwnPipe())) System.out.print(entry.getKey() + " ");
                }
            }

        }
        else if (sources.containsKey(objectname)) {
            Source source = sources.get(objectname);
            var list = source.GetPlayers();
            //Megkeressük az értékhez tartozó kulcsot --> helyben lévő karakterek listázása
            System.out.print("Players: ");
            for (Map.Entry<String, Player> entry: players.entrySet()) {
                for (Player p : list) {
                    if (entry.getValue().equals(p)) System.out.print(entry.getKey() + " ");
                }
            }
            //Értékhez kulcs keresése a hashmapben --> Csatlakoztatott csövek listázása
            System.out.print("\nConnectedPipes: ");
            PipeEnd[] ends = source.GetPipeEnds();
            for (Map.Entry<String, Pipe> entry: pipes.entrySet()) {
                for (PipeEnd pe : ends) {
                    if (entry.getValue().equals(pe.GetOwnPipe())) System.out.print(entry.getKey() + " ");
                }
            }
        }
        else {
            System.out.println("Unknown object! Note that all referred objects are to be added to the running model.");
        }

    }

}
