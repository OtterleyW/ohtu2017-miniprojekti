package ohtu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controllers {

    KirjaDao kirjaDao = new KirjaDao("jdbc:sqlite:kirjasto.db");

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<a href='/other'>linkki</a>";
    }

    @GetMapping("/other")
    @ResponseBody
    public String other() {
        return "Hei Maailma!";
    }

    @GetMapping("/testi")
    @ResponseBody
    public String testi() {

        try {
            kirjaDao.lisaaKirja("testikirja", "testikirjoittaja");
        } catch (Exception e) {

        }
        return "Pöö!";
    }

    @GetMapping("/testi2")
    @ResponseBody
    public String testi2() {

        Kirja k = new Kirja("kuinka selviytyä ohtusta", "jumala");
        try {
            kirjaDao.lisaaKirja(k.getOtsikko(), k.getKirjoittaja());
        } catch (Exception e) {

        }
        return ":D";
    }
}
