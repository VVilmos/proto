package Control;

import Model.*;
import Model.Timer;
import View.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ez az alkalmazás fő osztálya, ami egyben a Controller szerepét is ellátja.
 * Singleton osztály
 */
public class Main {

    /**
     * A nézet periodikus frissítését végző időzítő
     */
    private RefreshTimer refreshTimer;

    /**
     * A jelenlegi művelet típusa
     */
    private Operation currentOperation = Operation.IDLE;

    /**
     * A jelenleg kiválasztott járékos referenciát tárolja
     */
    private Player activePlayer;

    /**
     * A játékban résztvevő játékosok listája
     */
    private ArrayList<Player> players;

    /**
     * A jelenleg játékban levő elemeket tároló lista
     */
    private ArrayList<Element> elements;

    /**
     * Azokat az elemeket tárolja, amikre nemrég rákattintottak
     */
    private ArrayList<Element> clickedElements;


    /**
     * {@link ElementView} - {@link Element} összerendeléseket tartalmazó hashmap.
     * Ebből keressük ki a kattintott nézetekhez tartalmazó modellelemeket
     */
    private HashMap<ElementView, Element> viewsElements;

    /**
     * A Main singleton osztálynak egyetlen példánya.
     */
    private static Main instance = null;

    /**
     * A játékhoz tartozó modellelem
     */
    private Game game;

    /**
     * A játékhoz tartozó ablak
     */
    private static GameWindow window;

    /**
     * Az osztály egyetlen példányának a gettere.
     *
     * @return Visszatéríti a {@link Main} egyetlen példányát.
     */
    public static Main getInstance() {
        if (instance == null)
            instance = new Main();
        return instance;
    }

    /**
     * A Main osztály konstruktora. \n
     * Itt inicializálódik a {@link RefreshTimer}, valamint a modellelemekt tároló listák is.
     */
    private Main() {
        refreshTimer = RefreshTimer.getInstance();
        players = new ArrayList<>();
        elements = new ArrayList<>();
        clickedElements = new ArrayList<>();
        viewsElements = new HashMap<>();
        game = new Game();
    }


    /**
     * Ez a függvény kezeli a játék elemeinek a kattintását. Kikeresi a kattintott view-hoz tartozó modellelemet,
     * majd beleteszi a {@link Main#clickedElements} listába.
     *
     * @param view Az az {@link ElementView}, amire rákattintott a felhasználó.
     */
    public void Clicked(ElementView view) {
        Element clickedElement = viewsElements.get(view);
        System.out.println("Azonosítottam");
        clickedElements.add(clickedElement);
        RunOperation();
    }

    /**
     * Megkeresi egy adott modellelemhez tartozó nézetet.
     *
     * @param element A modellelem, amihez a megfelelő nézetet keressük.
     * @return Visszatéríti a keresett nézetet.
     */
    public ElementView GetElementView(Element element) {
        for (Map.Entry<ElementView, Element> entry : viewsElements.entrySet()) {
            if (entry.getValue() == element)
                return entry.getKey();
        }
        return null;
    }

    /**
     * Ez a metódus állítja be a jelenlegi műveletet, amit a játékban végzünk.
     *
     * @param op
     */
    public void SetOperation(Operation op) {
        currentOperation = op;
        RunOperation();
    }

