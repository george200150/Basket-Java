package utils.events;

import model.domain.Bilet;

public class BiletChangeEvent implements Event {
    private ChangeEventType type;
    private Bilet data, oldData;

    public BiletChangeEvent(ChangeEventType type, Bilet data){
        this.type = type;
        this.data = data;
    }

    public BiletChangeEvent(ChangeEventType type, Bilet data, Bilet oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() { return type; }

    public Bilet getData() { return data; }

    public Bilet getOldData() { return oldData; }
}
