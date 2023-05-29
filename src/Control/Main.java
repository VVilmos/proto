package Control;

import Model.*;
import Model.Timer;
import View.ElementView;
import View.RefreshTimer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
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
        RunOperation();
        currentOperation = op;
    }

    /**
     * Ez a metódus futtat egy műveletet.
     * Kattintás vagy {@link Main#SetOperation(Operation)} hatására hívódik.
     */
    public void RunOperation() {
        switch (currentOperation) {
            case IDLE:
                return;
            case SWITCHPUMP:
                if (clickedElements.size() == 2) {
                    Pipe input = (Pipe) clickedElements.get(0);
                    Pipe output = (Pipe) clickedElements.get(1);
                    game.SwitchPump(activePlayer, input, output);
                    clickedElements.remove(0);
                    clickedElements.remove(0);
                    currentOperation = Operation.IDLE;
                }
                break;
            case DISCONNECTPIPE:
                if (clickedElements.size() == 1) {
                    game.DisconnectPipe(activePlayer, (Pipe) clickedElements.get(0));
                    clickedElements.remove(0);
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
                activePlayer.ConnectPipe();
                currentOperation = Operation.IDLE;
                break;
            case PLACEPUMP:
                Pipe newPipe = activePlayer.PlacePump();
                if(newPipe != null)
                    addPipe(newPipe);
                currentOperation = Operation.IDLE;
                break;
            case MOVE:
                if(clickedElements.size() == 1) {
                    activePlayer.Move(clickedElements.get(0));
                    currentOperation = Operation.IDLE;
                    clickedElements.remove(0);
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
        //TODO: View
    }

    /**
     * Ez a metódus hozzáad egy szabotőrt a játékhoz
     */
    public void addSaboteur(String name) {
        Saboteur saboteur = new Saboteur();
        game.AddSaboteur(saboteur, name);
        players.add(saboteur);
        //TODO:View
    }

    /**
     * Hozzáad egy csövet a játékhoz
     * @param p A cső, amit a játékhoz hozzáad.
     */
    public void addPipe(Pipe p){
        elements.add(p);
        game.AddPipe(p);
        //TODO: View
    }
    /**
     * Hozzáad egy pumpát a játékhoz
     * @param p A cső, amit a játékhoz hozzáad.
     */
    public void addPump(Pump p){
        elements.add(p);
        game.AddPump(p);
        //TODO: View
    }

    /**
     * Hozzáad egy ciszternát a játékhoz
     * @param c A cső, amit a játékhoz hozzáad.
     */
    public void addCistern(Cistern c){
        elements.add(c);
        game.AddCistern(c);
        //TODO: View
    }

    /**
     * Hozzáad egy csövet a játékhoz
     * @param s A cső, amit a játékhoz hozzáad.
     */
    public void addSource(Source s){
        elements.add(s);
        game.AddSource(s);
        //TODO: View
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
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RefreshTimer.getInstance().shutdown();
        Timer.getInstance().terminate();
    }
}