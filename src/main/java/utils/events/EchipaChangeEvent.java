package utils.events;

import model.domain.Echipa;

public class EchipaChangeEvent implements Event {

    private ChangeEventType type;
    private Echipa data, oldData;

    public EchipaChangeEvent(ChangeEventType type, Echipa data){
        this.type = type;
        this.data = data;
    }

    public EchipaChangeEvent(ChangeEventType type, Echipa data, Echipa oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() { return type; }

    public Echipa getData() { return data; }

    public Echipa getOldData() { return oldData; }
}
