/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pebscina
 */
public class VideoTest {

    public VideoTest() {
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

        Video v = new Video("otsikko", "www.com", "0", "");
        assertEquals(v.getOtsikko(), "otsikko");
        assertEquals(v.getUrl(), "www.com");
        assertEquals(v.getLuettu(), "0");
    }

    @Test
    public void setOtsikkoToimii() {

        Video v = new Video("otsikko", "www.com", "0", "");
        v.setOtsikko("parempi otsikko");
        assertEquals(v.getOtsikko(), "parempi otsikko");
    }

    @Test
    public void setIdToimii() {

        Video v = new Video("otsikko", "www.com", "0", "");
        assertEquals(null, v.getId());
        v.setId("321");
        assertEquals(v.getId(), "321");
    }

    @Test
    public void setUrlToimii() {

        Video v = new Video("how to count to 10", "youtube.com", "1", "");
        v.setUrl("new url");
        assertEquals(v.getUrl(), "new url");
    }

    @Test
    public void getLuettuToimii() {

        Video v = new Video("how to count to 10", "youtube.com", "1", "");
        assertEquals(v.getLuettu(), "1");
    }

    @Test
    public void setLuettuToimii() {

        Video v = new Video("how to count to 10", "youtube.com", "1", "");
        v.setLuettu("0");
        assertEquals(v.getLuettu(), "0");
    }
    
    @Test
    public void merkitseLuetuksiToimii() {
        
        Video v = new Video("mwahahaha", "youtube.com", "0", "");
        v.merkitseLuetuksi();
        assertEquals(v.getLuettu(), "1");
        v.merkitseLuetuksi();
        assertEquals(v.getLuettu(), "0");
    }
}
