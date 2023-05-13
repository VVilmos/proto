package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Element {

    /**
     * Az adott elemen álló játékosok listája
     */
    protected List<Player> players = new ArrayList<>();

    /**
     * @return az adott elemen álló játékosok listája
     */
    public List<Player> GetPlayers() {
        return this.players;
    }

    /**
     * Az adott elem szomszédait visszaadó absztrakt függvény, Pipe és a Node osztályokban van megvalósítva
     * @return a szomszédok listája
     */
    public abstract List<Element> GetNeighbours();

    /**
     * A paraméterként megadott játékos elfogadását/ráléptetését az adott elemre megvalósító absztarkt függvény,
     * a Pipe és a Node osztályokban van megvalósítva
     * @param p Az adott elemre lépni kívánó játékos
     * @return sikerült-e a rálépés
     */
    public abstract boolean AcceptPlayer(Player p);

    /**
     * A paraméterként megadott játékost törli a rajta levő játékosok közül
     * @param p az eltávolítandó játékos
     */
    public void RemovePlayer(Player p) {
        players.remove(p);
    }

    /**
     * Csatlakoztatja a megadott csővéget az adott elemhez (a Node osztályban van felüldefiniálva)
     * @param pe a csatlakozatni kívánt csővég
     * @return sikerült-e a csatlakoztatás
     */
    public boolean AddPipe(PipeEnd pe) { return false;}

    /**
     * Lecsatlakoztatja a megadott csővéget az adott elemről (a Node osztályban van felüldefiniálva)
     * @param pe a lecsatlakoztatni kívánt csővég
     */
    public void RemovePipe(PipeEnd pe) {}

    /**
     * Megjavítja az adott Elementet (a Pump osztályban van felüldefiniálva)
     */
    public void Repair() {}

    /**
     * Átállítja a be- és kimeneti csövégeket (a Pump osztályban van felüldefiniálva)
     * @param from bemeneti csővég
     * @param to kimeneti csővég
     */
    public void Switch(PipeEnd from, PipeEnd to) {}

    /**
     * Létrehoz egy új pumpát az adott elemnél (a Cistern osztályban van felüldefiniálva)
     * @return az új pumpa
     */
    public Pump MakePump() {return null;}

    /**
     * Létrehoz egy új csövet az adott elemnél (a Cistern osztályban van felüldefiniálva)
     * @return az új cső szabad vége
     */
    public PipeEnd MakePipe() {return null;}

    /**
     * Elvágja az adott elemet (a Pipe osztályban van felüldefiniálva)
     * @return a keletkezett új cső
     */
    public Pipe Cut() {return null;}

    /**
     * Kilyukasztja az adott elemet (a Pipe osztályban van felüldefiniálva)
     */
    public void Leak() {}

    /**
     * Megfoltozza az adott elemet (a Pipe osztályban van felüldefiniálva)
     */
    public void Patch() {}

    /**
     * Az adott elem végeit adja vissza (a Pipe osztályban van felüldefiniálva)
     * @return a végek listája
     */
    public List<PipeEnd> GetEnds() {return null;}

    public PipeEnd[] GetPipeEnds() { return null; }

    /**
     * Az adott elemet ragadóssá teszi (a Pipe osztályban van felüldefiniálva)
     */
    public void MakeSticky(){}

    /**
     * Az adott elemet csúszóssá teszi (a Pipe osztályban van felüldefiniálva)
     */
    public void MakeSlippery(){}
}
