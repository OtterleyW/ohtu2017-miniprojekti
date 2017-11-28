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
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class KirjaVinkkiController {

    private KirjaDao kirjaDao;

    public KirjaVinkkiController() {
        this.kirjaDao = new KirjaDao("jdbc:sqlite:kirjasto.db");
    }
    
    public KirjaVinkkiController(String tietokantaosoite) {
        this.kirjaDao = new KirjaDao("jdbc:sqlite:testitietokanta.db");
    }

    @GetMapping("/vinkit")
    public String listaaVinkit(Model model) throws Exception {
        model.addAttribute("kirjat", kirjaDao.haeKirjat());
        return "vinkit";
    }

    @GetMapping("/")
    public String home() {
        return "index";
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
            return "error";
        }
        return "Lisätty kirja " + otsikko + " kirjoittajalta " + kirjoittaja + "! " + "<a href='/'>(takaisin)</a>";
    }

    @GetMapping("/{id}/muokkaa")
    public String muokkaaKirjaa(Model model, @PathVariable String id) throws Exception {
        try {
            Kirja k = kirjaDao.haeKirja(id);
            model.addAttribute("kirja", k);
        } catch (Exception ex) {
            return "error";
        }
        return "muokkaa_kirjaa";
    }

    @GetMapping("/{id}/poista")
    public String poistaKirjaVarmistus(Model model, @PathVariable String id) throws Exception {
        try {
            Kirja k = kirjaDao.haeKirja(id);
            model.addAttribute("kirja", k);
        } catch (Exception ex) {
            return "error";
        }
        return "poista_kirja";
    }

    @PostMapping("/{id}/poista_kirja")
    @ResponseBody
    public String poistaKirja(@PathVariable String id) throws Exception {

        Kirja k = kirjaDao.haeKirja(id);
        try {
            kirjaDao.poistaKirja(id);
        } catch (Exception ex) {
            return "error";
        }
        return "Poistettu kirja " + k.getOtsikko() + " kirjoittajalta " + k.getKirjoittaja() + ". <a href='/vinkit'>(vinkkilistaukseen)</a>";

    }

    @PostMapping("/{id}/muokkaa_kirjaa")
    @ResponseBody
    public String muokkaaKirjaa(@PathVariable String id, @RequestParam(value = "kirjoittaja") String kirjoittaja, @RequestParam(value = "otsikko") String otsikko) {

        try {
            kirjaDao.muokkaaKirjaa(id, kirjoittaja, otsikko);
        } catch (Exception ex) {
            return "error";
        }
        return "Muokattu kirja " + otsikko + " kirjoittajalta " + kirjoittaja + "! " + "<a href='/vinkit'>(vinkkilistaukseen)</a>";
    }

    @GetMapping("/{id}/onko_luettu")
    public RedirectView merkitseOnkoLuettu(Model model, @PathVariable String id) throws Exception {
        try {
            Kirja k = kirjaDao.haeKirja(id);
            k.merkitseLuetuksi();
            kirjaDao.muutaOnkoLuettu(k.getLuettu(), id);

        } catch (Exception ex) {
            return new RedirectView("error");
        }
        return new RedirectView("/vinkit");
    }
}
