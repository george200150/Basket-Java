package model.domain;

public class Client extends Entity<String> {

    private String sessionId;
    private String password;
    private String nume;
<<<<<<< Updated upstream

    public Client(String id, String password) {
=======
    private String host;
    private int port;

    public Client(String id, String sessionId, String password, String nume, String host, int port) {
>>>>>>> Stashed changes
        super.setId(id);
        this.sessionId = sessionId;
        this.password = password;
    }

    public String getNume() {
        return nume;
    }

<<<<<<< Updated upstream
    public void setNume(String nume) {
        this.nume = nume;
=======
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
>>>>>>> Stashed changes
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
