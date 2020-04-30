package model.domain;

import java.io.Serializable;

public enum TipMeci implements Serializable {
    CALIFICARE(1),
    SAISPREZECIME(2),
    OPTIME(3),
    SFERT(4),
    SEMIFINALA(5),
    FINALA(6);

    private int numVal;

    TipMeci(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

    public void setNumVal(int numVal){
        this.numVal = numVal;
    }
}
