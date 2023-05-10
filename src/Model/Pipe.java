package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cső passzív elem.
 * Tárolja/kezeli a fogadott vizet.
 */
public class Pipe extends Element{
    /**
     * Törött-e a cső.
     */
    private boolean isBroken = false;

    /**
     * Van-e a csőben víz.
     */
    private boolean hasWater = false;

    /**
     * A cső végei (1 vagy 2 db)
     */
    private List<PipeEnd> ends = new ArrayList<>();

    /**
     * Az éppen létező pipeok száma (csak névadásnál van szerepe).
     */
    private static int count = 0;

    /**
     * Kinullázza a számlálót.
     */
    public static void ResetCounter() {count = 0;}

    /**
     * Konstruktor
     * @param node a node, amihez kezdetben kapcsolva van.
     */
    public Pipe(Node node) {
        count++;

        // Skeleton.CtorStart("PipeEnd(" + Skeleton.GetObjectName(this) + ") end" + count + "1");
        PipeEnd end1 = new PipeEnd(this);

        node.AddPipe(end1);

        PipeEnd end2 = new PipeEnd(this);
        ends.add(end1);
        ends.add(end2);
    }

    /**
     * Kilyukasztja a csövet.
     */
    public void Leak() {
        isBroken = true;
        if (hasWater) {
            Game.getSaboteurPool().AddWater();
            hasWater =false;
        }
    }

    /**
     * Megfoltozza a csövet.
     */
    public void Patch() {
        isBroken = false;
    }

    /**
     * Elfogad csövet valakitől.
     * @return sikeres-e a fogadás.
     */
    public boolean AcceptWater() {
        if(hasWater) {
            return false;
        }
        hasWater = true;
        if(isBroken){
            Game.getSaboteurPool().AddWater();
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

        if (players.size() > 0) {

            return false;
        }
        players.add(p);


        return true;
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
        Node node = ends.get(1).GetAttachedNode();
        node.RemovePipe(ends.get(1));

        Pipe newpip = new Pipe(node);

        return newpip;
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
}
