package ohtu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PodcastDaoTest {

    PodcastDao dao;

    public PodcastDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        dao = new PodcastDao("jdbc:sqlite:kirjasto.db");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void haePodcastitPalauttaaListan() throws Exception {

        List<Podcast> podcastit = dao.haePodcastit();
        assertTrue(podcastit != null);
    }

    @Test
    public void podcastinVoiLisata() throws Exception {

        String url = UUID.randomUUID().toString().substring(0, 15);
        String tekija = UUID.randomUUID().toString().substring(0, 15);

        Podcast v = new Podcast(url, tekija, "0", "");
        dao.lisaaPodcast(url, tekija, "");
        List<Podcast> podcastit = dao.haePodcastit();

        boolean lisatty = false;
        String id = "";
        
        for (Podcast podcast : podcastit) {
            if (podcast.getUrl().equals("http://" + url) && podcast.getTekija().equals(tekija)) {
                lisatty = true;
                id = podcast.getId();
            }
        }

        assertTrue(lisatty);
        dao.poistaPodcast(id);
    }

    @Test
    public void podcastinVoiHakea() throws Exception {

        List<Podcast> podcastit = dao.haePodcastit();
        if (podcastit.size() > 0) {
            Podcast v = podcastit.get(0);
            String id = v.getId();

            Podcast v2 = dao.haePodcast(id);
            assertEquals(v.getTekija(), v2.getTekija());
            assertEquals(v.getUrl(), v2.getUrl());
        }
    }

    @Test
    public void poistaPodcastToimii() throws Exception {

        dao.lisaaPodcast("www.com", "tekija", "");
        List<Podcast> podcastit = dao.haePodcastit();

        Podcast v = podcastit.get(podcastit.size() - 1);
        String id = v.getId();

        dao.poistaPodcast(id);
        podcastit = dao.haePodcastit();
        boolean poistettu = true;

        for (Podcast podcast : podcastit) {
            if (podcast.getId().equals(id)) {
                poistettu = false;
            }
        }
        assertTrue(poistettu);

    }

    @Test
    public void muokkaaPodcastiaToimii() throws Exception {

        dao.lisaaPodcast("qqq.com", "tekija", "");
        List<Podcast> podcastit = dao.haePodcastit();
        Podcast v = podcastit.get(podcastit.size() - 1);

        String id = v.getId();
        dao.muokkaaPodcastia(id, "uusi url", "joku tekija", "kuvaus");
        v = dao.haePodcast(id);

        assertEquals(v.getUrl(), "http://uusi url");
        assertEquals(v.getTekija(), "joku tekija");

        dao.poistaPodcast(id);
    }

    @Test
    public void voiMerkitaLuetuksiTaiLukemattomaksi() throws Exception {

        dao.lisaaPodcast("www.xd.com", "jonne", "");
        List<Podcast> podcastit = dao.haePodcastit();
        Podcast v = podcastit.get(podcastit.size() - 1);

        String id = v.getId();

        dao.muutaOnkoLuettu("0", id);
        assertEquals("0", dao.haePodcast(id).getLuettu());
        dao.muutaOnkoLuettu("1", id);
        assertEquals("1", dao.haePodcast(id).getLuettu());

        dao.poistaPodcast(id);
    }

    @Test
    public void josUrlTaiTekijaTyhjaEiLisata() throws Exception {

        assertFalse(dao.lisaaPodcast("", "tekijä", ""));
        assertFalse(dao.lisaaPodcast("url", "", ""));
        assertFalse(dao.lisaaPodcast("", "", ""));
    }

    @Test
    public void josUrlTaiTekijaTyhjaEiMuokata() throws Exception {

        dao.lisaaPodcast("hauska meemivideo", "sieni.us", "");
        List<Podcast> podcastit = dao.haePodcastit();
        Podcast v = podcastit.get(podcastit.size() - 1);

        assertFalse(dao.muokkaaPodcastia(v.getId(), "", "url", ""));
        assertFalse(dao.muokkaaPodcastia(v.getId(), "otsikko", "", ""));
        assertFalse(dao.muokkaaPodcastia(v.getId(), "", "", ""));
        
        dao.poistaPodcast(v.getId());
    }

    @Test
    public void validMetodiToimiiJosSyoteNull() throws Exception {
        String nimi = null;
        String url = "urli";

        String nimi2 = "nimi";
        String url2 = null;

        assertEquals(false, dao.lisaaPodcast(nimi, url, ""));
        assertEquals(false, dao.lisaaPodcast(nimi2, url2, ""));
    }

    @Test
    public void hakusanallaLoytyyPodcast() throws Exception {
        List<Podcast> podcastit = new ArrayList();
        String nimi = "Haettava podcast";
        String url = "www.haku.url";

        dao.lisaaPodcast(nimi, url, "");
        List<Podcast> kaikki = dao.haePodcastit();
        Podcast v = kaikki.get(kaikki.size() - 1);

        podcastit = dao.haeHakusanaaVastaavat("haettava po");

        assertEquals(false, podcastit.isEmpty());
        
        dao.poistaPodcast(v.getId());
    }
}
