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
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kirja(kirjoittaja, otsikko) "
                + "VALUES ( ?, ? )");
        stmt.setString(1, kirja.getKirjoittaja());
        stmt.setString(2, kirja.getOtsikko());
        stmt.execute();

        conn.close();

    }
}
