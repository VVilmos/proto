package Model;


import java.util.ArrayList;
import java.util.List;

public class Player {

    protected Element on;

    protected PipeEnd holdingPipeEnd;


    public Player(){

    }
    public void Move(Element to) { //miért boolean a visszatérés?
        Skeleton.Start(this, "Move(" + Skeleton.GetObjectName(to) + ")");
        List<Element> neighbours =  on.GetNeighbours();
        boolean adjacent = false;
        for(int i = 0; i < neighbours.size(); i++){
            if(to.equals(neighbours.get(i))){
                adjacent = true;
            }
        }
        if(!adjacent){
            return;
        }
        boolean accepted;
        accepted = to.AcceptPlayer(this);
        if (accepted){
            on = to;
        }
        Skeleton.End();
    }
    public void SwitchPump(PipeEnd from, PipeEnd to){
        on.Switch(from, to);
    }
    public void ConnectPipe() {
        boolean accepted;
        if(holdingPipeEnd != null){
            accepted = on.AddPipe(holdingPipeEnd);
            if(accepted){
                holdingPipeEnd = null;
            }
        }
    }

    public void DisconnectPipe(PipeEnd p){
        on.RemovePipe(p);
    }
}
