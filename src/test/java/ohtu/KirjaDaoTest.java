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

public class KirjaDaoTest {

    KirjaDao kirjaDao;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.kirjaDao = new KirjaDao("jdbc:sqlite:testikanta.db");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void haeKirjatPalauttaaListan() throws Exception {

        List<Kirja> kirjat = kirjaDao.haeKirjat();
        assertTrue(kirjat != null);
    }

    @Test
    public void lisaaKirjaToimii() throws Exception {

        Kirja k = apuLisaaKirjaJaHaeJaPalauta();

        List<Kirja> kirjat = kirjaDao.haeKirjat();
        boolean lisatty = false;
        for (Kirja kirja : kirjat) {
            if (kirja.getKirjoittaja().equals(k.getKirjoittaja()) && kirja.getOtsikko().equals(k.getOtsikko())) {
                lisatty = true;
            }
        }
        assertTrue(lisatty);
        kirjaDao.poistaKirja(k.getId());
    }

    @Test
    public void haeKirjatToimii() throws Exception {

        Kirja k = apuLisaaKirjaJaHaeJaPalauta();
        boolean loytyi = false;

        for (Kirja kirja : this.kirjaDao.haeKirjat()) {
            if (kirja.getOtsikko().equals(k.getOtsikko()) && kirja.getKirjoittaja().equals(k.getKirjoittaja())) {
                loytyi = true;
            }
        }
        assertEquals(true, loytyi);
        kirjaDao.poistaKirja(k.getId());
    }

    @Test
    public void poistaKirjaToimii() throws Exception {

        Kirja k = apuLisaaKirjaJaHaeJaPalauta();
        String id = k.getId();
        kirjaDao.poistaKirja(id);

        List<Kirja> kirjat = kirjaDao.haeKirjat();
        boolean poistettu = true;
        for (Kirja kirja : kirjat) {
            if (kirja.getId().equals(id)) {
                poistettu = false;
            }
        }
        assertTrue(poistettu);
    }

    @Test
    public void haeKirjaToimii() throws Exception {

        Kirja k = apuLisaaKirjaJaHaeJaPalauta();
        Kirja k2 = kirjaDao.haeKirja(k.getId());

        assertEquals(k.getKirjoittaja(), k2.getKirjoittaja());
        assertEquals(k.getOtsikko(), k2.getOtsikko());

        kirjaDao.poistaKirja(k.getId());
    }

    @Test
    public void kirjaaVoiMuokata() throws Exception {

        Kirja k = apuLisaaKirjaJaHaeJaPalauta();
        kirjaDao.muokkaaKirjaa(k.getId(), "jonna", "juo mäfää pelaa täfää", "");

        k = kirjaDao.haeKirja(k.getId());
        assertEquals(k.getKirjoittaja(), "jonna");
        assertEquals(k.getOtsikko(), "juo mäfää pelaa täfää");

        kirjaDao.poistaKirja(k.getId());
    }

    @Test
    public void luettuStatustaVoiMuuttaa() throws Exception {

        Kirja k = apuLisaaKirjaJaHaeJaPalauta();
        kirjaDao.muutaOnkoLuettu("0", k.getId());
        assertEquals(k.getLuettu(), "0");

        kirjaDao.poistaKirja(k.getId());
    }

    @Test
    public void haeLuettujaPalauttaaVainLuettuja() throws Exception {

        apuHaeLuettujaTaiLukemattomiaJaPalauta("1");
    }

    @Test
    public void haeLukemattomiaPalauttaaVainLukemattomia() throws Exception {

        apuHaeLuettujaTaiLukemattomiaJaPalauta("0");
    }

    @Test
    public void validMetodiToimiiKunAnnettaanOtsikkoJaKirjoittaja() throws Exception {
        String otsikko = "Testikirja";
        String kirjoittaja = "Testikirjailija";

        assertEquals(true, this.kirjaDao.lisaaKirja(kirjoittaja, otsikko, ""));
    }

    @Test
    public void validMetodiToimiiKunAnnettaanTyhjaOtsikkoMuttaOikeaKirjoittaja() throws Exception {
        String otsikko = "";
        String kirjoittaja = "Testikirjailija";

        assertEquals(false, this.kirjaDao.lisaaKirja(kirjoittaja, otsikko, ""));
    }

    @Test
    public void validMetodiToimiiKunAnnettaanOikeaOtsikkoMuttaTyhjaKirjoittaja() throws Exception {
        String otsikko = "Testikirja";
        String kirjoittaja = "";

        assertEquals(false, this.kirjaDao.lisaaKirja(kirjoittaja, otsikko, ""));
    }

    private Kirja apuLisaaKirjaJaHaeJaPalauta() throws Exception {

        kirjaDao.lisaaKirja("jonne", "pelaa es juo cs", "");
        List<Kirja> kirjat = kirjaDao.haeKirjat();
        Kirja k = kirjat.get(kirjat.size() - 1);

        return k;
    }

    private void apuHaeLuettujaTaiLukemattomiaJaPalauta(String status) throws Exception {

        List<Kirja> lukemattomat = kirjaDao.haeLuettuStatuksenPerusteella(status);
        boolean kaikkiLuettuTaiEiLuettu = true;
        for (Kirja kirja : lukemattomat) {
            if (!kirja.getLuettu().equals(status)) {
                kaikkiLuettuTaiEiLuettu = false;
            }
        }
        assertTrue(kaikkiLuettuTaiEiLuettu);
    }
}
