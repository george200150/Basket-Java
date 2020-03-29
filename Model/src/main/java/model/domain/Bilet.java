package model.domain;

public class Bilet extends Entity<String> {
    private String numeClient;
    private float pret;
    private String idMeci;
    private String idClient = null;

    public Bilet(String id, String numeClient, float pret, String idMeci) {
        super.setId(id);
        this.numeClient = numeClient;
        this.pret = pret;
        this.idMeci = idMeci;
    }

    public Bilet(String id, String numeClient, float pret, String idMeci, String idClient) {
        super.setId(id);
        this.numeClient = numeClient;
        this.pret = pret;
        this.idMeci = idMeci;
        this.idClient = idClient;
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
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

    @Override
    public String toString() {
        return "model.domain.Bilet{" +
                "numeClient='" + numeClient + '\'' +
                ", pret=" + pret +
                ", idMeci='" + idMeci + '\'' +
                ", idClient='" + idClient + '\'' +
                '}';
    }
}
