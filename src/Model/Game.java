package Model;

import Control.GameEnding;

import java.io.*;
import java.util.*;

public class Game {
    /**
     * Hibaüzenet arra az esetre, ha egy keresett objektum nem létezik.
     */
    private static final String unknownObjMsg = "Unknown object! Note that all referred objects are to be added to the running model.";

    /**
     * Hibaüzenet arra az esetre, ha egy parancs argumentumai helytelenek.
     */
    private static final String argErrorMsg = "Invalid argument! Please check the correct syntax of the command in the documentation.";

    /**
     * A játékban lévő csövek, pumpák, ciszternák, források, szerelők és szabotőrök.
     */
    private HashMap<String, Pipe> pipes = new HashMap<>();
    private HashMap<String, Pump> pumps = new HashMap<>();
    private HashMap<String, Cistern> cisterns = new HashMap<>();
    private HashMap<String, Source> sources = new HashMap<>();
    private HashMap<String, Mechanic> mechanics = new HashMap<>();
    private HashMap<String, Saboteur> saboteurs = new HashMap<>();
    private HashMap<String, Node> nodes = new HashMap<String, Node>();
    private HashMap<String, Player> players = new HashMap<String, Player>();

    /**
     * A játékban lévő objektumok nevei.
     */
    private HashMap<Object, String> objectnames = new HashMap<>();

    /**
     * A játék determinisztikusságát határozza meg.
     */
    private static boolean determinism = false;

    /**
     * Növekvő sorszám az elemekhez
     */
    private static int elem_number = 0;

    /**
     * Igaz, ha a játék fut
     */
    private boolean running = false;

    /**
     * A játék időzítője.
     */
    private Timer timer;

    /**
     * Visszaadja a játékban levő csöveket.
     *
     * @return a csövek
     */
    public HashMap<String, Pipe> getPipes() {
        return pipes;
    }

    /**
     * A szabotőrök csapata által megszerzett víz "gyűjtőhelye"
     */
    protected static Pool saboteurPool = new Pool(); //protected??
    /**
     * A szerelők csapata által megszerzett víz "gyűjtőhelye"
     */
    protected static Pool mechanicPool = new Pool();

    /**
     * A Game osztály konstruktora. Inicializálja a Timer singleton osztályt.
     */
    public Game() {
        Timer.setInterval(500);
        timer = Timer.getInstance();
    }

    /**
     * Elindítja a játékot.
     */
    public void StartGame() {
        timer.start();
        running = true;

        Thread thread = new Thread(this::BreakRandomPump);
        thread.setDaemon(true);
        thread.start();

    }

