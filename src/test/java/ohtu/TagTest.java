package ohtu;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    public TagTest() {
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
    public void tagiAlustetaanOikein() {

        Tag t = new Tag("tagi");
        assertEquals(t.getNimi(), "tagi");
    }

    @Test
    public void tagilleAsetetaanId() {
        Tag t = new Tag("");
        t.setId("1");
        
        assertEquals(t.getId(), "1");

    }

    @Test
    public void tagilleAsetetaanNimi() {
        Tag t = new Tag("");
        t.setNimi("nimi");

        assertEquals(t.getNimi(), "nimi");

    }

}
