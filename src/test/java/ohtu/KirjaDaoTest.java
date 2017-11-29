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
    public void haeKirjatToimii() throws Exception {
        String otsikko = UUID.randomUUID().toString().substring(0, 10);
        this.kirjaDao.lisaaKirja("kalevi", otsikko);
        boolean loytyi = false;

        for (Kirja kirja : this.kirjaDao.haeKirjat()) {
            if (kirja.getOtsikko().equals(otsikko)) {
                loytyi = true;
            }
        }

        assertEquals(true, loytyi);

    }
    
    @Test
    public void validMetodiToimiiKunAnnettaanOtsikkoJaKirjoittaja() throws Exception {
        String otsikko = "Testikirja";
        String kirjoittaja = "Testikirjailija";
        
       assertEquals(true, this.kirjaDao.lisaaKirja(kirjoittaja, otsikko));
    }
    
    @Test
    public void validMetodiToimiiKunAnnettaanTyhjaOtsikkoMuttaOikeaKirjoittaja() throws Exception {
        String otsikko = "";
        String kirjoittaja = "Testikirjailija";

        assertEquals(false, this.kirjaDao.lisaaKirja(kirjoittaja, otsikko));
    }
    
    @Test
    public void validMetodiToimiiKunAnnettaanOikeaOtsikkoMuttaTyhjaKirjoittaja() throws Exception {
        String otsikko = "Testikirja";
        String kirjoittaja = "";

        assertEquals(false, this.kirjaDao.lisaaKirja(kirjoittaja, otsikko));
    }


}
