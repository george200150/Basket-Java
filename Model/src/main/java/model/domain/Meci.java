package model.domain;

import java.io.Serializable;
import java.util.Date;

public class Meci extends Entity<String> implements Serializable {
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
    public String getAway() {
        return away;
    }
    public Date getDate() {
        return date;
    }
    public TipMeci getTip() {
        return tip;
    }
    public int getNumarBileteDisponibile() {
        return numarBileteDisponibile;
    }

    @Override
    public String toString() {
        return "model.domain.Meci{" +
                "id='" + super.getId() + '\'' +
                "home=" + home +
                ", away=" + away +
                ", date=" + date +
                ", tip=" + tip +
                ", numarBileteDisponibile=" + numarBileteDisponibile +
                '}';
    }
}
