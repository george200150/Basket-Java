package domain;

public class Client extends Entity<String> {

    public Client(String id) {
        super.setId(id);
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + super.getId() + '\'' +
                '}';
    }
}
