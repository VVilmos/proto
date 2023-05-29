package Control;

import View.Canvas;
import View.MechanicView;
import View.RefreshTimer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * A játék ablakát megjelenítő osztály
 */
public class GameWindow extends JFrame {

    /**
     * A játék vezérlőjének egy referenciája
     */
    private Main controller;

    /**
     * A játékmenetet megjeleníő vászon. (lásd {@link View.Canvas})
     */
    private View.Canvas canvas;

    /**
     * A játékosok listáját tartalmazó felületelem
     */
    private JList playerList;

    /**
     * Egy szerelőt hozzáadó menü elem
     */
    private JMenuItem mAddMechanic;

    /**
     * Egy szabótőrt hozzáadó menüelem
     */
    private JMenuItem mAddSaboteur;

    /**
     * A játékot elindító menüelem.
     */
    private JMenuItem mStart;

    /**
     * A játékot megállító menüelem
     */
    private JMenuItem mPause;

    /**
     * A játékosok listáját tartalmazó felületelem modelleleme
     */
    private DefaultListModel<String> playerListModel;
    /**
     * A játék vezérléséhez szükséges gombok
     */
    private JButton bMove;
    private JButton bRepairpump;
    private JButton bSwitch;
    private JButton bConnect;
    private JButton bDisconnect;
    private JButton bLeakpipe;
    private JButton bSticky;
    private JButton bSlippery;
    private JButton bRepairpipe;
    private JButton bPickuppump;
    private JButton bPickuppipe;
    private JButton bPlacepump;

    /**
     * Játék perzisztenciájához szükséges menüelemek
     */
    private JMenuItem mSave;
    private JMenuItem mLoad;

    /**
     * A játékablak konstruktora
     *
     * @param title Az ablakon megjelenő szöveg
     */
    public GameWindow(String title) {
        super(title);
        controller = Main.getInstance();
        initializeWindow();
        initializeListeners();
        RefreshTimer.getInstance().setComponent(canvas);
        RefreshTimer.getInstance().start();
    }

    /**
     * A felületelemek listenereit inicializáló függvény.
     */
    private void initializeListeners() {
        mAddMechanic.addActionListener(this::addMechanicListener);
        mAddSaboteur.addActionListener(this::addSaboteurListener);
        mPause.addActionListener(this::pauseActionListener);
        mStart.addActionListener(this::startActionListener);
        playerList.addListSelectionListener(this::playerListListener);
        bMove.addActionListener((e) -> controller.SetOperation(Operation.MOVE));
        bRepairpipe.addActionListener((e) -> controller.SetOperation(Operation.REPAIRPIPE));
        bConnect.addActionListener((e) -> controller.SetOperation(Operation.CONNECTPIPE));
        bDisconnect.addActionListener((e) -> controller.SetOperation(Operation.DISCONNECTPIPE));
        bLeakpipe.addActionListener((e) -> controller.SetOperation(Operation.BREAKPIPE));
        bPickuppipe.addActionListener((e) -> controller.SetOperation(Operation.PICKUPPIPE));
        bPlacepump.addActionListener((e) -> controller.SetOperation(Operation.PLACEPUMP));
        bRepairpump.addActionListener((e) -> controller.SetOperation(Operation.REPAIRPUMP));
        bSlippery.addActionListener((e) -> controller.SetOperation(Operation.SLIPPERYPIPE));
        bSticky.addActionListener((e) -> controller.SetOperation(Operation.STICKYPIPE));
        bSwitch.addActionListener((e) -> controller.SetOperation(Operation.SWITCHPUMP));
        bPickuppump.addActionListener((e) -> controller.SetOperation(Operation.PICKUPPUMP));
        mLoad.addActionListener(this::loadListener);
        mSave.addActionListener(this::saveListener);
    }


    /**
     * Az felületet felépítő metódus. Ez hozza létre, helyezi el a felületelemeket.
     */
    private void initializeWindow() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu game = new JMenu("Game");

