package model.domain;


/*public enum TipMeci {
    CALIFICARE,
    SAISPREZECIME,
    OPTIME,
    SFERT,
    SEMIFINALA,
    FINALA
}*/





public enum TipMeci {
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
}
