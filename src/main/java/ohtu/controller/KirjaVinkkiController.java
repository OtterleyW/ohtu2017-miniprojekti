package ohtu.controller;

import ohtu.Kirja;
import ohtu.KirjaDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class KirjaVinkkiController {

    private KirjaDao kirjaDao;

    public KirjaVinkkiController() {
        this.kirjaDao = new KirjaDao("jdbc:sqlite:kirjasto.db");
    }

    @GetMapping("/vinkit")
    public String listaaVinkit(Model model) throws Exception {
        model.addAttribute("kirjat", kirjaDao.haeKirjat());
        return "vinkit";
    }

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<a href='/testi'>Lisää kirja</a> " + "<a href='/vinkit'>Listaa kirjavinkit</a> ";
    }

    @GetMapping("/testi")
    public ModelAndView testi() {
        return new ModelAndView("kirja");
    }

    @PostMapping("/lisaakirja")
    @ResponseBody
    public String lisaakirja(@RequestParam(value = "kirjoittaja") String kirjoittaja, @RequestParam(value = "otsikko") String otsikko) {

        try {
            kirjaDao.lisaaKirja(kirjoittaja, otsikko);
        } catch (Exception ex) {
            return "ERRRRRROR! " + "<a href='/testi'>Takaisin</a>";
        }

        return "Lisätty kirja " + otsikko + " kirjoittajalta " + kirjoittaja + "! " + "<a href='/testi'>Takaisin</a>";
    }

    @GetMapping("/{id}/muokkaa")
    public String muokkaaKirjaa(Model model, @PathVariable String id) throws Exception {
        Kirja k = kirjaDao.haeKirja(id);
        model.addAttribute("kirja", k);
        System.out.println(k);
        return "muokkaa_kirjaa";
    }

    @PostMapping("/{id}/muokkaa_kirjaa")
    @ResponseBody
    public String muokkaaKirjaa(@PathVariable String id, @RequestParam(value = "kirjoittaja") String kirjoittaja, @RequestParam(value = "otsikko") String otsikko) {

        try {
            kirjaDao.muokkaaKirjaa(id, kirjoittaja, otsikko);
        } catch (Exception ex) {
            return "ERRRRRROR! " + "<a href='/'>Takaisin</a>";
        }

        return "Muokattu kirja " + otsikko + " kirjoittajalta " + kirjoittaja + "! " + "<a href='/'>Takaisin</a>";
    }
}
