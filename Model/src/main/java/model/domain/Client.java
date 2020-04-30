package model.domain;

import java.io.Serializable;


public class Client extends Entity<String> implements Serializable {

    private String password;
    private String nume;

    private String host;
    private int port;

    public Client(String id, String password, String nume, String host, int port) {
        super.setId(id);
        this.password = password;
        this.nume = nume;
        this.host = host;
        this.port = port;
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "model.domain.Client{" +
                "id='" + super.getId() + '\'' +
                '}';
    }
}
