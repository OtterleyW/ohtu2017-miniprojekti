package ohtu.controller;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import ohtu.Kirja;
import ohtu.KirjaDao;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
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
public class KirjaVinkkiControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;
    private KirjaDao kirjaDao;

    @Before
    public void setUp() {
        this.kirjaDao = new KirjaDao("jdbc:sqlite:kirjasto.db");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void kirjavinkitSivuStatusOk() throws Exception {
        mockMvc.perform(get("/vinkit"))
                .andExpect(status().isOk());
    }

    @Test
    public void kirjavinkitStatusOkJaModelissaVinkit() throws Exception {
        mockMvc.perform(get("/vinkit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lukemattomat"))
                .andExpect(model().attributeExists("luetut"));
    }

    @Test
    public void lisattyKirjaLoytyyTietokannasta() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 10);

        mockMvc.perform(post("/lisaakirja")
                .param("kirjoittaja", "Kirjoittava Kirjoittaja")
                .param("otsikko", otsikko)
                .param("kuvaus", ""));

        MvcResult result = mockMvc.perform(get("/vinkit")).andReturn();

        Collection<Kirja> kirjat = (Collection) result.getModelAndView().getModel().get("lukemattomat");

        boolean loytyi = false;
        
        String id = "";

        for (Kirja kirja : kirjat) {
            if (kirja.getKirjoittaja().equals("Kirjoittava Kirjoittaja") && kirja.getOtsikko().equals(otsikko)) {
                loytyi = true;
                id = kirja.getId();
            }
        }

        assertTrue(loytyi);
        kirjaDao.poistaKirja(id);
    }

    @Test
    public void muokkaussivunStatusOk() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);

        kirjaDao.lisaaKirja("Olen Kirjailija", otsikko, "");

        List<Kirja> kirjat = kirjaDao.haeKirjat();
        String id = "";

        for (Kirja kirja : kirjat) {
            if (kirja.getKirjoittaja().equals("Olen Kirjailija") && kirja.getOtsikko().equals(otsikko)) {
                id = kirja.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/muokkaa"))
                .andExpect(status().isOk());
        
        kirjaDao.poistaKirja(id);
    }

    @Test
    public void kirjanMuokkausToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 10);
        kirjaDao.lisaaKirja("Myös Kirjailija", otsikko, "");
        String uusiOtsikko = UUID.randomUUID().toString().substring(0, 15);

        List<Kirja> lukemattomat = kirjaDao.haeLuettuStatuksenPerusteella("0");

        String id = "";

        for (Kirja kirja : lukemattomat) {
            if (kirja.getKirjoittaja().equals("Myös Kirjailija") && kirja.getOtsikko().equals(otsikko)) {
                id = kirja.getId();
                break;
            }
        }

        mockMvc.perform(post("/" + id + "/muokkaa_kirjaa")
                .param("id", id)
                .param("kirjoittaja", "Minua on muokattu! :)")
                .param("otsikko", uusiOtsikko)
                .param("kuvaus", ""));

        assertTrue(kirjaDao.haeKirja(id).getKirjoittaja().equals("Minua on muokattu! :)")
                && kirjaDao.haeKirja(id).getOtsikko().equals(uusiOtsikko));
        
        kirjaDao.poistaKirja(id);
    }

    @Test
    public void luetuksiMerkkaaminenToimii() throws Exception {
        // Luodaan varmasti uniikki kirjoittaja & otsikko ettei löydy samaa ennestään
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String kirjoittaja = UUID.randomUUID().toString().substring(0, 15);

        kirjaDao.lisaaKirja(kirjoittaja, otsikko, "");

        List<Kirja> lukemattomat = kirjaDao.haeLuettuStatuksenPerusteella("0");

        String id = "";

        for (Kirja kirja : lukemattomat) {
            if (kirja.getKirjoittaja().equals(kirjoittaja) && kirja.getOtsikko().equals(otsikko)) {
                id = kirja.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/onko_luettu")
                .param("id", id));

        boolean loytyiLuetuista = false;

        List<Kirja> luetut = kirjaDao.haeLuettuStatuksenPerusteella("1");

        for (Kirja kirja : luetut) {
            if (kirja.getId().equals(id)
                    && kirja.getOtsikko().equals(otsikko)
                    && kirja.getKirjoittaja().equals(kirjoittaja)) {

                loytyiLuetuista = true;
            }
        }

        assertTrue(kirjaDao.haeKirja(id).getLuettu().equals("1") && loytyiLuetuista);
        
        kirjaDao.poistaKirja(id);
    }

    @Test
    public void joLuetunVinkinMerkkaaminenLukemattomaksiToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String kirjoittaja = UUID.randomUUID().toString().substring(0, 15);

        kirjaDao.lisaaKirja(kirjoittaja, otsikko, "");

        List<Kirja> lukemattomat = kirjaDao.haeLuettuStatuksenPerusteella("0");

        String id = "";

        for (Kirja kirja : lukemattomat) {
            if (kirja.getKirjoittaja().equals(kirjoittaja) && kirja.getOtsikko().equals(otsikko)) {
                id = kirja.getId();
            }
        }

        mockMvc.perform(get("/" + id + "/onko_luettu")
                .param("id", id));

        List<Kirja> luetut = kirjaDao.haeLuettuStatuksenPerusteella("1");

        boolean kirjaMerkittyLuetuksi = false;

        for (Kirja kirja : luetut) {
            if (kirja.getId().equals(id)
                    && kirja.getOtsikko().equals(otsikko)
                    && kirja.getKirjoittaja().equals(kirjoittaja)) {
                kirjaMerkittyLuetuksi = true;
            }
        }

        assertTrue(kirjaMerkittyLuetuksi);

        // merkitään kirja lukemattomaksi
        mockMvc.perform(get("/" + id + "/onko_luettu")
                .param("id", id));

        lukemattomat = kirjaDao.haeLuettuStatuksenPerusteella("0");
        boolean kirjaLoytyiLukemattomista = false;

        for (Kirja kirja : lukemattomat) {
            if (kirja.getId().equals(id)
                    && kirja.getKirjoittaja().equals(kirjoittaja)
                    && kirja.getOtsikko().equals(otsikko)) {

                kirjaLoytyiLukemattomista = true;
            }
        }

        assertTrue(kirjaDao.haeKirja(id).getLuettu().equals("0") && kirjaLoytyiLukemattomista);
        
        kirjaDao.poistaKirja(id);
    }

    @Test
    public void kirjanPoistoToimii() throws Exception {
        String kirjoittaja = "Herra " + UUID.randomUUID().toString().substring(0, 10);
        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        kirjaDao.lisaaKirja(kirjoittaja, otsikko, "");

        List<Kirja> lukemattomat = kirjaDao.haeLuettuStatuksenPerusteella("0");
        String id = "";

        for (Kirja kirja : lukemattomat) {
            if (kirja.getOtsikko().equals(otsikko) && kirja.getKirjoittaja().equals(kirjoittaja)) {
                id = kirja.getId();
                break;
            }
        }

        mockMvc.perform(post("/" + id + "/poista_kirja")
                .param("id", id));

        boolean loytyi = false;

        for (Kirja kirja : kirjaDao.haeKirjat()) {
            if (kirja.getKirjoittaja().equals(kirjoittaja) && kirja.getOtsikko().equals(otsikko)) {
                loytyi = true;
            }
        }

        assertTrue(!loytyi);
    }
}
