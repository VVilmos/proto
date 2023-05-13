package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * A szerelőt reprezentáló osztály, a Player osztályból származik.
 * Felelőssége: Képes megjavítani az elromlott pumpákat és a lyukas csöveket,
 * új csövet / pumpákat magához venni és letenni.
 */
public class Mechanic extends Player{
    /**
     * A szerelőnél levő pumpákat tárolja el.
     */
    private List<Pump> holdingPumps = new ArrayList<>();

    /**
     * A Mechanic paraméter nélküli konstruktora.
     */
    public Mechanic(){
        super();
    }

    /**
     * Megjavítja a pumpát, amin a szerelő áll.
     */
    public void RepairPump() {
        on.Repair();
    }

    /**
     * Lerak egy pumpát.
     */
    public void PlacePump() {
        if(holdingPumps.size() == 0) {
            return;
        }
        Pipe newPipe = on.Cut();
        if(newPipe != null) {
            List<PipeEnd> ends = on.GetEnds();
            holdingPumps.get(0).AddPipe(ends.get(1));
            holdingPumps.get(0).AddPipe(newPipe.GetEnds().get(1));
            Move(holdingPumps.get(0));
            holdingPumps.remove(0);
        }
    }

    /**
     * Felvesz egy új pumpát.
     * Megkéri a Ciszternát, hogy gyártson le neki egy új pumpát,
     * majd hozzáaadja azt a holdingPumps-hoz, ha nem null.
     */
    public void PickupPump() {
        Pump p = on.MakePump();
        if(p != null) {
            holdingPumps.add(p);
        }
    }

    /**
     * Felvesz egy új csővéget, amennyiben a Mechanic kezében nincs már egy.
     * Megkéri a Ciszternát, hogy gyártson le neki egy új csövet,
     * majd hozzáadja a végét a holdingPipeEnd-hez.
     * A cső másik vége a Ciszternához van bekötve.
     */
    public void PickupPipe() {
        if(holdingPipeEnd == null){
            PipeEnd p = on.MakePipe();
            if(p != null){
                holdingPipeEnd = p;
            }
        }
    }

    /**
     * Megfoltozza a csövet, amin a Mechanic áll.
     */
    public void RepairPipe() {
        on.Patch();
    }

    /**
     * A játékosnál levő pumpákhoz adja a paraméterben megadott pumpát.
     * @param p a hozzáadandó pumpa
     */
    public void HoldPump(Pump p){
        holdingPumps.add(p);
    }

    /**
     * Visszaadja a játékosnál levő pumpákat.
     * @return a játékosnál levő pumpák
     */
    public List<Pump> GetHoldingPumps(){
        return holdingPumps;
    }
}
