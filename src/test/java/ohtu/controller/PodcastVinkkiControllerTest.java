package ohtu.controller;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import ohtu.Podcast;
import ohtu.PodcastDao;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PodcastVinkkiControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;
    private PodcastDao podcastDao;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        this.podcastDao = new PodcastDao("jdbc:sqlite:kirjasto.db");
    }

    @Test
    public void podcastVinkitSivuStatusOk() throws Exception {
        mockMvc.perform(get("/podcastvinkit"))
                .andExpect(status().isOk());
    }

    @Test
    public void podcastVinkitLoytyyModelista() throws Exception {
        mockMvc.perform(get("/podcastvinkit"))
                .andExpect(model().attributeExists("podcastit"))
                .andExpect(model().attributeExists("kuunnellutPodcastit"));
    }

    @Test
    public void lisattyPodcastLoytyyTietokannasta() throws Exception {
        String url = "www." + UUID.randomUUID().toString().substring(0, 10) + ".com";
        String tekija = "Herra " + UUID.randomUUID().toString().substring(0, 5);

        mockMvc.perform(post("/lisaapodcast")
                .param("url", url)
                .param("tekija", tekija)
                .param("kuvaus", ""));

        MvcResult res = mockMvc.perform(get("/podcastvinkit")).andReturn();

        Collection<Podcast> podcastit = (Collection) res.getModelAndView().getModel().get("podcastit");

        boolean loytyi = false;
        String id = "";

        for (Podcast podcast : podcastit) {
            if (podcast.getTekija().equals(tekija) && podcast.getUrl().equals("http://" + url)) {
                loytyi = true;
                id = podcast.getId();
                break;
            }
        }

        assertTrue(loytyi);
        
        podcastDao.poistaPodcast(id);
    }

    @Test
    public void muokkaussivunStatusOk() throws Exception {
        String tekija = "Herra " + UUID.randomUUID().toString().substring(0, 15);
        String url = "www." + UUID.randomUUID().toString().substring(0, 10) + ".com";
        podcastDao.lisaaPodcast(url, tekija, "");

        List<Podcast> podcastit = podcastDao.haePodcastit();
        String id = "";

        for (Podcast podcast : podcastit) {
            if (podcast.getTekija().equals(tekija) && podcast.getUrl().equals("http://" + url)) {
                id = podcast.getId();
                break;
            }
        }

        mockMvc.perform(get("/" + id + "/muokkaapodcastia"))
                .andExpect(status().isOk());
        
        podcastDao.poistaPodcast(id);
    }

    @Test
    public void podcastinMuokkausToimii() throws Exception {
        String url = "www." + UUID.randomUUID().toString().substring(0, 10) + ".com";
        String tekija = "Herra " + UUID.randomUUID().toString().substring(0, 10);

        podcastDao.lisaaPodcast(url, tekija, "");

        String uusiTekija = "Rouva " + UUID.randomUUID().toString().substring(0, 10);
        String uusiUrl = "www." + UUID.randomUUID().toString().substring(0, 10) + ".fi";

        List<Podcast> kuuntelemattomat = podcastDao.haePodcastitKuunnellunPerusteella("0");

        String id = "";

        for (Podcast pod : kuuntelemattomat) {
            if (pod.getTekija().equals(tekija) && pod.getUrl().equals("http://" + url)) {
                id = pod.getId();
                break;
            }
        }

        mockMvc.perform(post("/" + id + "/muokkaa_podcastia")
                .param("id", id)
                .param("url", uusiUrl)
                .param("tekija", uusiTekija)
                .param("kuvaus", "muokattu kuvaus"));

        assertTrue(podcastDao.haePodcast(id).getTekija().equals(uusiTekija)
                && podcastDao.haePodcast(id).getUrl().equals("http://" + uusiUrl)
                && podcastDao.haePodcast(id).getKuvaus().equals("muokattu kuvaus"));
        
        podcastDao.poistaPodcast(id);
    }

    @Test
    public void podcastinPoistoToimii() throws Exception {
        String tekija = "Herra " + UUID.randomUUID().toString().substring(0, 10);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";
        podcastDao.lisaaPodcast(url, tekija, "");

        List<Podcast> kuuntelemattomat = podcastDao.haePodcastitKuunnellunPerusteella("0");
        String id = "";

        for (Podcast pod : kuuntelemattomat) {
            if (pod.getTekija().equals(tekija) && pod.getUrl().equals("http://" + url)) {
                id = pod.getId();
                break;
            }
        }

        mockMvc.perform(post("/" + id + "/poista_podcast")
                .param("id", id));

        boolean loytyi = false;

        for (Podcast pod : podcastDao.haePodcastit()) {
            if (pod.getTekija().equals(tekija) && pod.getUrl().equals("http://" + url)) {
                loytyi = true;
            }
        }

        assertTrue(!loytyi);
    }

    @Test
    public void podcastinKuunnelluksiMerkkaaminenToimii() throws Exception {
        String tekija = "Herra " + UUID.randomUUID().toString().substring(0, 10);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        podcastDao.lisaaPodcast(url, tekija, "");

        List<Podcast> kuuntelemattomat = podcastDao.haePodcastitKuunnellunPerusteella("0");
        String id = "";

        for (Podcast pod : kuuntelemattomat) {
            if (pod.getUrl().equals("http://" + url) && pod.getTekija().equals(tekija)) {
                id = pod.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/onko_kuunneltu")
                .param("id", id));

        assertTrue(podcastDao.haePodcast(id).getLuettu().equals("1"));
        
        podcastDao.poistaPodcast(id);
    }

    @Test
    public void kuunnellunPodcastinMerkkaaminenKuuntelemattomaksiToimii() throws Exception {
        String tekija = "Herra " + UUID.randomUUID().toString().substring(0, 10);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        podcastDao.lisaaPodcast(url, tekija, "");

        List<Podcast> kuuntelemattomat = podcastDao.haePodcastitKuunnellunPerusteella("0");
        String id = "";

        for (Podcast pod : kuuntelemattomat) {
            if (pod.getUrl().equals("http://" + url) && pod.getTekija().equals(tekija)) {
                id = pod.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/onko_kuunneltu")
                .param("id", id));

        mockMvc.perform(get("/" + id + "/onko_kuunneltu")
                .param("id", id));

        assertTrue(podcastDao.haePodcast(id).getLuettu().equals("0"));
        
        podcastDao.poistaPodcast(id);
    }

    @Test
    public void naytaInfoSivuToimii() throws Exception {
        String tekija = "Mr. " + UUID.randomUUID().toString().substring(0, 10);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        podcastDao.lisaaPodcast(url, tekija, "");

        List<Podcast> kuuntelemattomat = podcastDao.haePodcastitKuunnellunPerusteella("0");
        String id = "";

        for (Podcast pod : kuuntelemattomat) {
            if (pod.getUrl().equals("http://" + url) && pod.getTekija().equals(tekija)) {
                id = pod.getId();
            }
        }

        mockMvc.perform(get("/podcast/" + id + "/info")
                .param("id", id))
                .andExpect(status().isOk());
        
        podcastDao.poistaPodcast(id);
    }

    @Test
    public void poistonVarmistussivuToimii() throws Exception {
        String tekija = "Mrs. " + UUID.randomUUID().toString().substring(0, 10);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        podcastDao.lisaaPodcast(url, tekija, "");
        
        List<Podcast> kuuntelemattomat = podcastDao.haePodcastitKuunnellunPerusteella("0");
        String id = "";

        for (Podcast pod : kuuntelemattomat) {
            if (pod.getUrl().equals("http://" + url) && pod.getTekija().equals(tekija)) {
                id = pod.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/poistapodcast")
                .param("id", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("podcast"));
        
        podcastDao.poistaPodcast(id);
    }
}
