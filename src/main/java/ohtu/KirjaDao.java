package ohtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.lang.Error;

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
        stmt.close();
        conn.close();

    }

    public List<Kirja> haeKirjat() throws Exception {
        List<Kirja> kirjat = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Kirja");

        while (rs.next()) {
            String otsikko = rs.getString("otsikko");
            String kirjoittaja = rs.getString("kirjoittaja");

            kirjat.add(new Kirja(otsikko, kirjoittaja));
        }

        rs.close();
        conn.close();

        return kirjat;
    }

    public Kirja haeKirja(String id) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kirja WHERE id = ?");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        Kirja kirja = new Kirja(rs.getString("kirjoittaja"), rs.getString("otsikko"));
        kirja.setId(id);

        rs.close();
        conn.close();

        return kirja;
    }

    public void muokkaaKirjaa(String id, String kirjoittaja, String otsikko) throws Exception {

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("UPDATE Kirja SET kirjoittaja = ?, otsikko= ? WHERE id = ? ");
        stmt.setString(1, kirjoittaja);
        stmt.setString(2, otsikko);
        stmt.setString(3, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }
}
