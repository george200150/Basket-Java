package dto;

import java.io.Serializable;


public class UserDTO implements Serializable{
    private String id;
    private String passwd;
    private String nume;

    public UserDTO(String id, String passwd, String nume) {
        this.id = id;
        this.passwd = passwd;
        this.nume = nume;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswd() {
        return passwd;
    }

    @Override
    public String toString(){
        return "UserDTO["+id+' '+passwd+"]";
    }
}
