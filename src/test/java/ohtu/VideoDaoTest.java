/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

public class VideoDaoTest {

    VideoDao dao;

    public VideoDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

        dao = new VideoDao("jdbc:sqlite:testikanta.db");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void haeVideotPalauttaaListan() throws Exception {

        List<Video> videot = dao.haeVideot();
        assertTrue(videot != null);
    }

    @Test
    public void videonVoiLisata() throws Exception {

        String otsikko = UUID.randomUUID().toString().substring(0, 15);
        String url = UUID.randomUUID().toString().substring(0, 15);

        Video v = new Video(otsikko, url, "0", "");
        dao.lisaaVideo(otsikko, url, "");
        List<Video> videot = dao.haeVideot();

        boolean lisatty = false;

        for (Video video : videot) {
            if (video.getOtsikko().equals(otsikko) && video.getUrl().equals("http://" + url)) {
                lisatty = true;
            }
        }

        assertTrue(lisatty);
    }

    @Test
    public void videonVoiHakea() throws Exception {

        List<Video> videot = dao.haeVideot();
        if (videot.size() > 0) {
            Video v = videot.get(0);
            String id = v.getId();

            Video v2 = dao.haeVideo(id);
            assertEquals(v.getOtsikko(), v2.getOtsikko());
            assertEquals(v.getUrl(), v2.getUrl());
        }
    }

    @Test
    public void poistaVideoToimii() throws Exception {

        dao.lisaaVideo("otsikko", "www.com", "");
        List<Video> videot = dao.haeVideot();

        Video v = videot.get(videot.size() - 1);
        String id = v.getId();

        dao.poistaVideo(id);
        videot = dao.haeVideot();
        boolean poistettu = true;

        for (Video video : videot) {
            if (video.getId().equals(id)) {
                poistettu = false;
            }
        }
        assertTrue(poistettu);

    }

    @Test
    public void muokkaaVideotaToimii() throws Exception {
        dao.lisaaVideo("video", "xyz.com", "");
        List<Video> videot = dao.haeVideot();
        Video v = videot.get(videot.size() - 1);

        String id = v.getId();
        dao.muokkaaVideota(id, "uusi nimi", "joku url", "kuvaus");
        v = dao.haeVideo(id);

        assertEquals(v.getOtsikko(), "uusi nimi");
        assertEquals(v.getUrl(), "http://joku url");

        dao.poistaVideo(id);
    }

    @Test
    public void voiMerkitaLuetuksiTaiLukemattomaksi() throws Exception {

        dao.lisaaVideo("platypus", "sieni.es", "");
        List<Video> videot = dao.haeVideot();
        Video v = videot.get(videot.size() - 1);

        String id = v.getId();

        dao.muutaOnkoLuettu("0", id);
        assertEquals("0", dao.haeVideo(id).getLuettu());
        dao.muutaOnkoLuettu("1", id);
        assertEquals("1", dao.haeVideo(id).getLuettu());

        dao.poistaVideo(id);
    }

    @Test
    public void josOtsikkoTaiUrlTyhjaEiLisata() throws Exception {

        assertFalse(dao.lisaaVideo("", "tekijä", ""));
        assertFalse(dao.lisaaVideo("nimi", "", ""));
        assertFalse(dao.lisaaVideo("", "", ""));
    }

    @Test
    public void josOtsikkoTaiUrlTyhjaEiMuokata() throws Exception {

        dao.lisaaVideo("hauska meemivideo", "sieni.us", "");
        List<Video> videot = dao.haeVideot();
        Video v = videot.get(videot.size() - 1);

        assertFalse(dao.muokkaaVideota(v.getId(), "", "url", ""));
        assertFalse(dao.muokkaaVideota(v.getId(), "otsikko", "", ""));
        assertFalse(dao.muokkaaVideota(v.getId(), "", "", ""));
    }
    
    @Test
    public void validMetodiToimiiJosSyoteNull() throws Exception {
        String nimi = null;
        String url = "urli";
        
        String nimi2 = "nimi";
        String url2 = null;

        assertEquals(false, dao.lisaaVideo(nimi, url, ""));
        assertEquals(false, dao.lisaaVideo(nimi2, url2, ""));
    }

    @Test
    public void hakusanallaLoytyyVideo() throws Exception {
        List<Video> videot = new ArrayList();
        String nimi = "Haettava video";
        String url = "www.haku.url";

        dao.lisaaVideo(nimi, url, "");

        videot = dao.haeHakusanaaVastaavat("Haettava");

        assertEquals(false, videot.isEmpty());
    }

}
