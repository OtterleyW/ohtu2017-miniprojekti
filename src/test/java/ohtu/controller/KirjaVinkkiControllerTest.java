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
        this.kirjaDao = new KirjaDao("jdbc:sqlite:testitietokanta.db");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void juuripolkuStatusOk() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void vinkitStatusOkJaModelissaVinkit() throws Exception {
        mockMvc.perform(get("/vinkit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("kirjat"));
    }

    @Test
    public void lisattyKirjaLoytyyTietokannasta() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 10);

        mockMvc.perform(post("/lisaakirja")
                .param("kirjoittaja", "Kirjoittava Kirjoittaja")
                .param("otsikko", otsikko));

        MvcResult result = mockMvc.perform(get("/vinkit")).andReturn();

        Collection<Kirja> kirjat = (Collection) result.getModelAndView().getModel().get("kirjat");

        boolean loytyi = false;

        for (Kirja kirja : kirjat) {
            if (kirja.getKirjoittaja().equals("Kirjoittava Kirjoittaja") && kirja.getOtsikko().equals(otsikko)) {
                loytyi = true;
            }
        }

        assertTrue(loytyi);
    }

    @Test
    public void muokkaussivunStatusOk() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 10);

        kirjaDao.lisaaKirja("Olen Kirjailija", otsikko);
        mockMvc.perform(get("/1/muokkaa"))
                .andExpect(status().isOk());
    }

    @Test
    public void kirjanMuokkausToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 10);
        kirjaDao.lisaaKirja("Myös Kirjailija", otsikko);
        String uusiOtsikko = UUID.randomUUID().toString().substring(0, 15);

        List<Kirja> kirjat = kirjaDao.haeKirjat();

        String id = "";

        for (Kirja kirja : kirjat) {
            if (kirja.getKirjoittaja().equals("Myös Kirjailija") && kirja.getOtsikko().equals(otsikko)) {
                id = kirja.getId();
            }
        }

        mockMvc.perform(post("/" + id + "/muokkaa_kirjaa")
                .param("id", id)
                .param("kirjoittaja", "Minua on muokattu! :)")
                .param("otsikko", uusiOtsikko));

        assertTrue(kirjaDao.haeKirja(id).getKirjoittaja().equals("Minua on muokattu! :)")
                && kirjaDao.haeKirja(id).getOtsikko().equals(uusiOtsikko));
    }
}
