package model.domain;

//import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="BILETANNOTS")
public class BiletAnnot {
    private String id;
    private String numeClient;
    private float pret;
    private String idMeci;
    private String idClient;

    public BiletAnnot(String id, String numeClient, float pret, String idMeci) {
        this.id = id;
        this.numeClient = numeClient;
        this.pret = pret;
        this.idMeci = idMeci;
    }

    public BiletAnnot(String id, String numeClient, float pret, String idMeci, String idClient) {
        this.id = id;
        this.numeClient = numeClient;
        this.pret = pret;
        this.idMeci = idMeci;
        this.idClient = idClient;
    }

    public BiletAnnot() {
    }

    @Id
    //@GeneratedValue(generator="increment") // CANNOT BE PERFORMED AS THE ID IS A STRING !!!
    //@GenericGenerator(name="increment", strategy="increment")
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