    /**
     * Ez a metódus futtat egy műveletet.
     * Kattintás vagy {@link Main#SetOperation(Operation)} hatására hívódik.
     */
    public void RunOperation() {
        if (activePlayer == null) return;
        switch (currentOperation) {
            case IDLE:
                clickedElements.clear();
                return;
            case SWITCHPUMP:
                if (clickedElements.size() >= 2) {
                    Pipe input = (Pipe) clickedElements.get(0);
                    Pipe output = (Pipe) clickedElements.get(1);
                    game.SwitchPump(activePlayer, input, output);
                    clickedElements.clear();
                    currentOperation = Operation.IDLE;
                }
                break;
            case DISCONNECTPIPE:
                if (clickedElements.size() >= 1) {
                    game.DisconnectPipe(activePlayer, (Pipe) clickedElements.get(0));

                    Pipe pipe = (Pipe) clickedElements.get(0);
                    window.getCanvas().Remove(GetElementView(pipe));
                    viewsElements.remove(GetElementView(pipe));
                    clickedElements.clear();
                    currentOperation = Operation.IDLE;
                }
                break;
            case BREAKPIPE:
                activePlayer.BreakPipe();
                currentOperation = Operation.IDLE;
                break;
            case REPAIRPUMP:
                activePlayer.RepairPump();
                currentOperation = Operation.IDLE;
                break;
            case REPAIRPIPE:
                activePlayer.RepairPipe();
                currentOperation = Operation.IDLE;
                break;
            case STICKYPIPE:
                activePlayer.MakeStickyPipe();
                currentOperation = Operation.IDLE;
                break;
            case SLIPPERYPIPE:
                activePlayer.MakeSlipperyPipe();
                currentOperation = Operation.IDLE;
                break;
            case PICKUPPUMP:
                activePlayer.PickupPump();
                currentOperation = Operation.IDLE;
                break;
            case PICKUPPIPE:
                activePlayer.PickupPipe();
                currentOperation = Operation.IDLE;
                break;
            case CONNECTPIPE:
                Pipe pipe = activePlayer.GetHoldingPipeEnd().GetOwnPipe();
                activePlayer.ConnectPipe();
                List<Element> neighbours = pipe.GetNeighbours();
                if(neighbours.size() == 2){
                    ElementView view1 = GetElementView(neighbours.get(0));
                    ElementView view2 = GetElementView(neighbours.get(1));
                    Point center1 = view1.GetCenterCoordinates();
                    Point center2 = view2.GetCenterCoordinates();
                    if(activePlayer.GetHoldingPipeEnd() == null){
                        PipeView pv = new PipeView(pipe);
                        pv.SetEndPoints(center1, center2);
                        window.getCanvas().PushElementView_Front(pv);
                        viewsElements.put(pv, pipe);
                    }
                }
                currentOperation = Operation.IDLE;
                break;
            case PLACEPUMP:
                if (activePlayer.GetHoldingPumps() == null || activePlayer.GetHoldingPumps().size() == 0) return;
                Element on = activePlayer.GetLocation();
                Pump p = activePlayer.GetHoldingPumps().get(0);
                Pipe newPipe = activePlayer.PlacePump();
                if (newPipe != null) {
                    ElementView onView = GetElementView(on);
                    Point newPumpCenter = onView.GetCenterCoordinates();
                    Point[] ends = onView.GetEndPointsCoordinates();
                    onView.SetEndPoints(ends[0], newPumpCenter);
                    addPipe(newPipe, newPumpCenter, ends[1]);
                    addPump(p, newPumpCenter);
                }
                currentOperation = Operation.IDLE;
                break;
            case MOVE:
                if (clickedElements.size() >= 1) {
                    activePlayer.Move(clickedElements.get(0));
                    //currentOperation = Operation.IDLE;        move az beragado billentyu
                    clickedElements.clear();
                }
                break;
        }
    }

    /**
     * Ez a metódus hozzáad egy szerelőt a játékhoz
     */
    public void addMechanic(String name) {
        Mechanic mechanic = new Mechanic();
        game.AddMechanic(mechanic, name);
        players.add(mechanic);
        MechanicView mv = new MechanicView(mechanic);
        Cistern cistern = game.GetFirstCistern();
        mechanic.Move(cistern);
        window.getCanvas().AddPlayerView(mv);
    }

    /**
     * Ez a metódus hozzáad egy szabotőrt a játékhoz
     */
    public void addSaboteur(String name) {
        Saboteur saboteur = new Saboteur();
        game.AddSaboteur(saboteur, name);
        players.add(saboteur);
        SaboteurView sv = new SaboteurView(saboteur);
        Source source = game.GetFirstSource();
        saboteur.Move(source);
        window.getCanvas().AddPlayerView(sv);
    }

    /**
     * Hozzáad egy csövet a játékhoz
     *
     * @param p A cső, amit a játékhoz hozzáad.
     */
    public void addPipe(Pipe p, Point end1, Point end2) {
        elements.add(p);
        game.AddPipe(p);
        PipeView pv = new PipeView(p);
        pv.SetEndPoints(end1, end2);
        window.getCanvas().PushElementView_Front(pv);
        viewsElements.put(pv, p);
    }

    /**
     * Hozzáad egy pumpát a játékhoz
     *
     * @param p      A cső, amit a játékhoz hozzáad.
     * @param center A pumpa középpontja
     */
    public void addPump(Pump p, Point center) {
        elements.add(p);
        game.AddPump(p);
        PumpView pv = new PumpView(p);
        pv.SetCenter(center);
        window.getCanvas().PushElementView_Back(pv);
        viewsElements.put(pv, p);
    }

    /**
     * Hozzáad egy ciszternát a játékhoz
     *
     * @param c      A cső, amit a játékhoz hozzáad.
     * @param center Az elem középpontja
     */
    public void addCistern(Cistern c, Point center) {
        elements.add(c);
        game.AddCistern(c);
        CisternView cv = new CisternView(c);
        cv.SetCenter(center);
        window.getCanvas().PushElementView_Back(cv);
        viewsElements.put(cv, c);
    }

