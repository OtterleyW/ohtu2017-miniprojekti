package ohtu;

public class Kirja {

    private String otsikko;
    private String kirjoittaja;
    private String tyyppi;
    private String isbn;

    public Kirja(String otsikko, String kirjoittaja) {

        this.kirjoittaja = kirjoittaja;
        this.otsikko = otsikko;
        this.tyyppi = "Kirja";
        this.isbn = "";
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

    public String getTyyppi() {
        return tyyppi;
    }

    public void setKirjoittaja(String kirjoittaja) {
        this.kirjoittaja = kirjoittaja;
    }
    
    public void setISBN(String isbn) {
        this.isbn = isbn;
    }
    
    public String getISBN() {
        return this.isbn;
    }

}
