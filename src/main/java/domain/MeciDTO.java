package domain;

import java.util.Date;

public class MeciDTO {
    private String id;
    private Echipa home;
    private Echipa away;
    private Date date;
    private TipMeci tip;
    private int numarBilete;

    private String homeString;
    private String awayString;
    private String numarBileteSauSoldOut;

    public MeciDTO(String id, Echipa home, Echipa away, Date date, TipMeci tip, int numarBilete) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.date = date;
        this.tip = tip;
        this.numarBilete = numarBilete;

        this.homeString = home.toString();
        this.awayString = away.toString();
        if(numarBilete == 0)
            this.numarBileteSauSoldOut = "SOLD OUT";
        else
            this.numarBileteSauSoldOut = numarBilete + "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Echipa getHome() {
        return home;
    }

    public void setHome(Echipa home) {
        this.home = home;
    }

    public Echipa getAway() {
        return away;
    }

    public void setAway(Echipa away) {
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

    public String getHomeString() {
        return homeString;
    }

    public void setHomeString(String homeString) {
        this.homeString = homeString;
    }

    public String getAwayString() {
        return awayString;
    }

    public void setAwayString(String awayString) {
        this.awayString = awayString;
    }

    public String getNumarBileteSauSoldOut() {
        return numarBileteSauSoldOut;
    }

    public void setNumarBileteSauSoldOut(String numarBileteSauSoldOut) {
        this.numarBileteSauSoldOut = numarBileteSauSoldOut;
    }
}
