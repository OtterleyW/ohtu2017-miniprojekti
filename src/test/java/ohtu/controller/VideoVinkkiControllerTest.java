package ohtu.controller;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import ohtu.Video;
import ohtu.VideoDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoVinkkiControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;
    private VideoDao videoDao;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        this.videoDao = new VideoDao("jdbc:sqlite:kirjasto.db");
    }

    @Test
    public void videovinkitSivuStatusOk() throws Exception {
        mockMvc.perform(get("/videovinkit"))
                .andExpect(status().isOk());
    }

    @Test
    public void videovinkitLoytyyModelista() throws Exception {
        mockMvc.perform(get("/videovinkit"))
                .andExpect(model().attributeExists("videot"))
                .andExpect(model().attributeExists("katsotutVideot"));
    }

    @Test
    public void lisattyVideoLoytyyTietokannasta() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String url = "www.testisaitti.fi";

        mockMvc.perform(post("/lisaavideo")
                .param("otsikko", otsikko)
                .param("url", url)
                .param("kuvaus", ""));

        MvcResult res = mockMvc.perform(get("/videovinkit")).andReturn();

        Collection<Video> videot = (Collection) res.getModelAndView().getModel().get("videot");

        boolean loytyi = false;

        for (Video video : videot) {
            if (video.getOtsikko().equals(otsikko) && video.getUrl().equals(url)) {
                loytyi = true;
                break;
            }
        }

        assertTrue(loytyi);
    }

    @Test
    public void muokkaussivunStatusOk() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        videoDao.lisaaVideo(otsikko, "www.twitch.tv", "");

        List<Video> videot = videoDao.haeVideot();
        String id = "";

        for (Video video : videot) {
            if (video.getOtsikko().equals(otsikko) && video.getUrl().equals("www.twitch.tv")) {
                id = video.getId();
                break;
            }
        }

        mockMvc.perform(get("/" + id + "/muokkaavideota"))
                .andExpect(status().isOk());
    }

    @Test
    public void videonMuokkausToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        videoDao.lisaaVideo(otsikko, "www.youtube.com", "");
        String uusiOtsikko = UUID.randomUUID().toString().substring(0, 15);

        List<Video> katsomattomat = videoDao.haeVideotKatsotunPerusteella("0");

        String id = "";

        for (Video video : katsomattomat) {
            if (video.getOtsikko().equals(otsikko) && video.getUrl().equals("www.youtube.com")) {
                id = video.getId();
                break;
            }
        }

        mockMvc.perform(post("/" + id + "/muokkaa_videota")
                .param("id", id)
                .param("otsikko", uusiOtsikko)
                .param("url", "www.uusisaitti.com")
                .param("kuvaus", "muokattu kuvaus"));

        assertTrue(videoDao.haeVideo(id).getOtsikko().equals(uusiOtsikko)
                && videoDao.haeVideo(id).getUrl().equals("www.uusisaitti.com")
                && videoDao.haeVideo(id).getKuvaus().equals("muokattu kuvaus"));
    }

    @Test
    public void videonPoistoToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";
        videoDao.lisaaVideo(otsikko, url, "");

        List<Video> katsomattomat = videoDao.haeVideotKatsotunPerusteella("0");

        String id = "";

        for (Video video : katsomattomat) {
            if (video.getOtsikko().equals(otsikko) && video.getUrl().equals(url)) {
                id = video.getId();
                break;
            }
        }

        mockMvc.perform(post("/" + id + "/poista_video")
                .param("id", id));

        boolean loytyi = false;

        for (Video video : videoDao.haeVideot()) {
            if (video.getOtsikko().equals(otsikko) && video.getUrl().equals(url)) {
                loytyi = true;
            }
        }

        assertTrue(!loytyi);
    }

    @Test
    public void videonKatsotuksiMerkkaaminenToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        videoDao.lisaaVideo(otsikko, url, "");

        List<Video> katsomattomat = videoDao.haeVideotKatsotunPerusteella("0");
        String id = "";

        for (Video video : katsomattomat) {
            if (video.getUrl().equals(url) && video.getOtsikko().equals(otsikko)) {
                id = video.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/onko_katsottu")
                .param("id", id));

        assertTrue(videoDao.haeVideo(id).getLuettu().equals("1"));
    }

    @Test
    public void katsotunVideonMerkkaaminenKatsomattomaksiToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        videoDao.lisaaVideo(otsikko, url, "");

        List<Video> katsomattomat = videoDao.haeVideotKatsotunPerusteella("0");
        String id = "";

        for (Video video : katsomattomat) {
            if (video.getUrl().equals(url) && video.getOtsikko().equals(otsikko)) {
                id = video.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/onko_katsottu")
                .param("id", id));

        mockMvc.perform(get("/" + id + "/onko_katsottu")
                .param("id", id));

        assertTrue(videoDao.haeVideo(id).getLuettu().equals("0"));
    }

    @Test
    public void naytaInfoSivuToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        videoDao.lisaaVideo(otsikko, url, "");

        List<Video> katsomattomat = videoDao.haeVideotKatsotunPerusteella("0");
        String id = "";

        for (Video video : katsomattomat) {
            if (video.getUrl().equals(url) && video.getOtsikko().equals(otsikko)) {
                id = video.getId();
            }
        }

        mockMvc.perform(get("/video/" + id + "/info")
                .param("id", id))
                .andExpect(status().isOk());
    }

    @Test
    public void poistonVarmistussivuToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String url = "www." + UUID.randomUUID().toString().substring(0, 7) + ".com";

        videoDao.lisaaVideo(otsikko, url, "");

        List<Video> katsomattomat = videoDao.haeVideotKatsotunPerusteella("0");
        String id = "";

        for (Video video : katsomattomat) {
            if (video.getUrl().equals(url) && video.getOtsikko().equals(otsikko)) {
                id = video.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/poistavideo")
                .param("id", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("video"));
    }
}
