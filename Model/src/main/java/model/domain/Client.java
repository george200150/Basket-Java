package model.domain;

public class Client extends Entity<String> {

    private String password;
    private String nume;

    public Client(String id, String password) {
        super.setId(id);
        this.password = password;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
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
