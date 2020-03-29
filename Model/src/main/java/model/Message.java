package model;

import model.domain.Client;
import model.domain.Entity;

public class Message extends Entity<Integer> {
    private Client sender, receiver;
    private String text;

    public Message(Client sender, String text, Client receiver) {
        this.sender = sender;
        this.text = text;
        this.receiver = receiver;
    }

    public Client getSender() {
        return sender;
    }

    public Client getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender.getId() +
                ", receiver=" + receiver.getId() +
                ", text='" + text + '\'' +
                '}';
    }

    private int id;
    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
