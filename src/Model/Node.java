package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Aktív elemeket reprezentáló absztrakt osztály
 * Felelőssége: tárolja a hozzá csatlakoztatott csöveket közvetve a csővégek által
 */
public abstract class Node extends Element implements ISteppable{
    private static final int pipeCapacity = 50;

    /**
     * fix hosszúságú csővégekből álló tömb, az aktív elemhez csatlakoztatott csővégeket jelöli
     */
    protected PipeEnd[] pipeEnds = new PipeEnd[pipeCapacity];

    /**
     * Az osztály konstruktora, ahol inicializálódik a csővégek tömbje
     */
    Node(){
        for(int i = 0; i < pipeCapacity; i++){
            pipeEnds[i] = null;
        }
    }
    /**
     * Az aktív elemre egy karakter próbál lépni
     * @param p az érkező karakter
     * @return a mezőváltás/karakterfogadás sikeressége
     */
    @Override
    public boolean AcceptPlayer(Player p) {
        players.add(p);
        return true;
    }

    /**
     * Absztrakt függvény, az aktív elem lép
     */
    @Override
    public abstract void Step();

    /**
     * @return az adott Node szomszédai
     */
    public List<Element> GetNeighbours() {
        List<Element> neighbours = new ArrayList<>();
        for (int i = 0; i < pipeEnds.length; i++){
            if (pipeEnds[i] != null) {
                neighbours.add(pipeEnds[i].GetOwnPipe());
            }
        }

        return neighbours;
    }

    /**
     * Egy szabadvégű cső csatlakoztatása az aktív elemhez
     * @param pe a cső szabad/be nem kötött vége
     * @return a felcsatlakoztatás sikeressége
     */
    public boolean AddPipe(PipeEnd pe)  {
        int i = 0;
        while(i < pipeCapacity && pipeEnds[i] != null) {i++;}

        if(i < pipeCapacity) {
            pipeEnds[i] = pe;
            pe.ConnectNode(this);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Egy bekötött cső eltávolítása az aktív elemről
     * @param pe az eltávolítani kívánt cső bekötött vége
     */
    public void RemovePipe(PipeEnd pe) {
        for(int i = 0; i < pipeEnds.length; i++){
            if(pipeEnds[i] != null && pipeEnds[i] == pe){
                pipeEnds[i].DisconnectFromNode();
                pipeEnds[i] = null;
                break;
            }
        }
    }

    /**
     * Getter, mely visszaadja az aktív elemhez csatlakozatott csöveg bekötött végeit
     * @return a bekötött csővégek listája
     */
    public PipeEnd[] GetPipeEnds() {
        return this.pipeEnds;
    }
}
