package dto;

import java.io.Serializable;

public class EchipaDTO implements Serializable {
    private String id;
    private String nume;

    public EchipaDTO(String id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString(){
        return "EchipaDTO["+id+' '+nume+"]";
    }
}
