package domain;

public class Bilet extends Entity<String> {
    private float pret;
    private String idMeci;
    private String idClient = null;

    public Bilet(String id, float pret, String idMeci) {
        super.setId(id);
        this.pret = pret;
        this.idMeci = idMeci;
    }

    public Bilet(String id, float pret, String idMeci, String idClient) {
        super.setId(id);
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
        return "Bilet{" +
                "id='" + super.getId() + '\'' +
                "pret=" + pret +
                ", idMeci='" + idMeci + '\'' +
                ", cumparator=" + idClient +
                '}';
    }
}
