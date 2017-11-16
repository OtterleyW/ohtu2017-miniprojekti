package ohtu;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class KirjaDao {

    private String tietokantaosoite;

    public KirjaDao(String tietokantaosoite) {
        
        this.tietokantaosoite = tietokantaosoite;
    }

    public void lisaaKirja(String kirjoittaja, String otsikko) throws Exception {

        Kirja kirja = new Kirja(kirjoittaja, otsikko);
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kirja(kirjoittaja, otsikko, tyyppi) "
                + "VALUES ( ?, ?, ? )");
        stmt.setString(1, kirja.getKirjoittaja());
        stmt.setString(2, kirja.getOtsikko());
        stmt.setString(3, kirja.getTyyppi());
        stmt.execute();

        conn.close();

    }
}