        mAddMechanic = new JMenuItem("Add Mechanic");
        mSave = new JMenuItem("Save");
        mLoad = new JMenuItem("Load");
        mAddSaboteur = new JMenuItem("Add Saboteur");
        mStart = new JMenuItem("Start");
        mPause = new JMenuItem("Pause");


        file.add(mSave);
        file.add(mLoad);
        edit.add(mAddMechanic);
        edit.add(mAddSaboteur);
        game.add(mStart);
        game.add(mPause);
        bar.add(file);
        bar.add(edit);
        bar.add(game);

        this.setJMenuBar(bar);

        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);

        bMove = new JButton("Move");
        bMove.setMinimumSize(new Dimension(100, 40));
        bSwitch = new JButton("Switch");
        bConnect = new JButton("Connect");
        bDisconnect = new JButton("Disconnect");
        bLeakpipe = new JButton("Leak Pipe");
        bSticky = new JButton("Sticky");
        bSlippery = new JButton("Slippery");
        bRepairpump = new JButton("Repair Pump");
        bRepairpipe = new JButton("Repair Pipe");
        bPickuppump = new JButton("Pick up Pump");
        bPickuppipe = new JButton("Pick up Pipe");
        bPlacepump = new JButton("Place Pump");

        JPanel jp = new JPanel();

        jp.add(bMove);
        jp.add(bSwitch);
        jp.add(bConnect);
        jp.add(bDisconnect);
        jp.add(bLeakpipe);
        jp.add(bSticky);
        jp.add(bSlippery);
        jp.add(bRepairpump);
        jp.add(bRepairpipe);
        jp.add(bPickuppump);
        jp.add(bPickuppipe);
        jp.add(bPlacepump);

        add(jp, BorderLayout.SOUTH);
        JPanel jplist = new JPanel();
        playerListModel = new DefaultListModel<>();
        playerList = new JList(playerListModel);
        jplist.add(playerList);
        add(jplist, BorderLayout.EAST);


    }

    /**
     * Az Add Mechanic menüelem listenere
     */
    public void addMechanicListener(ActionEvent event) {
        String name = JOptionPane.showInputDialog("Name of the Mechanic");
        playerListModel.addElement(name);
        controller.addMechanic(name);
    }

    /**
     * Az Add Saboteur menüelem listenere
     */
    public void addSaboteurListener(ActionEvent event) {
        String name = JOptionPane.showInputDialog("Name of the Saboteur:");
        playerListModel.addElement(name);
        controller.addSaboteur(name);
    }

    /**
     * Egy játékos kiválasztásához szükséges listener.
     */
    public void playerListListener(ListSelectionEvent event) {
        int index = playerList.getSelectedIndex();
        controller.setActivePlayer(index);
    }

    /**
     * A játék elinditásához szükséges listener.
     */
    public void startActionListener(ActionEvent actionEvent) {
        controller.getGame().StartGame();
    }

    /**
     * A játék megállításához szükséges listener.
     */
    public void pauseActionListener(ActionEvent actionEvent) {
        GameEnding ending = controller.getGame().EndGame();
        if (ending == GameEnding.MECHANICS_WIN)
            JOptionPane.showMessageDialog(this, "Mechanics won!");
        else if (ending == GameEnding.SABOTEURS_WIN)
            JOptionPane.showMessageDialog(this, "Saboteurs won!");
        else if (ending == GameEnding.EQUAL)
            JOptionPane.showMessageDialog(this, "It's a tie!");
    }

    /**
     * A játék betöltése menüelem listenerje
     */
    public void loadListener(ActionEvent actionEvent){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            controller.getGame().Load(file.getPath());
            //TODO: Views
        }
    }

    /**
     * A játék elmentése menüelem listenerje
     */
    public void saveListener(ActionEvent actionEvent){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            controller.getGame().Save(file.getPath());
            //TODO: Views
        }
    }

    /**
     * Getter a játékteret megjelenítő vászonra
     * @return A vászon, ami a játékteret tartalmazza
     */
    public Canvas getCanvas() {
        return canvas;
    }
}
