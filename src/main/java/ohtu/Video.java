package ohtu;

import ohtu.interfaces.Vinkki;

public class Video implements Vinkki {

    private String otsikko;
    private String id;
    private String url;
    private String luettu;
    private String kuvaus;

    public Video(String otsikko, String url, String luettu, String kuvaus) {
        this.otsikko = otsikko;
        this.url = url;
        // Arvo 0 = ei luettu, arvo 1 = luettu
        this.luettu = luettu;
        if (kuvaus == null) {
            this.kuvaus = "";
        } else {
            this.kuvaus = kuvaus;
        }
    }

    @Override
    public void merkitseLuetuksi() {
        if (this.luettu.equals("1")) {
            this.luettu = "0";
        } else {
            this.luettu = "1";
        }

    }

    public String getOtsikko() {
        return otsikko;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getLuettu() {
        return luettu;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLuettu(String luettu) {
        this.luettu = luettu;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getKuvaus() {
        return kuvaus;
    }

}
