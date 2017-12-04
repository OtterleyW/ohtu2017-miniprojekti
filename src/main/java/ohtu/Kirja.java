package ohtu;

import ohtu.interfaces.Vinkki;

public class Kirja implements Vinkki {

    private String otsikko;
    private String kirjoittaja;
    private String id;
    private String luettu;
    private String kuvaus;

    public Kirja(String kirjoittaja, String otsikko, String luettu, String kuvaus) {

        this.kirjoittaja = kirjoittaja;
        this.otsikko = otsikko;
        // Arvo 0 = ei luettu, arvo 1 = luettu
        this.luettu = luettu;
        if (kuvaus == null) {
            this.kuvaus = "";
        } else {
            this.kuvaus = kuvaus;
        }
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public String getKirjoittaja() {
        return kirjoittaja;
    }

    public void setKirjoittaja(String kirjoittaja) {
        this.kirjoittaja = kirjoittaja;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void merkitseLuetuksi() {
        if (this.luettu.equals("1")) {
            this.luettu = "0";
        } else {
            this.luettu = "1";
        }

    }

    public String getLuettu() {
        return luettu;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getKuvaus() {
        return kuvaus;
    }
}
