package utils.events;

import model.domain.Meci;

public class MeciChangeEvent implements Event {
    private ChangeEventType type;
    private Meci data, oldData;

    public MeciChangeEvent(ChangeEventType type, Meci data){
        this.type = type;
        this.data = data;
    }

    public MeciChangeEvent(ChangeEventType type, Meci data, Meci oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() { return type; }

    public Meci getData() { return data; }

    public Meci getOldData() { return oldData; }
}
