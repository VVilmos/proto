package Model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A játékosok által irányítható karaktereket reprezentáló absztrakt osztály.
 * Felelőssége: a karakterek mozgatása, különböző akciók megvalósítása.
 */
public abstract class Player {
    /**
     * Az az Element, amelyen a Player éppen tartózkodik.
     */
    protected Element on = null;

    /**
     * Az a PipeEnd, amelyet a Player a kezében tart.
     */
    protected PipeEnd holdingPipeEnd = null;

    /**
     * Igaz, amennyiben a játékos hozzáragadt a csőhöz.
     */
    private boolean isStuck = false;

    /**
     * A Player paraméter nélküli konstruktora.
     */
    public Player() {
    }

    /**
     * Átmozgatja a Player-t egy másik, szomszédos Element-re, amennyiben sikerül, eltárolja az új helyzetének.
     * Amennyiben a Player még nincs rajta egy Element-en sem, tehát az on attribútuma null, a mozgatás
     * beállítja a to paramétert az on attribútumnak, amit inicializáláskor használunk.
     * Egyéb esetben, ha nincs odaragadva a csőhöz, lekérdezi a szomszédokat, és ellenőrzi, hogy a megadott Element szomszédos-e azzal az
     * Element-tel, amin áll. Ha ez teljesül, akkor rálépteti a PLayer-t, és ha ez a művelet sikeres volt, akkor
     * eltávolítja az on Elementről a PLayer-t és beállítja az új helyzetét.
     *
     * @param to az az Element, amire átmozgatja a Player-t
     */
    public void Move(Element to) {
        if (on == null) {
            to.AcceptPlayer(this);
            on = to;
        } else if (!isStuck) {
            List<Element> neighbours = on.GetNeighbours();
            boolean adjacent = false;
            for (int i = 0; i < neighbours.size(); i++) {
                if (to.equals(neighbours.get(i))) {
                    adjacent = true;
                }
            }
            if (!adjacent) {
                return;
            }
            boolean accepted = to.AcceptPlayer(this);
            if (accepted) {
                on.RemovePlayer(this);
                on = to;
            }
        }
    }

    /**
     * Átállítja annak a pumpának a bemeneti és kimeneti csövét, amin a karakter áll.
     *
     * @param from az a csővég, amiből kapja a vizet a pumpa
     * @param to   az a csővég, amelyikbe továbbítja a vizet a pumpa
     */
    public void SwitchPump(PipeEnd from, PipeEnd to) {
        on.Switch(from, to);
    }

    /**
     * Csatlakoztatja a holding attribútumban levő csövet ahhoz az Elementhez, amin áll.
     * Csak akkor próbál csatlakoztatni, ha van cső a PLayer kezében, és ha ez sikerült,
     * törli a csövet a kezéből.
     */
    public void ConnectPipe() {
        boolean accepted;
        if (holdingPipeEnd != null) {
            accepted = on.AddPipe(holdingPipeEnd);
            if (accepted) {
                holdingPipeEnd = null;
            }
        }
    }

    /**
     * Lecsatlakoztatja a megadott paraméterű csővéget arról az Elementről, amin áll, és hozzáadja
     * a holdingPipeEnd-hez. Amenniyben a Player-nek már van a kezében egy csővég, akkor nem tud más
     * csövet lecsatlakoztatni.
     *
     * @param p a lecsatlakoztatni kívánt PipeEnd
     */
    public void DisconnectPipe(PipeEnd p) {
        if (holdingPipeEnd == null) {
            var pipeends = on.GetPipeEnds();
            int i;
            for (i = 0; i < pipeends.length; i++) {
                if (pipeends[i] != null && pipeends[i].equals(p)) break;
            }
            if (i < pipeends.length) {
                on.RemovePipe(p);
                holdingPipeEnd = p;
            }
        }
    }

    /**
     * Kilyukasztja a csövet, amin éppen áll.
     */
    public void BreakPipe() {
        on.Leak();
    }

    /**
     * Ragadóssá teszi a csövet, amin a játékos áll.
     */
    public void MakeStickyPipe() {
        on.MakeSticky();
    }

    /**
     * Csúszóssá teszi a csövet, amin éppen áll.
     */
    public void MakeSlipperyPipe() {

    }

    /**
     * Felvesz egy új pumpát.
     * Megkéri a Ciszternát, hogy gyártson le neki egy új pumpát,
     * majd hozzáaadja azt a holdingPumps-hoz, ha nem null.
     */
    public void PickupPump() {}

    /**
     * Felvesz egy új csővéget, amennyiben a Mechanic kezében nincs már egy.
     * Megkéri a Ciszternát, hogy gyártson le neki egy új csövet,
     * majd hozzáadja a végét a holdingPipeEnd-hez.
     * A cső másik vége a Ciszternához van bekötve.
     */
    public void PickupPipe() {}

    /**
     * Lerak egy pumpát.
     */
    public Pipe PlacePump() {
        return null;
     }

    /**
     * A Player-t hozzáragasztja a csőhöz.
     */
    public void Stuck() {
        isStuck = true;
    }

    /**
     * A Player-t elválasztja a csőtől.
     */
    public void Release() {
        isStuck = false;
    }

    /**
     * A megadott cső végét a Player kezébe adja.
     *
     * @param p a megadott cső, amit a Player megfog
     */
    public void HoldPipe(Pipe p) {
        holdingPipeEnd = p.GetEnds().get(0);
    }

    /**
     * A Player-t a megadott Element-re csúsztatja.
     *
     * @param e a megadott Element, amire a Player csúszik
     */
    public void SlippedTo(Element e) {
        on.RemovePlayer(this);
        on = e;
        on.AcceptPlayer(this);
    }

    /**
     * Beállítja a paraméterben megadott csővéget a HoldingPipeEnd-nek.
     *
     * @param pE a beállítandó PipeEnd
     */
    public void SetHoldingPipeEnd(PipeEnd pE) {
        this.holdingPipeEnd = pE;
    }

    /**
     * Visszaadja azt az Elementet, amin a Player éppen áll.
     */
    public Element GetLocation() {
        return on;
    }

    /**
     * Visszaadja azt a csővéget, amit a Player a kezében tart.
     *
     * @return a holdingPipeEnd attribútum
     */
    public PipeEnd GetHoldingPipeEnd() {
        return holdingPipeEnd;
    }

    /**
     * Megjavítja a csövet, amin épp áll.
     */
    public void RepairPump(){}

    /**
     * Megfoltozza a csövet, amin épp áll.
     */
    public void RepairPipe() {}

    /**
     * Visszaadja a játékosnál levő pumpákat.
     *
     * @return a játékosnál levő pumpák
     */
    public List<Pump> GetHoldingPumps() {
        return null;
    }
}