    public synchronized void BreakRandomPump() {
        while (true) {
            Random r = new Random();
            int size = pumps.size();
            int i = r.nextInt(0, size-1);
            int prob = r.nextInt(1, 10);
            if (prob == 4 ) {
                ArrayList<Pump> pumps1 = new ArrayList<>(pumps.values());
                Pump pumpToBreak = pumps1.get(i);
                pumpToBreak.BreakPump();
            }
            try {
                Thread.sleep(2000);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }



    }

    /**
     * Megállítja a játékot és kiírja a nyertes csapat nevét.
     */
    public GameEnding EndGame() {
        timer.pause();
        running = false;
        if (getSaboteurPool().GetWater() > getMechanicPool().GetWater()) {
            saboteurPool.Reset();
            mechanicPool.Reset();
            return GameEnding.SABOTEURS_WIN;
        } else if (getSaboteurPool().GetWater() < getMechanicPool().GetWater()) {
            saboteurPool.Reset();
            mechanicPool.Reset();
            return GameEnding.MECHANICS_WIN;
        } else {
            saboteurPool.Reset();
            mechanicPool.Reset();
            return GameEnding.EQUAL;
        }

    }

    /**
     * Getter, mely visszaadja a szabotőrök "vízgyűjtőjét"
     *
     * @return referencia a szabotőrök vízgyüjtőjére
     */
    public static Pool getSaboteurPool() {
        return saboteurPool;
    }

    /**
     * Getter, mely visszaadja a szerelők "vízgyűjtőjét"
     *
     * @return referencia a szerelők vízgyüjtőjére
     */
    public static Pool getMechanicPool() {
        return mechanicPool;
    }

    /**
     * Generál egy véletlen értéket, amely a cső csúszóssá tételénél használatos.
     *
     * @return véletlen érték
     */
    public static int generateRandomSlipperyTime() {
        if (determinism)
            return 1;
        return generateRandomBetween(1, 5);
    }

    /**
     * Generál egy véletlen értéket, amely a cső ragadóssá tételénél használatos.
     *
     * @return véletlen érték
     */
    public static int generateRandomStickyTime() {
        if (determinism)
            return 1;
        return generateRandomBetween(1, 5);
    }

    /**
     * Generál egy véletlen értéket, amely a cső védetté tételénél használatos.
     *
     * @return véletlen érték
     */
    public static int generateRandomProtectedTime() {
        if (determinism)
            return 1;
        return generateRandomBetween(1, 5);
    }

    /**
     * Generál egy véletlen értéket, ami egy Pipehoz kapcsolódó Nodeot jelent, ide fog továbbmenni a játékos.
     *
     * @param from amelyik indexű Noderól érkezett.
     * @return
     */
    public static int generateNextStep(int from) {
        if (determinism)
            return from == 0 ? 1 : 0;
        return generateRandomBetween(0, 1);
    }

    /**
     * Generál egy random egész számot a megadott intervallumban.
     *
     * @param low  alsó határ
     * @param high felső határ
     * @return véletlen szám
     */
    private static int generateRandomBetween(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    /**
     * Hozzáad egy szerelőt a játékhoz
     *
     * @param mechanic A szerelő referenciája
     * @param name     A szerelő neve
     */
    public void AddMechanic(Mechanic mechanic, String name) {
        mechanics.put(name, mechanic);
        players.put(name, mechanic);
        objectnames.put(mechanic, name);
    }

    /**
     * Hozzáad egy szabotőrt a játékhoz
     *
     * @param saboteur A szerelő referenciája
     * @param name     A szerelő neve
     */
    public void AddSaboteur(Saboteur saboteur, String name) {
        saboteurs.put(name, saboteur);
        players.put(name, saboteur);
        objectnames.put(saboteur, name);
    }

    /**
     * Hozzáad egy csövet a játékhoz
     *
     * @param p A cső reeferenciája
     */
    public void AddPipe(Pipe p) {
        String name = "pipe" + getElemNum();
        pipes.put(name, p);
        objectnames.put(p, name);
        if (timer.isRunning()) {
            timer.pause();
            timer.addISteppable(p);
            timer.start();
        } else timer.addISteppable(p);
    }

    /**
     * Hozzáad egy pumpát a játékhoz
     *
     * @param p A pumpa referenciája
     */
    public void AddPump(Pump p) {
        String name = "pump" + getElemNum();
        pumps.put(name, p);
        nodes.put(name, p);
        objectnames.put(p, name);
        if (timer.isRunning()) {
            timer.pause();
            timer.addISteppable(p);
            timer.start();
        } else timer.addISteppable(p);
    }

    /**
     * Hozzáad egy ciszternát a játékhoz
     *
     * @param c A ciszterna referenciája
     */
    public void AddCistern(Cistern c) {
        String name = "cistern" + getElemNum();
        cisterns.put(name, c);
        nodes.put(name, c);
        objectnames.put(c, name);
        timer.addISteppable(c);
    }

    /**
     * Hozzáad egy forrást a játékhoz
     *
     * @param s A forrás referenciája
     */
    public void AddSource(Source s) {
        String name = "source" + getElemNum();
        sources.put(name, s);
        nodes.put(name, s);
        objectnames.put(s, name);
        timer.addISteppable(s);
    }

    /**
     * Hozzáad egy új objektumot a játékhoz.
     *
     * @param type a hozzáadni kívánt objektum típusa
     * @param name a hozzáadni kívánt objektum neve
     */
    public void Add(String type, String name) {
        if (Objects.equals(type, "Pipe")) {
            Pipe p = new Pipe();
            pipes.put(name, p);
            objectnames.put(p, name);
            timer.addISteppable(p);
        } else if (Objects.equals(type, "Pump")) {
            Pump p = new Pump();
            pumps.put(name, p);
            nodes.put(name, p);
            objectnames.put(p, name);
            timer.addISteppable(p);
        } else if (Objects.equals(type, "Cistern")) {
            Cistern c = new Cistern();
            cisterns.put(name, c);
            nodes.put(name, c);
            objectnames.put(c, name);
            timer.addISteppable(c);
        } else if (Objects.equals(type, "Source")) {
            Source s = new Source();
            sources.put(name, s);
            nodes.put(name, s);
            objectnames.put(s, name);
            timer.addISteppable(s);
        } else if (Objects.equals(type, "Mechanic")) {
            Mechanic m = new Mechanic();
            mechanics.put(name, m);
            players.put(name, m);
            objectnames.put(m, name);
        } else if (Objects.equals(type, "Saboteur")) {
            Saboteur s = new Saboteur();
            saboteurs.put(name, s);
            players.put(name, s);
            objectnames.put(s, name);
        } else {
            System.out.println(argErrorMsg);
        }
    }

    /**
     * A paraméterként megadott nevű objektum (Pump) elörésére alkalmas függvény
     *
     * @param pumpname az eltörni kívánt pumpa neve
     */
    public void Break(String pumpname) {
        if (pumps.containsKey(pumpname)) {
            Pump p = pumps.get(pumpname);
            p.BreakPump();
        } else {
            System.out.println("Unknown object! Note that all referred objects are to be added to the running model.");
        }
    }

    /**
     * Kiüríti az add paranccsal létrehozott csöveknek és pumpáknak a víztartályát, amennyiben azok tartalmaznak vizet.
     *
     * @param name a cső vagy pumpa neve
     */
    public void Drain(String name) {
        if (pipes.containsKey(name)) {
            pipes.get(name).RemoveWater();
        } else if (pumps.containsKey(name)) {
            pumps.get(name).EmptyWaterTank();
        } else {
            System.out.println(unknownObjMsg);
        }
    }

    /**
     * A paraméterként megadott nevű objektum (Pump vagy Pipe) vízzel való feltöltésére alkalmas függvény
     *
     * @param name Annak az objektumnak a neve, amit meg szeretnénk tölteni vízzel
     */
    public void Fill(String name) {
        if (pipes.containsKey(name)) {
            pipes.get(name).AcceptWater();
        } else if (pumps.containsKey(name)) {
            pumps.get(name).FillWaterTank();
        } else {
            System.out.println("Unknown object! Note that all referred objects are to be added to the running model.");
        }
    }

    /**
     * A megadott nevű szerelő kezébe adja annak a már létrehozott csőnek a végét, aminek a nevét megadjuk paraméterként.
     *
     * @param pipename     a cső neve
     * @param mechanicname a szerelő neve
     */
    public void HoldPipe(String pipename, String mechanicname) {
        if (pipes.containsKey(pipename) && mechanics.containsKey(mechanicname)) {
            mechanics.get(mechanicname).HoldPipe(pipes.get(pipename));
        } else {
            System.out.println(unknownObjMsg);
        }
    }

    /**
     * A megadott nevű szerelő kezébe adja a megadott nevű, már létrehozott pumpát.
     *
     * @param pumpname     a pumpa neve
     * @param mechanicname a szerelő neve
     */
    public void HoldPump(String pumpname, String mechanicname) {
        if (pumps.containsKey(pumpname) && mechanics.containsKey(mechanicname)) {
            mechanics.get(mechanicname).HoldPump(pumps.get(pumpname));
        } else {
            System.out.println(unknownObjMsg);
        }
    }

    /**
     * Lépteti a már létrehozott léptethető objektumok közül a megadott nevűt.
     *
     * @param name a léptethető objektum neve
     */
    public void Step(String name) {
        if (pipes.containsKey(name)) {
            pipes.get(name).Step();
        } else if (pumps.containsKey(name)) {
            pumps.get(name).Step();
        } else if (cisterns.containsKey(name)) {
            cisterns.get(name).Step();
        } else if (sources.containsKey(name)) {
            sources.get(name).Step();
        } else {
            System.out.println(unknownObjMsg);
        }
    }

    /**
     * Egy cső lecsatlakoztatását végzi egy karakteren keresztül.
     * Ha a paraméterként megadott karakter vagy cső nem létezik, hibát jelez.
     * Ha a megadott csövet a karakter nem tudja lecsatlakoztatni, akkor a modell nem változik, de nem jelez hibát.
     *
     * @param playername a szerelő vagy szabotőr objektum neve, akivel le akarjuk csatlakoztatni a csövet
     * @param pipename   a lecsatlakoztatni kívánt cső objektum neve
     */
    public void DisconnectPipe(String playername, String pipename) {
        if (mechanics.containsKey(playername) && pipes.containsKey(pipename)) {
            Mechanic m = mechanics.get(playername);
            Pipe pip = pipes.get(pipename);
            var pipeEnds = pip.GetEnds();
            m.DisconnectPipe(pipeEnds.get(0));      //ha nem az on felőli vége, akkor nem csinál semmit
            m.DisconnectPipe(pipeEnds.get(1));
        } else if (saboteurs.containsKey(playername) && pipes.containsKey(pipename)) {
            Saboteur s = saboteurs.get(playername);
            Pipe pip = pipes.get(pipename);
            var pipeEnds = pip.GetEnds();
            s.DisconnectPipe(pipeEnds.get(0));      //ha nem az on felőli vége, akkor nem csinál semmit
            s.DisconnectPipe(pipeEnds.get(1));
        } else {
            System.out.println(unknownObjMsg);
        }

    }

    /**
     * Egy cső lecsatlakoztatását végzi egy karakteren keresztül.
     * Ha a paraméterként megadott karakter vagy cső nem létezik, hibát jelez.
     * Ha a megadott csövet a karakter nem tudja lecsatlakoztatni, akkor a modell nem változik, de nem jelez hibát.
     *
     * @param player a szerelő vagy szabotőr objektum, akivel le akarjuk csatlakoztatni a csövet
     * @param pipe   a lecsatlakoztatni kívánt cső objektum
     */
    public void DisconnectPipe(Player player, Pipe pipe) {
        if (pipe != null) {
            var pipeEnds = pipe.GetEnds();
            player.DisconnectPipe(pipeEnds.get(0));      //ha nem az on felőli vége, akkor nem csinál semmit
            player.DisconnectPipe(pipeEnds.get(1));
        }

    }

    /**
     * A paramáterként kapott csőnévnek megfelelő cső objektumot kilyukasztja. Ha a keresett cső nem létezik, hibát jelez.
     *
     * @param pipename a kilyukasztandó cső neve
     */
    public void Leak(String pipename) {
        if (pipes.containsKey(pipename)) {
            Pipe p = pipes.get(pipename);
            p.Leak();
        } else {
            System.out.println(unknownObjMsg);
        }

    }

    /**
     * Egy új pumpa vagy cső felvételére alkalmas függvény a szerelők esetében (ha ciszternán áll éppen)
     *
     * @param type         a felvenni kívánt objektum típusa (Pump vagy Pipe)
     * @param objectName   a felvenni kívánt objektum leendő neve
     * @param mechanicName a szerelő, aki felveszi az paraméterként megadott típusú objektumot
     */
    public void PickUp(String type, String objectName, String mechanicName) {
        if (mechanics.containsKey(mechanicName)) {
            Mechanic m = mechanics.get(mechanicName);
            if (Objects.equals(type, "--pipe")) {
                m.PickupPipe();
                if (m.GetHoldingPipeEnd() != null) {
                    pipes.put(objectName, m.GetHoldingPipeEnd().GetOwnPipe());
                    objectnames.put(m.GetHoldingPipeEnd().GetOwnPipe(), objectName);
                }
            } else if (Objects.equals(type, "--pump")) {
                m.PickupPump();
                if (m.GetHoldingPumps().size() > 0) {
                    pumps.put(objectName, m.GetHoldingPumps().get(m.GetHoldingPumps().size() - 1));
                    objectnames.put(m.GetHoldingPumps().get(m.GetHoldingPumps().size() - 1), objectName);
                }
            }
        } else {
            System.out.println("Unknown mechanic name! Note that the referred object is to be added to the running model.");
        }
    }

    /**
     * A cső csúszóssá tételére alkalmas függvény, amin a paraméterként megadott nevű játékos áll (szabotőrök esetén)
     *
     * @param saboteurName a szabotőr neve, aki csúszóssá teszi a csövet
     */
    public void SlipperyPipe(String saboteurName) {
        if (saboteurs.containsKey(saboteurName)) {
            Saboteur s = saboteurs.get(saboteurName);
            s.MakeSlipperyPipe();
        } else {
            System.out.println("Unknown object! Note that all referred objects are to be added to the running model.");
        }
    }

    /**
     * A cső ragadóssá tételére alkalmas függvény, amin a paraméterként megadott nevű játékos áll
     *
     * @param playerName a játékos neve, aki ragadóssá teszi a csövet
     */
    public void StickyPipe(String playerName) {
        if (mechanics.containsKey(playerName)) {
            Mechanic m = mechanics.get(playerName);
            m.MakeStickyPipe();
        } else if (saboteurs.containsKey(playerName)) {
            Saboteur s = saboteurs.get(playerName);
            s.MakeStickyPipe();
        } else {
            System.out.println("Unknown object! Note that all referred objects are to be added to the running model.");
        }
    }

    /**
     * Állapotlekérdező parancs, amely kiírja a paraméterként megadott névez tartozó objektum állapotát a modellben.
     * Ha a keresett névhez nem tartozik objektum, a függvény hibát jelez.
     *
     * @param objectname a lekérdezendő modellbeli objektum. Lehet: pumpa, cső, ciszterna, forrás, szerelő, szabotőr
     * @param args       a lekérdezendő tulajdonságok betűnként kódólva (csak a pumpa és cső lekérdezésénél vesszük figyelembe)
     */
    public void State(String objectname, String args) {
        if (objectname.equals("pools")) {
            System.out.println("Mechanics: " + mechanicPool.GetWater());
            System.out.println("Saboteurs: " + saboteurPool.GetWater());
        } else if (mechanics.containsKey(objectname)) {
            Mechanic m = mechanics.get(objectname);
            Element on = m.GetLocation();

            System.out.println("on: " + objectnames.get(on));


            System.out.print("holdingPipe: ");

            //ha nincs a kezében semmi
            if (m.GetHoldingPipeEnd() == null) {
                System.out.println("null");
            } else { //ha van a kezében cső
                Pipe holdpipe = m.GetHoldingPipeEnd().GetOwnPipe();
                System.out.println(objectnames.get(holdpipe));
            }

            System.out.print("holdingPumps: ");
            if (m.GetHoldingPumps().size() == 0) {
                System.out.println("");
            } else {
                var holdingpump = m.GetHoldingPumps();
                for (Pump p : holdingpump) {
                    System.out.print(objectnames.get(p) + " ");
                }
                System.out.println("");
            }
            System.out.println();
        } else if (saboteurs.containsKey(objectname)) {
            Saboteur s = saboteurs.get(objectname);
            Element on = s.GetLocation();

            System.out.println("on: " + objectnames.get(on));

            System.out.print("holdingPipe: ");

            //ha nincs a kezében semmi
            if (s.GetHoldingPipeEnd() == null) {
                System.out.println("null");
                System.out.println();
                return;
            }

            //ha van a kezében cső
            Pipe holdpipe = s.GetHoldingPipeEnd().GetOwnPipe();
            System.out.println(objectnames.get(holdpipe));
            System.out.println();
        } else if (pipes.containsKey(objectname)) {
            Pipe pip = pipes.get(objectname);
            if (args.equals("")) {

                //helybéli karakter(ek)
                var list = pip.GetPlayers();
                System.out.print("Player: ");
                for (Player p : list) System.out.print(objectnames.get(p) + " ");
                System.out.println();

                //cső végei
                var list2 = pip.GetNeighbours();
                System.out.println("End1: " + objectnames.get(list2.get(0)));
                System.out.println("End2: " + objectnames.get(list2.get(1)));

                //állapotok
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
                if (pip.GetProtectedness()) System.out.println("true");
                else System.out.println("false");


            } else {
                if (args.contains("o")) {
                    var list = pip.GetPlayers();
                    System.out.print("Player: ");
                    for (Player p : list) System.out.print(objectnames.get(p) + " ");
                    System.out.println();

                }
                if (args.contains("e")) {
                    var list2 = pip.GetNeighbours();
                    System.out.println("End1: " + objectnames.get(list2.get(0)));
                    System.out.println("End2: " + objectnames.get(list2.get(1)));

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
                    if (pip.GetProtectedness()) System.out.println("true");
                    else System.out.println("false");
                }

            }
            System.out.println();
        } else if (pumps.containsKey(objectname)) {
            Pump pu = pumps.get(objectname);
            if (args.equals("")) {

                //helybéli karakterek
                var list = pu.GetPlayers();
                System.out.print("Players: ");
                for (Player p : list) {
                    System.out.print(objectnames.get(p) + " ");
                }
                System.out.println();

                //csatlakoztatott csövek
                System.out.print("ConnectedPipes: ");
                var neighbours = pu.GetNeighbours();
                for (Element neighbour : neighbours) {
                    System.out.print(objectnames.get(neighbour) + " ");
                }
                System.out.println();

                //Bemeneti cső
                System.out.print("inPipe: ");
                if (pu.GetInPipeNumber() == -1) System.out.println("null");
                else {
                    PipeEnd[] ends = pu.GetPipeEnds();
                    for (int i = 0; i < ends.length; i++) {
                        if (i == pu.GetInPipeNumber()) {
                            Pipe pip = ends[i].GetOwnPipe();
                            System.out.println(objectnames.get(pip));
                            break;
                        }
                    }
                }

                //Kimeneti cső
                System.out.print("outPipe: ");
                if (pu.GetOutPipeNumber() == -1) System.out.println("null");
                else {
                    PipeEnd[] ends = pu.GetPipeEnds();
                    for (int i = 0; i < ends.length; i++) {
                        if (i == pu.GetOutPipeNumber()) {
                            Pipe pip = ends[i].GetOwnPipe();
                            System.out.println(objectnames.get(pip));
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

            } else {
                if (args.contains("o")) {

                    var list = pu.GetPlayers();
                    System.out.print("Players: ");
                    for (Player p : list) {
                        System.out.print(objectnames.get(p) + " ");
                    }
                    System.out.println();
                }
                if (args.contains("p")) {

                    System.out.print("ConnectedPipes: ");
                    PipeEnd[] ends = pu.GetPipeEnds();
                    for (PipeEnd pe : ends) {
                        if (pe != null)
                            System.out.print(objectnames.get(pe.GetOwnPipe()) + " ");
                    }

                    System.out.println();
                }
                if (args.contains("f")) {
                    System.out.print("inPipe: ");
                    if (pu.GetInPipeNumber() == -1) System.out.println("null");
                    else {
                        PipeEnd[] ends = pu.GetPipeEnds();
                        for (int i = 0; i < ends.length; i++) {
                            if (i == pu.GetInPipeNumber()) {
                                Pipe pip = ends[i].GetOwnPipe();
                                System.out.println(objectnames.get(pip));
                                break;
                            }
                        }
                    }
                }
                if (args.contains("t")) {
                    System.out.print("outPipe: ");
                    if (pu.GetOutPipeNumber() == -1) System.out.println("null");
                    else {
                        PipeEnd[] ends = pu.GetPipeEnds();
                        for (int i = 0; i < ends.length; i++) {
                            if (i == pu.GetOutPipeNumber()) {
                                Pipe pip = ends[i].GetOwnPipe();
                                System.out.println(objectnames.get(pip));
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

        } else if (cisterns.containsKey(objectname)) {
            Cistern cis = cisterns.get(objectname);

            //helybéli karakterek
            var list = cis.GetPlayers();
            System.out.print("Players: ");
            for (Player p : list) {
                System.out.print(objectnames.get(p) + " ");
            }
            System.out.println();

            //csatlkaztatott csövek
            System.out.print("ConnectedPipes: ");
            PipeEnd[] ends = cis.GetPipeEnds();
            for (PipeEnd pe : ends) {
                if (pe != null) {
                    System.out.print(objectnames.get(pe.GetOwnPipe()) + " ");
                }
            }

            System.out.println();
            System.out.println();

        } else if (sources.containsKey(objectname)) {
            Source source = sources.get(objectname);

            //helybéli karakterek
            var list = source.GetPlayers();
            System.out.print("Players: ");
            for (Player p : list) {
                System.out.print(objectnames.get(p) + " ");
            }
            System.out.println();

            //csatlkaztatott csövek
            System.out.print("ConnectedPipes: ");
            PipeEnd[] ends = source.GetPipeEnds();
            for (PipeEnd pe : ends) {
                if (pe != null) {
                    System.out.print(objectnames.get(pe.GetOwnPipe()) + " ");
                }
            }

            System.out.println();
            System.out.println();

        } else {
            System.out.println(unknownObjMsg);
        }

    }

    /**
     * Játékost elmozgató parancs. Kikeresi a hashmap-ekből a megfelelő elemet, valamint a megfelelő játékost, ezután a játékost lépteti a megadott elemre.
     *
     * @param playerName  A játékos neve, akit léptetünk.
     * @param elementName Az elem neve, ahova a játékos lép.
     */
    public void Move(String playerName, String elementName) {
        Player player;
        player = mechanics.get(playerName);
        if (player == null)
            player = saboteurs.get(playerName);

        if (player == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        Element element;
        element = pipes.get(elementName);
        if (element == null)
            element = sources.get(elementName);
        if (element == null)
            element = cisterns.get(elementName);
        if (element == null)
            element = pumps.get(elementName);
        if (element == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        player.Move(element);
    }

    /**
     * Csúszóssá teszi a paraméterként megadott csövet.
     *
     * @param pipeName A cső, amit csúszóssá változtat.
     */
    public void Grease(String pipeName) {
        Pipe pipe = pipes.get(pipeName);
        if (pipe == null) {
            System.out.println(unknownObjMsg);
            return;
        }
        pipe.MakeSlippery();
    }

    /**
     * A játékos átállítja azt a pumpát, amin éppen áll
     *
     * @param player  A játékos, aki a pumpán kell, hogy álljon
     * @param inPipe  Az új bemeneti cső
     * @param outPipe Az új kimeneti cső
     */
    public void SwitchPump(Player player, Pipe inPipe, Pipe outPipe) {

        Element element = player.GetLocation();
        if (inPipe == null || outPipe == null) {
            System.out.println(unknownObjMsg);
            return;
        }
        PipeEnd[] pipeEnds = element.GetPipeEnds();
        PipeEnd inputPipeEnd = null;
        PipeEnd outputPipeEnd = null;

        for (PipeEnd pe : pipeEnds)
            if (pe != null && pe.GetOwnPipe() == inPipe)
                inputPipeEnd = pe;


        for (PipeEnd pe : pipeEnds)
            if (pe != null && pe.GetOwnPipe() == outPipe)
                outputPipeEnd = pe;

        element.Switch(inputPipeEnd, outputPipeEnd);
    }

    /**
     * A megadott nevű játékos átállítja azt a pumpát, amin éppen áll.
     *
     * @param playerName A játékos neve, aki a pumpán kell, hogy álljon,
     * @param input      Az új bemeneti cső neve.
     * @param output     Az új kimeneti cső neve.
     */
    public void SwitchPump(String playerName, String input, String output) {
        Player player = mechanics.get(playerName);
        if (player == null)
            player = saboteurs.get(playerName);
        if (player == null) {
            System.out.println(unknownObjMsg);
            return;
        }


        Element element = player.GetLocation();
        Pipe inPipe = pipes.get(input);
        Pipe outPipe = pipes.get(output);
        if (inPipe == null || outPipe == null) {
            System.out.println(unknownObjMsg);
            return;
        }
        PipeEnd[] pipeEnds = element.GetPipeEnds();
        PipeEnd inputPipeEnd = null;
        PipeEnd outputPipeEnd = null;

        if (input != "null") {
            for (PipeEnd pe : pipeEnds)
                if (pe.GetOwnPipe() == inPipe)
                    inputPipeEnd = pe;
        }

        if (output != "null") {
            for (PipeEnd pe : pipeEnds)
                if (pe.GetOwnPipe() == outPipe)
                    outputPipeEnd = pe;
        }
        element.Switch(inputPipeEnd, outputPipeEnd);
    }

    /**
     * A megadott szerelő letesz egy pumpát oda, ahol meg van állva.
     *
     * @param mechanicName A megadott szerelő neve.
     */
    public void PlacePump(String mechanicName) {
        Mechanic mechanic = mechanics.get(mechanicName);
        if (mechanic == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        Pipe newPipe = mechanic.PlacePump();
        if (newPipe != null) {
            objectnames.put(newPipe, "newPipe");
            pipes.put("newPipe", newPipe);
        }
    }

    /**
     * A megadott nevű szerelő megjavítja a megadott nevű csövet vagy pumpát
     *
     * @param elementName  Az elem neve, amit a szerelő megjavít
     * @param mechanicName A szerelő neve, aki javítani fog
     */
    public void Repair(String elementName, String mechanicName) {
        Mechanic mechanic = mechanics.get(mechanicName);
        if (mechanic == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        Pipe pipe = pipes.get(elementName);
        if (pipe != null) {
            if (mechanic.GetLocation() == pipe)
                mechanic.RepairPipe();
        } else {
            Pump pump = pumps.get(elementName);
            if (pump == null) {
                System.out.println(unknownObjMsg);
            } else {
                if (mechanic.GetLocation() == pump)
                    mechanic.RepairPump();
            }
        }
    }

    /**
     * Összeköti a megadott nodeot a megadott cső egyik szabad végével
     *
     * @param nodename1 a node neve.
     * @param pipename  a cső neve.
     */
    public void Connect(String nodename1, String pipename) {
        Node node1 = nodes.get(nodename1);
        if (node1 == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        Pipe pip = pipes.get(pipename);
        if (pip == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        PipeEnd empty = null;
        for (PipeEnd e : pip.GetEnds())
            if (e.GetAttachedNode() == null && empty == null)
                empty = e;

        if (empty != null)
            node1.AddPipe(empty);
    }

    /**
     * Összeköti a két megadott nodeot a megadott csövön keresztül.
     *
     * @param nodename1 a első node neve.
     * @param pipename  a cső neve.
     * @param nodename2 a második node neve.
     */
    public void Connect(String nodename1, String pipename, String nodename2) {
        Node node1 = nodes.get(nodename1);
        if (node1 == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        Pipe pip = pipes.get(pipename);
        if (pip == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        Node node2 = nodes.get(nodename2);
        if (node2 == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        List<PipeEnd> ends = pip.GetEnds();

        boolean flag = true;
        for (PipeEnd e : ends)
            flag &= e.GetAttachedNode() == null;

        if (flag) {
            node1.AddPipe(ends.get(0));
            node2.AddPipe(ends.get(1));
        }
    }

    /**
     * Ragadóssá teszi a megadott csövet.
     *
     * @param pipename a cső neve.
     */
    public void Sticky(String pipename) {
        Pipe pip = pipes.get(pipename);
        if (pip == null) {
            System.out.println(unknownObjMsg);
            return;
        }
        pip.MakeSticky();
    }

    /**
     * Lerakatja a játékossal azt a csövet, amit éppen fog.
     *
     * @param playername a játékos neve.
     */
    public void ConnectPipe(String playername) {
        Player p = players.get(playername);
        if (p == null) {
            System.out.println(unknownObjMsg);
            return;
        }
        p.ConnectPipe();
    }

    /**
     * Kilyukasztatja a játékossal azt a csövet, amin éppen áll.
     *
     * @param playername a játékos neve.
     */
    public void LeakPipe(String playername) {
        Player p = players.get(playername);
        if (p == null) {
            System.out.println(unknownObjMsg);
            return;
        }
        p.BreakPipe();
    }

    /**
     * Átállítja a megadott pumpát.
     *
     * @param pumpname a pumpa neve.
     * @param fromname az új bemeneti cső neve.
     * @param toname   az új kimeneti cső neve.
     */
    public void Switch(String pumpname, String fromname, String toname) {
        Pump pump = pumps.get(pumpname);
        if (pump == null) {
            System.out.println(unknownObjMsg);
            return;
        }

        Pipe from = null;
        Pipe to = null;

        if (!fromname.equals("null")) {
            from = pipes.get(fromname);
            if (from == null) {
                System.out.println(unknownObjMsg);
                return;
            }
        }

        if (!toname.equals("null")) {
            to = pipes.get(toname);
            if (to == null) {
                System.out.println(unknownObjMsg);
                return;
            }
        }

        PipeEnd fromEnd = null;
        if (from != null) {
            for (PipeEnd e : from.GetEnds())
                if (e.GetAttachedNode() == pump)
                    fromEnd = e;
        }

        PipeEnd toEnd = null;
        if (to != null) {
            for (PipeEnd e : to.GetEnds())
                if (e.GetAttachedNode() == pump)
                    toEnd = e;
        }

        if (fromEnd != null || toEnd != null)
            pump.Switch(fromEnd, toEnd);
    }

    /**
     * Elment egy játékot a megadott fájlba.
     *
     * @param path A fájl elérési útja, ahova ment.
     */
    public void Save(String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(pipes);
            objectOutputStream.writeObject(pumps);
            objectOutputStream.writeObject(cisterns);
            objectOutputStream.writeObject(sources);
            objectOutputStream.writeObject(mechanics);
            objectOutputStream.writeObject(saboteurs);
            objectOutputStream.writeObject(nodes);
            objectOutputStream.writeObject(players);
            objectOutputStream.writeObject(objectnames);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elment egy játékot a megadott fájlba.
     *
     * @param objectOutputStream Az {@link ObjectOutputStream}, ahova ment.
     */
    public void Save(ObjectOutputStream objectOutputStream) {
        try {
            objectOutputStream.writeObject(pipes);
            objectOutputStream.writeObject(pumps);
            objectOutputStream.writeObject(cisterns);
            objectOutputStream.writeObject(sources);
            objectOutputStream.writeObject(nodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Visszatölt egy játékot a megadott fájlból.
     *
     * @param path a fájl elérési útja, ahonnan betölti.
     */
    public void Load(String path) {
        try {
            timer.clear();
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            pipes = (HashMap<String, Pipe>) objectInputStream.readObject();
            pumps = (HashMap<String, Pump>) objectInputStream.readObject();
            cisterns = (HashMap<String, Cistern>) objectInputStream.readObject();
            sources = (HashMap<String, Source>) objectInputStream.readObject();
            mechanics = (HashMap<String, Mechanic>) objectInputStream.readObject();
            saboteurs = (HashMap<String, Saboteur>) objectInputStream.readObject();
            nodes = (HashMap<String, Node>) objectInputStream.readObject();
            players = (HashMap<String, Player>) objectInputStream.readObject();
            objectnames = (HashMap<Object, String>) objectInputStream.readObject();

            for (String key : pipes.keySet()) {
                Pipe p = pipes.get(key);
                p.initPool();
                timer.addISteppable(p);
            }

            for (String key : pumps.keySet()) {
                Pump p = pumps.get(key);
                timer.addISteppable(p);
            }

            for (String key : cisterns.keySet()) {
                Cistern c = cisterns.get(key);
                timer.addISteppable(c);
            }

            for (String key : sources.keySet()) {
                Source s = sources.get(key);
                timer.addISteppable(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Visszatölt egy játékot a megadott fájlból.
     *
     * @param objectInputStream az {@link ObjectInputStream}, ahonnan betölti.
     */
    public void Load(ObjectInputStream objectInputStream) {
        try {
            timer.clear();
            pipes = (HashMap<String, Pipe>) objectInputStream.readObject();
            pumps = (HashMap<String, Pump>) objectInputStream.readObject();
            cisterns = (HashMap<String, Cistern>) objectInputStream.readObject();
            sources = (HashMap<String, Source>) objectInputStream.readObject();
            nodes = (HashMap<String, Node>) objectInputStream.readObject();
            objectnames.clear();

            for (String key : pipes.keySet()) {
                Pipe p = pipes.get(key);
                timer.addISteppable(p);
                objectnames.put(p, key);
            }

            for (String key : pumps.keySet()) {
                Pump p = pumps.get(key);
                timer.addISteppable(p);
                objectnames.put(p, key);
            }

            for (String key : cisterns.keySet()) {
                Cistern c = cisterns.get(key);
                timer.addISteppable(c);
                objectnames.put(c, key);
            }

            for (String key : sources.keySet()) {
                Source s = sources.get(key);
                timer.addISteppable(s);
                objectnames.put(s, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Beállítja a játék nem determinisztikusságát,
     * vagy átállítja determinisztikusra.
     *
     * @param state "on" / "off" az új állapot.
     */
    public void Random(String state) {
        if (state.equals("on"))
            determinism = false;
        else if (state.equals("off"))
            determinism = true;
    }

    /**
     * Sorszámot generál az újonnan hozzáadott elemeknek
     *
     * @return Sorszám
     */
    private int getElemNum() {
        return elem_number++;
    }

    /**
     * @return Visszatéríti az első ciszternát
     */
    public Cistern GetFirstCistern() {
        Map.Entry<String, Cistern> entry = cisterns.entrySet().iterator().next();
        Cistern cistern = entry.getValue();
        return cistern;
    }

    /**
     * @return Visszatéríti az első ciszternát
     */
    public Source GetFirstSource() {
        Map.Entry<String, Source> entry = sources.entrySet().iterator().next();
        Source source = entry.getValue();
        return source;
    }

    /**
     * Getter a játék állapotára
     *
     * @return Igazat térít vissza, ha a játék fut
     */
    public boolean isRunning() {
        return running;
    }
}
