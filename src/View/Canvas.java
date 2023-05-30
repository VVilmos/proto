package View;

import Control.Main;

import javax.swing.*;
import javax.swing.text.PlainView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A játék szereplő objektumok grafikájának a rajzolási területet megvalósító osztály
 * JPanel osztály leszármazottja
 * Felelőssége a pályán elhelyezkedő objektum-képek nyilvántartása, frissítése/kirajzoltatása, valamint a felületre érkezett kattintások értelmezése
 */
public class Canvas extends JPanel {

    /**
     * A játék vezérlőjének egy referenciája
     */
    private Main controller;


    /**
     * A rajzolófelületen lévő karakterek
     */
    private List<PlayerView> playerviews = new ArrayList<>();

    /**
     * A rajzolófelületen lévő mezők
     */
    private List<ElementView> elementviews = new ArrayList<>();

    /**
     * A rajzolófelületen frissítése, a mezők és karakterek grafikájának újrarajzolása
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //sárga háttér
        setBackground(new Color(226, 222, 141));

        for (var elementview: elementviews) {
            elementview.Update(g);
        }
        for (var playerview: playerviews) {
            playerview.Update(g);
        }

    }

    /**
     * Az összes mező és karakter törlése a pályáról
     */
    public void Clear() {
        playerviews.clear();
        elementviews.clear();
    }

    /**
     * Eltávolít egy nézetet
     * @param view A nézet, amit eltávolít
     */
    public void Remove(ElementView view){
        elementviews.remove(view);
    }

    /**
     * Új karakter elhelyezése a pályára
     * @param pv az új játékos/karakter kirajzolását végző objektum
     */
    public void AddPlayerView(PlayerView pv) {
        playerviews.add(pv);
    }

    /**
     * Új mező elhelyezése a pályára
     * olyan mezőt veszünk, ami fedi valamelyik másik kirajzolt objektumot, például pumpa, ciszterna, forrás
     * @param ev az mező megjelenítését végző osztály
     */
    public void PushElementView_Back(ElementView ev) {
        elementviews.add(ev);
    }

    /**
     * Új mező elhelyezése a pályára
     * olyan mezőt veszünk, ami tipikusan egy másik objektum takarásában van, például a cső
     * @param ev az mező megjelenítését végző osztály
     */
    public void PushElementView_Front(ElementView ev) {
        elementviews.add(0, ev);
    }

    /**
     * A rajzolófelület paraméteres konstruktora
     */
   public Canvas() {
        controller = Main.getInstance();
       addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {

               int i;
               for (i = elementviews.size() -1 ; i >= 0 ; i--) {
                   Point p = new Point(e.getX(), e.getY());
                   if (elementviews.get(i).ContainsPoint(p)) break;
               }

               if (i < elementviews.size() && i >= 0) controller.Clicked(elementviews.get(i));
               repaint();
           }
       });
   }

    public void Load(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
       elementviews.clear();
       elementviews = (ArrayList<ElementView>) objectInputStream.readObject();
    }

    public void Save(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeObject(elementviews);
    }

    /**
     * Getter a nézetek listájára
     * @return Visszatéríti az összes nézetet egy listában
     */
    public List<ElementView> getElementViews() {
        return elementviews;
    }
}
