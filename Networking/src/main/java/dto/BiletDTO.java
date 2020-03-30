package dto;

import java.io.Serializable;

public class BiletDTO implements Serializable {
    private String id;
    private String numeClient;
    private float pret;
    private String idMeci;
    private String idClient;

    public BiletDTO(String id, String numeClient, float pret, String idMeci, String idClient) {
        this.id = id;
        this.numeClient = numeClient;
        this.pret = pret;
        this.idMeci = idMeci;
        this.idClient = idClient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(float pret) {
        this.pret = pret;
    }

    public String getIdMeci() {
        return idMeci;
    }

    public void setIdMeci(String idMeci) {
        this.idMeci = idMeci;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
}
