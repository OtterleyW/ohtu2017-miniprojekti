package ohtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ohtu.utils.ValidatorUtils;

public class VideoDao {

    private String tietokantaosoite;

    public VideoDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public boolean lisaaVideo(String otsikko, String url, String kuvaus) throws Exception {
        if (ValidatorUtils.areParametersValid(otsikko, url)) {
            Video video = new Video(otsikko, ValidatorUtils.returnValidUrl(url), "0", kuvaus);
            String komento = "INSERT INTO Video(otsikko, url, luettu, kuvaus) "
                    + "VALUES ( ?, ?, ?, ?)";

            DaoHelper.createAndExecuteStatement(tietokantaosoite, komento, video.getOtsikko(), video.getUrl(), video.getLuettu(), video.getKuvaus());

            return true;
        }

        return false;
    }

    public List<Video> haeVideot() throws Exception {
        List<Video> videot = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Video");

        while (rs.next()) {
            Video v = luoVideo(rs);

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

        Video video = new Video(rs.getString("otsikko"), rs.getString("url"), rs.getString("luettu"), rs.getString("kuvaus"));
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
        stmt = conn.prepareStatement("DELETE FROM videotag WHERE video_id = ?");
        stmt.setString(1, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    public boolean muokkaaVideota(String id, String otsikko, String url, String kuvaus) throws Exception {
        if (ValidatorUtils.areParametersValid(otsikko, url)) {
            String komento = "UPDATE Video SET otsikko = ?, url = ?, kuvaus = ? WHERE id = ? ";
            DaoHelper.createAndExecuteStatement(tietokantaosoite, komento, otsikko, ValidatorUtils.returnValidUrl(url), kuvaus, id);

            return true;
        }

        return false;
    }

    public void muutaOnkoLuettu(String onkoLuettu, String id) throws Exception {
        String komento = "UPDATE Video SET luettu = ? WHERE id = ? ";
        DaoHelper.createAndExecuteStatement(tietokantaosoite, komento, onkoLuettu, id);
    }

    public List<Video> haeVideotKatsotunPerusteella(String katsottu) throws Exception {
        List<Video> videot = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Video WHERE luettu = ?");
        stmt.setObject(1, katsottu);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            if (katsottu.equals(rs.getString("luettu"))) {
                Video video = luoVideo(rs);

                videot.add(video);
            }
        }

        stmt.close();
        rs.close();
        conn.close();

        return videot;
    }

    private Video luoVideo(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String otsikko = rs.getString("otsikko");
        String url = rs.getString("url");
        String onkoLuettu = rs.getString("luettu");
        String kuvaus = rs.getString("kuvaus");

        Video video = new Video(otsikko, url, onkoLuettu, kuvaus);
        video.setId(id);
        return video;
    }

    public List<Video> haeHakusanaaVastaavat(String hakusana) throws Exception {
        String haku = hakusana.toLowerCase().trim();
        List<Video> videot = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Video");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String id = rs.getString("id");
            String otsikko = rs.getString("otsikko");
            String url = rs.getString("url");
            String onkoLuettu = rs.getString("luettu");
            String kuvaus = rs.getString("kuvaus");

            boolean loytyiOmistaTiedoista = false;

            if ((otsikko != null && otsikko.toLowerCase().contains(haku))
                    || (kuvaus != null && kuvaus.toLowerCase().contains(haku))
                    || (url != null && url.toLowerCase().contains(haku))) {

                loytyiOmistaTiedoista = true;
                Video video = new Video(otsikko, url, onkoLuettu, kuvaus);
                video.setId(id);
                videot.add(video);
            }

            if (!loytyiOmistaTiedoista) {
                List<Tag> tagit = haeTagitVideonIdnPerusteella(id);

                for (Tag tag : tagit) {
                    if (tag.getNimi().toLowerCase().contains(haku)) {
                        Video video = new Video(otsikko, url, onkoLuettu, kuvaus);
                        video.setId(id);
                        videot.add(video);
                        break;
                    }
                }
            }
        }

        rs.close();
        stmt.close();
        conn.close();

        return videot;
    }

    public void lisaaTagi(String videoId, String tagi) throws Exception {

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tag WHERE nimi = ?");
        stmt.setString(1, tagi);
        ResultSet rs = stmt.executeQuery();

        if (rs.isBeforeFirst()) {
            String tagid = rs.getString("id");

            stmt = conn.prepareStatement("INSERT INTO videotag (video_id, tag_id) "
                    + "VALUES ( ? , ?)");
            stmt.setString(1, videoId);
            stmt.setString(2, tagid);
            stmt.execute();
            stmt.close();
            conn.close();
        } else {
            stmt = conn.prepareStatement("INSERT INTO Tag(nimi) "
                    + "VALUES ( ? )");
            stmt.setString(1, tagi);
            stmt.execute();
            stmt = conn.prepareStatement("INSERT INTO videotag (video_id, tag_id) "
                    + "VALUES ( ? , (SELECT id FROM Tag ORDER BY id DESC LIMIT 1))");
            stmt.setString(1, videoId);
            stmt.execute();
            stmt.close();
            conn.close();
        }

    }

    public List<Tag> haeTagitVideonIdnPerusteella(String videoId) throws Exception {
        List<Tag> tagit = new ArrayList();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tag WHERE Tag.id IN (SELECT tag_id FROM videotag WHERE video_id = ?)");
        stmt.setString(1, videoId);
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

    public void poistaVideoltaTagi(String tagiId, String videoId) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM videotag WHERE video_id = ? AND tag_id = ?");
        stmt.setString(1, videoId);
        stmt.setString(2, tagiId);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    public List<Video> kaikkiVinkitTagilla(String taginNimi) throws Exception {
        List<Video> videot = new ArrayList();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Video WHERE Video.id IN (SELECT video_id FROM videotag WHERE tag_id IN (SELECT id FROM Tag WHERE nimi = ? ))");
        stmt.setString(1, taginNimi);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Video video = luoVideo(rs);
            videot.add(video);
        }

        rs.close();
        stmt.close();
        conn.close();

        return videot;
    }
}
