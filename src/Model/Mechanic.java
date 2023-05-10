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
     * Lerak egy pumpát a csőre, amin áll.
     */
    public void PlacePump() {

        if(holdingPumps.size() == 0) {
            return;
        }

        Pipe newPipe = on.Cut();


        List<PipeEnd> ends = on.GetEnds();
        ends.get(1).ConnectNode(holdingPumps.get(0));

        newPipe.GetEnds().get(1).ConnectNode(holdingPumps.get(0));

        Move(holdingPumps.get(0));
        holdingPumps.remove(0);

    }

    /**
     * Felvesz egy új pumpát.
     * Megkéri a Ciszternát, hogy gyártson le neki egy új pumpát,
     * majd hozzáaadja azt a holdingPumps-hoz.
     */
    public void PickupPump() {
        Pump p = on.MakePump();
        holdingPumps.add(p);
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
            holdingPipeEnd = p;
        }
    }

    /**
     * Megfoltozza a csövet, amin a Mechanic áll.
     */
    public void RepairPipe() {

        on.Patch();
    }
}
