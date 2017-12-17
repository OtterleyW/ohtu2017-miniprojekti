package ohtu;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KirjaTest {

    public KirjaTest() {
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
    public void kirjaAlustetaanOikein() {

        Kirja k = new Kirja("kirjoittaja", "otsikko", "0", "");
        assertEquals(k.getKirjoittaja(), "kirjoittaja");
        assertEquals(k.getOtsikko(), "otsikko");
    }

    @Test
    public void setKirjoittajaToimii() {

        Kirja k = new Kirja("kirjailija", "kirja", "0", "");
        k.setKirjoittaja("toinen kirjailija");
        assertEquals(k.getKirjoittaja(), "toinen kirjailija");
    }

    @Test
    public void setOtsikkoToimii() {

        Kirja k = new Kirja("kirjailija", "kirja", "0", "");
        k.setOtsikko("miten poistua vimistä");
        assertEquals(k.getOtsikko(), "miten poistua vimistä");
    }

    @Test
    public void setIdToimii() {

        Kirja k = new Kirja("kirjailija", "kirja", "0", "");
        k.setId("1456");
        assertEquals(k.getId(), "1456");
    }
    
    @Test
    public void merkitseLuetuksiToimiiKunKirjavinkkiLukematon() {
        Kirja k = new Kirja("kirjailija", "kirja", "0", "");
        k.merkitseLuetuksi();
        assertEquals(k.getLuettu(), "1");
    }
    
    @Test
    public void merkitseLuetuksiToimiiKunKirjavinkkiLuettu() {
        Kirja k = new Kirja("kirjailija", "kirja", "1", "");
        k.merkitseLuetuksi();
        assertEquals(k.getLuettu(), "0");
    }
    
    @Test public void kirjalleLisataanKuvaus(){
        Kirja k = new Kirja("kirjailija", "kirja", "1", "");
        String kuvaus = "Tämä on kirja";
        k.setKuvaus("Tämä on kirja");
        
        assertEquals(kuvaus, k.getKuvaus());
    }
    

    
}