    /**
     * Hozzáad egy csövet a játékhoz
     *
     * @param s      A cső, amit a játékhoz hozzáad.
     * @param center Az elem középpontja
     */
    public void addSource(Source s, Point center) {
        elements.add(s);
        game.AddSource(s);
        SourceView sv = new SourceView(s);
        sv.SetCenter(center);
        window.getCanvas().PushElementView_Back(sv);
        viewsElements.put(sv, s);
    }

    /**
     * Ez a metódus beállítj, hogy melyik játékos legyen aktív.
     *
     * @param index Az adott játékos sorszáma.
     */
    public void setActivePlayer(int index) {
        activePlayer = players.get(index);
    }

    /**
     * Getter a játékmodellre.
     *
     * @return Visszatéríti a játékmenethez tartozó modellelemet.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Az alkalmazás main függvénye. Ez fut le az alkalmazás indításakor.
     *
     * @param args A program argumentumai
     */
    public static void main(String[] args) {
        window = new GameWindow("Sivatagi vízhálózat");
        window.setSize(1024, 768);
        Cistern c1 = new Cistern();
        Cistern c2 = new Cistern();
        Cistern c3 = new Cistern();
        Cistern c4 = new Cistern();


        Pipe pipe1 = new Pipe(c1);
        Pipe pipe2 = new Pipe(c2);
        Pipe pipe3 = new Pipe(c3);
        Pipe pipe4 = new Pipe(c4);


        Pump p1 = new Pump();
        Pump p2 = new Pump();
        Pump p3 = new Pump();
        Pump p4 = new Pump();

        p1.AddPipe(pipe1.GetEnds().get(1));
        p2.AddPipe(pipe2.GetEnds().get(1));
        p3.AddPipe(pipe3.GetEnds().get(1));
        p4.AddPipe(pipe4.GetEnds().get(1));

        Pipe pipe5 = new Pipe(p1);
        Pipe pipe6 = new Pipe(p2);
        Pipe pipe7 = new Pipe(p3);
        Pipe pipe8 = new Pipe(p4);

        Source source1 = new Source();
        Source source2 = new Source();
        Source source3 = new Source();
        Source source4 = new Source();

        source1.AddPipe(pipe5.GetEnds().get(1));
        source2.AddPipe(pipe6.GetEnds().get(1));
        source3.AddPipe(pipe7.GetEnds().get(1));
        source4.AddPipe(pipe8.GetEnds().get(1));

        Pipe cp1 = new Pipe(p1);
        p2.AddPipe(cp1.GetEnds().get(1));
        Pipe cp2 = new Pipe(p2);
        p3.AddPipe(cp2.GetEnds().get(1));
        Pipe cp3 = new Pipe(p3);
        p4.AddPipe(cp3.GetEnds().get(1));

        Main.getInstance().addPipe(cp1, new Point(570/2, 30), new Point(570/2, 230));
        Main.getInstance().addPipe(cp2, new Point(570/2, 230), new Point(570/2, 430));
        Main.getInstance().addPipe(cp3, new Point(570/2, 430), new Point(570/2, 620));



        Main.getInstance().addCistern(c1, new Point(30, 30));
        Main.getInstance().addCistern(c2, new Point(30, 230));
        Main.getInstance().addCistern(c3, new Point(30, 430));
        Main.getInstance().addCistern(c4, new Point(30, 620));



        Main.getInstance().addPump(p1, new Point(570/2, 30));
        Main.getInstance().addPump(p2, new Point(570/2, 230));
        Main.getInstance().addPump(p3, new Point(570/2, 430));
        Main.getInstance().addPump(p4, new Point(570/2, 620));

        Main.getInstance().addPipe(pipe1, new Point(30, 30), new Point(570/2, 30));
        Main.getInstance().addPipe(pipe2, new Point(30, 230), new Point(570/2, 230));
        Main.getInstance().addPipe(pipe3, new Point(30, 430), new Point(570/2, 430));
        Main.getInstance().addPipe(pipe4, new Point(30, 620), new Point(570/2, 620));
        Main.getInstance().addPipe(pipe5, new Point(570/2, 30), new Point(600, 30));
        Main.getInstance().addPipe(pipe6, new Point(570/2, 230), new Point(600, 230));
        Main.getInstance().addPipe(pipe7, new Point(570/2, 430), new Point(600, 430));
        Main.getInstance().addPipe(pipe8, new Point(570/2, 620), new Point(600, 620));

        Main.getInstance().addSource(source1, new Point(600, 30));
        Main.getInstance().addSource(source2, new Point(600, 230));
        Main.getInstance().addSource(source3, new Point(600, 430));
        Main.getInstance().addSource(source4, new Point(600, 620));





        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}