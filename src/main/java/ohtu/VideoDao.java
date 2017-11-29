package ohtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VideoDao {

    private String tietokantaosoite;

    public VideoDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public boolean lisaaVideo(String otsikko, String url) throws Exception {
        if (valid(otsikko, url)) {
            Video video = new Video(otsikko, url, "0");
            Connection conn = DriverManager.getConnection(tietokantaosoite);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Video(otsikko, url, luettu) "
                    + "VALUES ( ?, ?, ? )");
            stmt.setString(1, video.getOtsikko());
            stmt.setString(2, video.getUrl());
            stmt.setString(3, video.getLuettu());
            stmt.execute();
            stmt.close();
            conn.close();
            return true;
        }
        return false;

    }

    public List<Video> haeVideot() throws Exception {
        List<Video> videot = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Video");

        while (rs.next()) {
            String id = rs.getString("id");
            String otsikko = rs.getString("otsikko");
            String url = rs.getString("url");
            String onkoLuettu = rs.getString("luettu");

            Video v = new Video(otsikko, url, onkoLuettu);
            v.setId(id);

            videot.add(v);
        }

        rs.close();
        conn.close();

        return videot;
    }

    public Video haeVideo(String id) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Video WHERE id = ?");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        Video video = new Video(rs.getString("otsikko"), rs.getString("url"), rs.getString("luettu"));
        video.setId(id);

        rs.close();
        conn.close();

        return video;
    }

    public void poistaVideo(String id) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Video WHERE id = ?");
        stmt.setString(1, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    public boolean muokkaaVideota(String id, String otsikko, String url) throws Exception {
        if (valid(otsikko, url)) {
            Connection conn = DriverManager.getConnection(tietokantaosoite);
            PreparedStatement stmt = conn.prepareStatement("UPDATE Video SET otsikko = ?, url = ? WHERE id = ? ");
            stmt.setString(1, otsikko);
            stmt.setString(2, url);
            stmt.setString(3, id);
            stmt.execute();
            stmt.close();
            conn.close();
            return true;
        }
        return false;
    }

    public void muutaOnkoLuettu(String onkoLuettu, String id) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("UPDATE Video SET luettu = ? WHERE id = ? ");
        stmt.setString(1, onkoLuettu);
        stmt.setString(2, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    private boolean valid(String otsikko, String url) {
        if (otsikko.isEmpty()) {
            return false;
        } else if (url.isEmpty()) {
            return false;
        }
        return true;
    }

}
