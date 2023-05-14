package Model;

import java.io.Serializable;

/**
 * Forrás elem
 * Időegységenként löki ki magából a vizet a hozzá kapcsolódó csövekbe.
 */
public class Source extends Node implements Serializable {
    /**
     * A forrás minden ütem elején való léptetését megvalósító függvény,
     * amely minden hozzákötött csővégbe belepumpálja a vizet
     */
    @Override
    public synchronized void Step() {
        for (PipeEnd pe : pipeEnds) {
            if (pe != null) pe.AcceptWater();
        }
    }
}
