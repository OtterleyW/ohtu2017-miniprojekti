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
            kirjaDao.lisaaKirja("kirjoittaja", "kirja");
        } catch (Exception e) {

        }
        return ":D";
    }
}
