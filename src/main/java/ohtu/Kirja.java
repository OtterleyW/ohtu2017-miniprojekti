package ohtu;

public class Kirja {
    
    private String otsikko;
    private String kirjoittaja;
    private String id;

    public Kirja(String kirjoittaja, String otsikko) {

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
    
    public String getId(){
        return id;
    }
    
    public void setId(String id){
        this.id = id;
    }

}
