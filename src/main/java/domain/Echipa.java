package domain;

import java.util.Objects;

public class Echipa extends Entity<String> {
    private String nume;

    public Echipa(String id, String nume) {
        super.setId(id);
        this.nume = nume;
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Echipa)) return false;
        Echipa echipa = (Echipa) o;
        return Objects.equals(getId(), echipa.getId()) &&
                Objects.equals(getNume(), echipa.getNume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNume());
    }

    @Override
    public String toString() {
        return "Echipa{" +
                "id='" + super.getId() + '\'' +
                "nume='" + nume + '\'' +
                '}';
    }
}
