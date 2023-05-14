package Model;

import java.io.Serializable;

/**
 * Pumpát reprezentáló osztály
 * Felelőssége: Egy aktív elem, amely vizet pumpál két bekötött, kiválasztott cső között
 */
public class Pump extends Node implements Serializable {
    /**
     * A pumpa működési állapota
     * Kétféle állapot lehetséges: meghibásodott vagy nem
     * Meghibásodott pumpa nem tud már vizet szívni, csak az átmeneti tárolójában lévő vizet kiengedni
     */
    private boolean isBroken;

    /**
     * Annak a bekötött csőnek a vége, amiből minden ütembe a pumpa vizet próbál szívni
     * A sorszám a pumpa azon "bemenetének" száma, ahová a kiválasztott cső be van kötve
     */
    private int inPipe;

    /**
     * Annak a bekötött csőnek a vége,amibe minden ütembe a pumpa vizet juttat
     */
    private int outPipe;

    /**
     * A pumpa átmeneti tárolójának állapota
     * Kétféle állapotot különbözetünk meg: tele van vízzel vagy nem
     */
    private boolean tankFull;

    /**
     * Az osztály paraméter nélküli konstruktora
     * Alapértelmezetten egy üres tárolójú, de működőképes pumpa jön létre, melynek inPipe/outPipe mezői inicializálatlanok
     */
    public Pump() {
        inPipe = -1;
        outPipe = -1;
        tankFull = false;
        isBroken = false;
    }

    /**
     * A pumpa lép az ütem elején
     * Működése: minden lépésnél a kiválasztott bemeneti csőből a kimeneti csőbe vizet mozgat
     * Elvárt működése függ a működési állapotától és az átmeneti tároló állapotától
     */
    @Override
    synchronized public void Step() {

        if (outPipe != -1 && tankFull && pipeEnds[outPipe] != null) {
            boolean accepted = pipeEnds[outPipe].AcceptWater();
            if (accepted) tankFull = false;
        }
        if (inPipe != -1 && !isBroken && !tankFull && pipeEnds[inPipe] != null) {
            boolean arrived = pipeEnds[inPipe].RemoveWater();
            if (arrived) tankFull = true;
        }
    }

    /**
     * Egy bekötött cső eltávolítása a pumpáról
     *
     * @param pe az eltávolítani kívánt cső bekötött vége
     */
    @Override
    public synchronized void RemovePipe(PipeEnd pe) {
       if (outPipe != -1) {
           PipeEnd out = pipeEnds[outPipe];
           if (out == pe)
               outPipe = -1;
       }

        if (inPipe != -1) {
            PipeEnd in = pipeEnds[inPipe];
            if (in == pe)
                inPipe = -1;
        }
        super.RemovePipe(pe);
    }

    /**
     * A pumpán a víz áramlásának/mozgatásának átirányítása
     *
     * @param from annak a bekötött csőnek a vége, amiből vizet kívánunk mozgatni
     * @param to   annak a bekötött csőnek a vége, amibe vizet kívánunk mozgatni
     */
    synchronized public void Switch(PipeEnd from, PipeEnd to) {
        for (int i = 0; i < pipeEnds.length; i++) {
            if (pipeEnds[i] != null && pipeEnds[i] == from) inPipe = i;
            if (pipeEnds[i] != null && pipeEnds[i] == to) outPipe = i;
        }
    }

    /**
     * A pumpa meghibásodása
     */
    synchronized public void BreakPump() {
        isBroken = true;
    }

    /**
     * A pumpa megszerelése
     */
    synchronized public void Repair() {
        isBroken = false;
    }

    /**
     * A pumpa átmeneti tárolójának feltöltése
     */
    synchronized public void FillWaterTank() {
        tankFull = true;
    }

    /**
     * A pumpa átmeneti tárolójának kirürítése
     */
    synchronized public void EmptyWaterTank() {
        tankFull = false;
    }

    /**
     * Getter a pump aktuális bemeneti csövére
     *
     * @return a pumpa bemeneti csövének sorszáma a csatlakoztatott csövek közül
     */
    synchronized public int GetInPipeNumber() {
        return inPipe;
    }

    /**
     * Getter a pump aktuális kimeneti csövére
     *
     * @return a pumpa kimeneti csövének sorszáma a csatlakoztatott csövek közül
     */
    synchronized public int GetOutPipeNumber() {
        return outPipe;
    }

    /**
     * Getter a pumpa működési állapotára
     *
     * @return a pumpa törött-e
     */
    synchronized public boolean GetBrokenness() {
        return isBroken;
    }

    /**
     * Getter a pumpa átmeneti tárolójának vízszintjére
     *
     * @return az átmeneti tároló teli van-e
     */
    synchronized public boolean GetTankLevel() {
        return tankFull;
    }
}
