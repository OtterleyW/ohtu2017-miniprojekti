package ohtu;

import ohtu.interfaces.Vinkki;

public class Kirja implements Vinkki {

    private String otsikko;
    private String kirjoittaja;
    private String id;
    private boolean luettu;

    public Kirja(String kirjoittaja, String otsikko) {

        this.kirjoittaja = kirjoittaja;
        this.otsikko = otsikko;
        this.luettu = false;
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
        this.luettu = true;
    }
}
