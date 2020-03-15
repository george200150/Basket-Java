package domain;

import java.util.Date;

public class Meci extends Entity<String> {
    private String home;
    private String away;
    private Date date;
    private TipMeci tip;
    private int numarBileteDisponibile;

    public Meci(String id, String home, String away, Date date, TipMeci tip, int numarBileteDisponibile) {
        super.setId(id);
        this.home = home;
        this.away = away;
        this.date = date;
        this.tip = tip;
        this.numarBileteDisponibile = numarBileteDisponibile;
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TipMeci getTip() {
        return tip;
    }

    public void setTip(TipMeci tip) {
        this.tip = tip;
    }

    public int getNumarBileteDisponibile() {
        return numarBileteDisponibile;
    }

    public void setNumarBileteDisponibile(int numarBileteDisponibile) {
        this.numarBileteDisponibile = numarBileteDisponibile;
    }

    @Override
    public String toString() {
        return "Meci{" +
                "id='" + super.getId() + '\'' +
                "home=" + home +
                ", away=" + away +
                ", date=" + date +
                ", tip=" + tip +
                ", numarBileteDisponibile=" + numarBileteDisponibile +
                '}';
    }
}
