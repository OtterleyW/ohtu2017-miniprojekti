package ohtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ohtu.utils.ValidatorUtils;

public class KirjaDao {

    private String tietokantaosoite;

    public KirjaDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public boolean lisaaKirja(String kirjoittaja, String otsikko, String kuvaus) throws Exception {
        if (ValidatorUtils.areParametersValid(kirjoittaja, otsikko)) {
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
            Kirja k = luoKirja(rs);

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
        stmt = conn.prepareStatement("DELETE FROM kirjatag WHERE kirja_id = ?");
        stmt.setString(1, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    public boolean muokkaaKirjaa(String id, String kirjoittaja, String otsikko, String kuvaus) throws Exception {
        if (ValidatorUtils.areParametersValid(kirjoittaja, otsikko)) {
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

    public List<Kirja> haeLuettuStatuksenPerusteella(String luettu) throws Exception {
        List<Kirja> kirjat = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kirja WHERE luettu = ?");
        stmt.setObject(1, luettu);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            if (luettu.equals(rs.getString("luettu"))) {
                Kirja k = luoKirja(rs);
                kirjat.add(k);
            }
        }

        rs.close();
        stmt.close();
        conn.close();

        return kirjat;
    }

    public List<Kirja> haeHakusanaaVastaavat(String hakusana) throws Exception {
        String haku = hakusana.toLowerCase().trim();
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

            boolean loytyiOmistaTiedoista = false;

            if ((kirjoittaja != null && kirjoittaja.toLowerCase().contains(haku))
                    || (otsikko != null && otsikko.toLowerCase().contains(haku))
                    || (kuvaus != null && kuvaus.toLowerCase().contains(haku))) {

                loytyiOmistaTiedoista = true;
                Kirja kirja = new Kirja(kirjoittaja, otsikko, onkoLuettu, kuvaus);
                kirja.setId(id);
                kirjat.add(kirja);
            }

            if (!loytyiOmistaTiedoista) {
                List<Tag> tagit = haeTagitKirjanIdnPerusteella(id);

                for (Tag tag : tagit) {
                    if (tag.getNimi().toLowerCase().contains(haku)) {
                        Kirja kirja = new Kirja(kirjoittaja, otsikko, onkoLuettu, kuvaus);
                        kirja.setId(id);
                        kirjat.add(kirja);
                        break;
                    }
                }
            }
        }

        rs.close();
        stmt.close();
        conn.close();

        return kirjat;
    }

    public void lisaaTagi(String kirjaId, String tagi) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tag WHERE nimi = ?");
        stmt.setString(1, tagi);
        ResultSet rs = stmt.executeQuery();

        if (rs.isBeforeFirst()) {
            String tagid = rs.getString("id");

            stmt = conn.prepareStatement("INSERT INTO kirjatag (kirja_id, tag_id) "
                    + "VALUES ( ? , ?)");
            stmt.setString(1, kirjaId);
            stmt.setString(2, tagid);
            stmt.execute();
            stmt.close();
            conn.close();
        } else {
            stmt = conn.prepareStatement("INSERT INTO Tag(nimi) "
                    + "VALUES ( ? )");
            stmt.setString(1, tagi);
            stmt.execute();
            stmt = conn.prepareStatement("INSERT INTO kirjatag (kirja_id, tag_id) "
                    + "VALUES ( ? , (SELECT id FROM Tag ORDER BY id DESC LIMIT 1))");
            stmt.setString(1, kirjaId);
            stmt.execute();
            stmt.close();
            conn.close();
        }

    }

    public List<Tag> haeTagitKirjanIdnPerusteella(String kirjaId) throws Exception {
        List<Tag> tagit = new ArrayList();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tag WHERE Tag.id IN (SELECT tag_id FROM kirjatag WHERE kirja_id = ?)");
        stmt.setString(1, kirjaId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String id = rs.getString("id");
            String nimi = rs.getString("nimi");
            Tag t = new Tag(nimi);
            t.setId(id);
            tagit.add(t);
        }

        rs.close();
        stmt.close();
        conn.close();

        return tagit;
    }

    public List<Kirja> kaikkiVinkitTagilla(String taginNimi) throws SQLException {
        List<Kirja> kirjat = new ArrayList();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kirja WHERE Kirja.id IN (SELECT kirja_id FROM kirjatag WHERE tag_id IN (SELECT id FROM Tag WHERE nimi = ? ))");
        stmt.setString(1, taginNimi);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Kirja kirja = luoKirja(rs);
            kirjat.add(kirja);
        }

        rs.close();
        stmt.close();
        conn.close();

        return kirjat;
    }

    public void poistaKirjaltaTagi(String tagiId, String kirjaId) throws SQLException {
        
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM kirjatag WHERE kirja_id = ? AND tag_id = ?");
        stmt.setString(1, kirjaId);
        stmt.setString(2, tagiId);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    private Kirja luoKirja(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String kirjoittaja = rs.getString("kirjoittaja");
        String otsikko = rs.getString("otsikko");
        String onkoLuettu = rs.getString("luettu");
        String kuvaus = rs.getString("kuvaus");

        Kirja kirja = new Kirja(kirjoittaja, otsikko, onkoLuettu, kuvaus);
        kirja.setId(id);
        return kirja;
    }
}
