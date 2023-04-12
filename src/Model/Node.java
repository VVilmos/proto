package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Node extends Element implements ISteppable{

    protected List<PipeEnd> pipeEnds = new ArrayList<>();

    @Override
    public boolean AcceptPlayer(Player p) {
        players.add(p);
        return true;
    }

    @Override
    public abstract void step();

    public List<Element> GetNeighbours() {
        List<Element> neighbours = new ArrayList<>();
        for (int i = 0; i < pipeEnds.size(); i++)
            neighbours.add(pipeEnds.get(i).GetOwnPipe());

        return neighbours;
    }


}
