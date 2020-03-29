package dto;

import model.domain.TipMeci;

import java.io.Serializable;
import java.util.Date;

public class MeciDTO implements Serializable {
    private String id;
    private String home;
    private String away;
    private Date date;
    private TipMeci tip;
    private int numarBilete;

    public MeciDTO(String id, String home, String away, Date date, TipMeci tip, int numarBilete) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.date = date;
        this.tip = tip;
        this.numarBilete = numarBilete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getNumarBilete() {
        return numarBilete;
    }

    public void setNumarBilete(int numarBilete) {
        this.numarBilete = numarBilete;
    }
}
