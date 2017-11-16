
package ohtu;

public class Kirja {

    private String otsikko;
    private String kirjoittaja;

    public Kirja(String otsikko, String kirjoittaja) {
        this.kirjoittaja = kirjoittaja;
        this.otsikko = otsikko;
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

}
