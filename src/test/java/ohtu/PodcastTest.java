package ohtu;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PodcastTest {

    public PodcastTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void alustusToimii() {

        Podcast v = new Podcast("www.dsdsd.com", "pullamies", "0", "");
        assertEquals(v.getUrl(), "www.dsdsd.com");
        assertEquals(v.getTekija(), "pullamies");
        assertEquals(v.getLuettu(), "0");
    }

    @Test
    public void setTekijaToimii() {

        Podcast v = new Podcast("www.com", "martti", "0", "");
        v.setTekija("Marttimies");
        assertEquals(v.getTekija(), "Marttimies");
    }

    @Test
    public void setIdToimii() {

        Podcast v = new Podcast("otsikko", "www.com", "0", "");
        assertEquals(null, v.getId());
        v.setId("321");
        assertEquals(v.getId(), "321");
    }

    @Test
    public void setUrlToimii() {

        Podcast v = new Podcast("sivusto.com", "John Doe", "1", "");
        v.setUrl("new url");
        assertEquals(v.getUrl(), "new url");
    }

    @Test
    public void getLuettuToimii() {

        Podcast v = new Podcast("www.gg.net", "monkaS", "1", "");
        assertEquals(v.getLuettu(), "1");
    }

    @Test
    public void setLuettuToimii() {

        Podcast v = new Podcast("www.gg.net", "monkaS", "1", "");
        v.setLuettu("0");
        assertEquals(v.getLuettu(), "0");
    }

    @Test
    public void merkitseLuetuksiToimii() {

        Podcast v = new Podcast("www.url.com", "Jane Doe", "0", "");
        v.merkitseLuetuksi();
        assertEquals(v.getLuettu(), "1");
        v.merkitseLuetuksi();
        assertEquals(v.getLuettu(), "0");
    }

    @Test
    public void podcastilleLisataanKuvaus() {
        Podcast v = new Podcast("www.sorsa.fi", "Repe", "0", "");
        String kuvaus = "Kyllä hermo lepää";
        v.setKuvaus(kuvaus);

        assertEquals(kuvaus, v.getKuvaus());
    }

}
