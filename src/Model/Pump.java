package Model;

/**
 * Pumpát reprezentáló osztály
 * Felelőssége: Egy aktív elem, amely vizet pumpál két bekötött, kiválasztott cső között
 */
public class Pump extends Node{
    /**
     * A pumpa működési állapota
     * Kétféle állapot lehetséges: meghibásodott vagy nem
     * Meghibásodott pumpa nem tud már vizet szívni, csak az átmeneti tárolójában lévő vizet kiengedni
     */
    private boolean isBroken = false;

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
     */
    public Pump() {

    }

    /**
     * A pumpa lép az ütem elején
     * Működése: minden lépésnél a kiválasztott bemeneti csőből a kimeneti csőbe vizet mozgat
     * Elvárt működése függ a működési állapotától és az átmeneti tároló állapotától
     */
    @Override
    public void Step() {
        if (tankFull && pipeEnds[outPipe] != null) {
            boolean accepted = pipeEnds[outPipe].AcceptWater();
            if (accepted) tankFull = false;
        }
        if (!isBroken && !tankFull && pipeEnds[inPipe] != null) {
            boolean arrived = pipeEnds[inPipe].RemoveWater();
            if (arrived) tankFull = true;
        }
    }

    /**
     * A pumpán a víz áramlásának/mozgatásának átirányítása
     * @param from annak a bekötött csőnek a vége, amiből vizet kívánunk mozgatni
     * @param to annak a bekötött csőnek a vége, amibe vizet kívánunk mozgatni
     */
    public void Switch(PipeEnd from, PipeEnd to) {
        for (int i = 0; i < pipeEnds.length; i++) {
            if (pipeEnds[i] != null && pipeEnds[i] == from) inPipe = i;
            if (pipeEnds[i] != null && pipeEnds[i] == to) outPipe = i;
        }
    }

    /**
     * A pumpa meghibásodása
     */
    public void BreakPump() {
        isBroken = true;
    }

    /**
     * A pumpa megszerelése
     */
    public void Repair() {
        isBroken = false;
    }

    /**
     * A pumpa átmeneti tárolójának feltöltése
     */
    public void FillWaterTank() {
        tankFull = true;
    }
}
