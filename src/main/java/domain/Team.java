package domain;

import java.util.Objects;

public class Team extends Entity<String> {
    private String nume;

    public Team(String id, String nume) {
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
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return Objects.equals(getId(), team.getId()) &&
                Objects.equals(getNume(), team.getNume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNume());
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + super.getId() + '\'' +
                "nume='" + nume + '\'' +
                '}';
    }
}
