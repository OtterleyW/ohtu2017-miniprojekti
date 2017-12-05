package ohtu;

import ohtu.interfaces.Vinkki;

public class Podcast implements Vinkki {

    private String url;
    private String id;
    private String tekija;
    private String luettu;
    private String kuvaus;

    public Podcast(String url, String tekija, String luettu, String kuvaus) {
        this.url = url;
        this.tekija = tekija;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTekija(String tekija) {
        this.tekija = tekija;
    }

    public void setLuettu(String luettu) {
        this.luettu = luettu;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getUrl() {
        return url;
    }

    public String getTekija() {
        return tekija;
    }

    public String getLuettu() {
        return luettu;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
