package Model;

/**
 * A játékban levő csővégeket reprezentálja. \n
 * Lehet hozzá Node-ot kötni illetve lecsatlakoztatni róla. \n
 * Tárolja, hogy milyen Node-dal áll összeköttetésben, és hogy melyik csőhöz tartozik.
 */
public class PipeEnd {
    /**
     * Az a cső, melynek egyik vége az adott csővég példány
     */
    private Pipe pipe;
    /**
     * Az az aktív elem/csúcs, amelyhez a csővég csatlakoztatva van
     * Ha a csőnek ez a vége szabad, akkor a mező értéke null
     */
    private Node node = null;

    /**
     * A PipeEnd konstruktora \n
     * Hozzárendeli, felcsatlakoztatja a paraméterként kapott csőre a csővégt.
     *
     * @param p A cső, amire kerül ez a csővég.
     */
    public PipeEnd(Pipe p) {
        this.pipe = p;
    }

    /**
     * Getter a csővéghez tartozó csőre.
     *
     * @return Visszatéríti a csövet, aminek ez a csővég a vége.
     */
    public Pipe GetOwnPipe() {
        return pipe;
    }

    /**
     * Továbbítja a vizet a csőnek.
     *
     * @return Igazzal tér vissza, ha a cső ({@link Pipe}) képes vizet befogadni.
     */
    public boolean AcceptWater() {
        boolean accepted = pipe.AcceptWater();
        return accepted;
    }

    /**
     * Kiszívja, eltávolítja a vizet a csővéghez tartozó csőből.
     *
     * @return Igazzal tér vissza, ha a csőből ({@link Pipe}) lehet vizet kiszívni.
     */
    public boolean RemoveWater() {
        boolean accepted = pipe.RemoveWater();
        return accepted;
    }

    /**
     * A paraméterként kapott {@link Node}-hoz kapcsolja a csővéget.
     *
     * @param node A {@link Node}, amire a csővéget felcsatlakoztatjuk.
     */
    public void ConnectNode(Node node) {
        this.node = node;
    }

    /**
     * Lecsatlakoztatja a csővéget a felkapcsolt {@link Node}-ról.
     */
    public void DisconnectFromNode() {  //pontadas?
        boolean isFull = pipe.RemoveWater();
        if (isFull) Game.getSaboteurPool().AddWater();
        node = null;
    }

    /**
     * Getter a {@link Node}-ra, amire a csővég rá van kapcsolva.
     *
     * @return Visszatéríti a csővéghez kapcsolódó {@link Node}-ot.
     */
    public Node GetAttachedNode() {
        return this.node;
    }

    /**
     * Kiírja a csővég belső állapotát
     */
    public void GetState(){
        System.out.println("pipe:" /* a cső neve*/);
        System.out.println("node: " /* a node neve*/);
    }
}
