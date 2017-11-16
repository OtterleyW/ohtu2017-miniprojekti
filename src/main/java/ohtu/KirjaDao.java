package ohtu;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KirjaDao {

    private String tietokantaosoite;

    public KirjaDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public void lisaaKirja(String otsikko, String kirjoittaja) throws Exception {

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kirja(otsikko, kirjoittaja) "
                + "VALUES ( ?, ? )");
        stmt.setString(1, otsikko);
        stmt.setString(2, kirjoittaja);
        stmt.execute();

        conn.close();

    }
}
