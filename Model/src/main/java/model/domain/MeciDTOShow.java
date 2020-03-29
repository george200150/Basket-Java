package model.domain;

import java.util.Date;
import java.util.Objects;

public class MeciDTOShow /*implements Comparable*/ {
    private String id;
    private Echipa home;
    private Echipa away;
    private Date date;
    private TipMeci tip;
    private int numarBilete;

    private String homeString;
    private String awayString;
    private String numarBileteSauSoldOut;

    public MeciDTOShow(String id, Echipa home, Echipa away, Date date, TipMeci tip, int numarBilete) {
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
            this.numarBileteSauSoldOut = Integer.toString(numarBilete);
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

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeciDTOShow)) return false;
        MeciDTOShow that = (MeciDTOShow) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getHome(), that.getHome()) &&
                Objects.equals(getAway(), that.getAway());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getHome(), getAway());
    }

    @Override
    public int compareTo(Object o) { //TODO: not sure if this must implement comparable...
        if (this == o) return 1;
        if (!(o instanceof MeciDTOShow)) return 0;
        else{
            if(this.getId().equals(((MeciDTOShow) o).getId()))
                return 1;
        }
        return 0;
    }*/
}
