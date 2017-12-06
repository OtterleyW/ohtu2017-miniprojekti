package ohtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KirjaDao {

    private String tietokantaosoite;

    public KirjaDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public boolean lisaaKirja(String kirjoittaja, String otsikko, String kuvaus) throws Exception {
        if (valid(kirjoittaja, otsikko)) {
            Kirja kirja = new Kirja(kirjoittaja, otsikko, "0", kuvaus);
            Connection conn = DriverManager.getConnection(tietokantaosoite);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kirja(kirjoittaja, otsikko, luettu, kuvaus) "
                    + "VALUES ( ?, ?, ?, ? )");
            stmt.setString(1, kirja.getKirjoittaja());
            stmt.setString(2, kirja.getOtsikko());
            stmt.setString(3, kirja.getLuettu());
            stmt.setString(4, kirja.getKuvaus());
            stmt.execute();
            stmt.close();
            conn.close();

            return true;
        }
        return false;
    }

    public List<Kirja> haeKirjat() throws Exception {
        List<Kirja> kirjat = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Kirja");

        while (rs.next()) {
            String id = rs.getString("id");
            String kirjoittaja = rs.getString("kirjoittaja");
            String otsikko = rs.getString("otsikko");
            String onkoLuettu = rs.getString("luettu");
            String kuvaus = rs.getString("kuvaus");

            Kirja k = new Kirja(kirjoittaja, otsikko, onkoLuettu, kuvaus);
            k.setId(id);

            kirjat.add(k);
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

        Kirja kirja = new Kirja(rs.getString("kirjoittaja"), rs.getString("otsikko"), rs.getString("luettu"), rs.getString("kuvaus"));
        kirja.setId(id);

        rs.close();
        conn.close();

        return kirja;
    }

    public void poistaKirja(String id) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kirja WHERE id = ?");
        stmt.setString(1, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    public boolean muokkaaKirjaa(String id, String kirjoittaja, String otsikko, String kuvaus) throws Exception {
        if (valid(kirjoittaja, otsikko)) {
            Connection conn = DriverManager.getConnection(tietokantaosoite);
            PreparedStatement stmt = conn.prepareStatement("UPDATE Kirja SET kirjoittaja = ?, otsikko= ?, kuvaus = ? WHERE id = ? ");
            stmt.setString(1, kirjoittaja);
            stmt.setString(2, otsikko);
            stmt.setString(3, kuvaus);
            stmt.setString(4, id);
            stmt.execute();
            stmt.close();
            conn.close();
            return true;
        }
        return false;
    }

    public void muutaOnkoLuettu(String onkoLuettu, String id) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("UPDATE Kirja SET luettu = ? WHERE id = ? ");
        stmt.setString(1, onkoLuettu);
        stmt.setString(2, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    private boolean valid(String kirjoittaja, String otsikko) {
        if (kirjoittaja == null || otsikko == null) {
            return false;
        }

        if (kirjoittaja.trim().isEmpty()) {
            return false;
        } else if (otsikko.trim().isEmpty()) {
            return false;
        }

        return true;
    }

    public List<Kirja> haeLuettuStatuksenPerusteella(String luettu) throws Exception {
        List<Kirja> kirjat = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kirja WHERE luettu = ?");
        stmt.setObject(1, luettu);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            if (luettu.equals(rs.getString("luettu"))) {
                String id = rs.getString("id");
                String kirjoittaja = rs.getString("kirjoittaja");
                String otsikko = rs.getString("otsikko");
                String onkoLuettu = rs.getString("luettu");
                String kuvaus = rs.getString("kuvaus");

                Kirja k = new Kirja(kirjoittaja, otsikko, onkoLuettu, kuvaus);
                k.setId(id);

                kirjat.add(k);
            }
        }

        rs.close();
        stmt.close();
        conn.close();

        return kirjat;
    }

    public List<Kirja> haeHakusanaaVastaavat(String hakusana) throws Exception {
        List<Kirja> kirjat = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kirja");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String id = rs.getString("id");
            String kirjoittaja = rs.getString("kirjoittaja");
            String otsikko = rs.getString("otsikko");
            String onkoLuettu = rs.getString("luettu");
            String kuvaus = rs.getString("kuvaus");

            if (kirjoittaja.contains(hakusana) || otsikko.contains(hakusana) || (kuvaus != null && kuvaus.contains(hakusana))) {
                Kirja kirja = new Kirja(kirjoittaja, otsikko, onkoLuettu, kuvaus);
                kirja.setId(id);
                kirjat.add(kirja);
            }
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return kirjat;
    }
}
