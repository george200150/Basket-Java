package utils.events;

import domain.Client;

public class ClientChangeEvent implements Event {
    private ChangeEventType type;
    private Client data, oldData;

    public ClientChangeEvent(ChangeEventType type, Client data){
        this.type = type;
        this.data = data;
    }

    public ClientChangeEvent(ChangeEventType type, Client data, Client oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() { return type; }

    public Client getData() { return data; }

    public Client getOldData() { return oldData; }
}
