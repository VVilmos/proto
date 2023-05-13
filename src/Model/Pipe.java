package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cső passzív elem.
 * Tárolja/kezeli a fogadott vizet.
 */
public class Pipe extends Element implements ISteppable{
    /**
     * Ahány időegységig csúszós a cső.
     */
    private int slipperyFor = 0;

    /**
     * Ahány időegységig ragadós a cső.
     */
    private int stickyFor = 0;

    /**
     * Ahány időegységig nem lyukasztható a cső.
     */
    private int protectedFor = 0;

    /**
     * Törött-e a cső.
     */
    private boolean isBroken = false;

    /**
     * Van-e a csőben víz.
     */
    private boolean hasWater = false;

    /**
     * Eltárolja a csőből kifolyt víz mennyiségét.
     */
    private Pool saboteurPool;

    /**
     * A cső végei (1 vagy 2 db)
     */
    private List<PipeEnd> ends = new ArrayList<>();

    /**
     * Konstruktor
     * @param node a node, amihez kezdetben kapcsolva van.
     */
    public Pipe(Node node) {
        PipeEnd end1 = new PipeEnd(this);

        node.AddPipe(end1);

        PipeEnd end2 = new PipeEnd(this);
        ends.add(end1);
        ends.add(end2);

        saboteurPool = Game.getSaboteurPool();
    }

    /**
     * Kilyukasztja a csövet.
     */
    public void Leak() {
        if(protectedFor == 0) {
            isBroken = true;
            if(hasWater) {
                saboteurPool.AddWater();
                hasWater = false;
            }
        }
    }

    /**
     * Megfoltozza a csövet.
     */
    public void Patch() {
        if(isBroken) {
            isBroken = false;
            protectedFor = Game.generateRandomProtectedTime();
        }
    }

    /**
     * Elfogad vizet valakitől.
     * @return sikeres-e a fogadás.
     */
    public boolean AcceptWater() {
        if(hasWater) {
            return false;
        }
        hasWater = true;
        if(isBroken){
            saboteurPool.AddWater();
            hasWater = false;
        }
        return true;
    }

    /**
     * Ráléptet egy játékost a csőre.
     * @param p a játékos.
     * @return sikerült-e a művelet.
     */
    @Override
    public boolean AcceptPlayer(Player p) {
        if(players.size() == 0) {
            if (slipperyFor != 0) {
                players.add(p);
                Node nextNode = ends.get(Game.generateRandomBetween(0, ends.size() - 1)).GetAttachedNode();
                p.Move(nextNode);
                return false;
            }
            if (stickyFor != 0) {
                // p.Stuck();
                players.add(p);
                return true;
            }
            players.add(p);
            return true;
        }
        return false;
    }

    /**
     * Kiszedi a vizet a csőből, ha van benne.
     * @return sikerült-e a művelet.
     */
    public boolean RemoveWater() {
        if (hasWater) {
            hasWater = false;
            return true;
        }
        return false;
    }

    /**
     * Kettévágja a csövet.
     * @return a művelet által létrehozott új cső.
     */
    public Pipe Cut() {
        if(ends.size() == 2) {
            Node node = ends.get(1).GetAttachedNode();
            node.RemovePipe(ends.get(1));

            return new Pipe(node);
        } else {
            return null;
        }
    }

    /**
     * Lekéri a cső végeit.
     * @return a csővégek.
     */
    public List<PipeEnd> GetEnds() {

        return ends;
    }

    /**
     * Lekéri a cső szomszédos elemeit.
     * @return a szomszédok.
     */
    public List<Element> GetNeighbours() {
        List<Element> neighbours = new ArrayList<>();
        for (PipeEnd e : ends) {
            neighbours.add(e.GetAttachedNode());
        }


        return neighbours;
    }

    /**
     * Lépteti a csövet.
     */
    @Override
    public void Step() {
        if(slipperyFor != 0)
            slipperyFor--;
        if(stickyFor != 0)
            stickyFor--;
        if(protectedFor != 0)
            protectedFor--;
    }

    /**
     * Ragadóssá teszi a csövet.
     */
    public void MakeSticky() {
        if(stickyFor == 0)
            stickyFor = Game.generateRandomStickyTime();
    }

    /**
     * Csúszóssá teszi a csövet.
     */
    public void MakeSlippery() {
        if (slipperyFor == 0)
            slipperyFor = Game.generateRandomSlipperyTime();
    }
}
