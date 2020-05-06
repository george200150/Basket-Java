package model.domain;

<<<<<<< Updated upstream
public class Bilet extends Entity<String> {
=======
import java.io.Serializable;

public class Bilet extends Entity<String> implements Serializable {
    private String id;
>>>>>>> Stashed changes
    private String numeClient;
    private float pret;
    private String idMeci;
    private String idClient = null;

    public Bilet(String id, String numeClient, float pret, String idMeci) {
        super.setId(id);
        this.id = id;
        this.numeClient = numeClient;
        this.pret = pret;
        this.idMeci = idMeci;
    }

    public Bilet(String id, String numeClient, float pret, String idMeci, String idClient) {
        this.id = id;
        this.numeClient = numeClient;
        this.pret = pret;
        this.idMeci = idMeci;
        this.idClient = idClient;
    }

    public Bilet() {

    }

    public String getId() {
        return this.id;
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
