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
    private String viesti; //käyttäjälle näytettävä virheviesti tai muu ilmoitus

    public KirjaVinkkiController() {
        this.kirjaDao = new KirjaDao("jdbc:sqlite:kirjasto.db");
        this.viesti = "";
    }

    @GetMapping("/vinkit")
    public String listaaVinkit(Model model) throws Exception {
        //model.addAttribute("kirjat", kirjaDao.haeKirjat());
        tarkistaOnkoViestia(model);
        model.addAttribute("lukemattomat", kirjaDao.haeLuettuStatuksenPerusteella("0"));
        model.addAttribute("luetut", kirjaDao.haeLuettuStatuksenPerusteella("1"));
        return "vinkit";
    }

//    @GetMapping("/")
//    public String home() {
//        return "index";
//    }
    @GetMapping("/testi")
    public String testi(Model model) {
        tarkistaOnkoViestia(model);
        return "kirja";
    }

    @PostMapping("/lisaakirja")
    @ResponseBody
    public RedirectView lisaakirja(@RequestParam(value = "kirjoittaja") String kirjoittaja, @RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "kuvaus") String kuvaus) {
        Boolean lisatty = false;
        try {
            lisatty = kirjaDao.lisaaKirja(kirjoittaja, otsikko, kuvaus);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        if (lisatty) {
            viesti = "Lisätty kirja " + otsikko + " kirjoittajalta " + kirjoittaja + "!";
            return new RedirectView("/vinkit");
        }

        viesti = "Kirjan nimi tai kirjailija ei voi olla tyhjä!";
        return new RedirectView("/testi");
    }

    @GetMapping("/{id}/muokkaa")
    public String muokkaaKirjaa(Model model, @PathVariable String id) throws Exception {
        return kirjanMuokkaus(model, id);
    }

    @GetMapping("/e/{id}/muokkaa")
    public String muokkaaKirjaaEtusivulta(Model model, @PathVariable String id) throws Exception {
        return kirjanMuokkaus(model, id);
    }

    private String kirjanMuokkaus(Model model, String id) throws Exception {
        try {
            Kirja k = kirjaDao.haeKirja(id);
            tarkistaOnkoViestia(model);
            model.addAttribute("kirja", k);
        } catch (Exception ex) {
            return "error";
        }
        return "muokkaa_kirjaa";
    }

    @GetMapping("/{id}/poista")
    public String poistaKirjaVarmistus(Model model, @PathVariable String id) throws Exception {
        return kirjanPoisto(model, id);
    }

    @GetMapping("/e/{id}/poista")
    public String poistaKirjaVarmistusEtusivulta(Model model, @PathVariable String id) throws Exception {
        return kirjanPoisto(model, id);
    }

    private String kirjanPoisto(Model model, String id) throws Exception {
        try {
            Kirja k = kirjaDao.haeKirja(id);
            model.addAttribute("kirja", k);
        } catch (Exception ex) {
            return "error";
        }
        return "poista_kirja";
    }

    @GetMapping("/kirja/{id}/info")
    public String naytaInfo(Model model, @PathVariable String id) throws Exception {
        try {
            Kirja k = kirjaDao.haeKirja(id);
            model.addAttribute("kirja", k);
            model.addAttribute("tagit", kirjaDao.haeTagitKirjanIdnPerusteella(id));
        } catch (Exception ex) {
            return "error";
        }
        return "kirjan_infosivu";
    }

    @GetMapping("/{id}/lisaatagi")
    public String lisaaTagi(Model model, @PathVariable String id) throws Exception {
        return taginLisays(model, id);
    }

    @GetMapping("/e/{id}/lisaatagi")
    public String lisaaTagiEtusivulta(Model model, @PathVariable String id) throws Exception {
        return taginLisays(model, id);
    }

    private String taginLisays(Model model, String id) {
        try {
            Kirja k = kirjaDao.haeKirja(id);
            model.addAttribute("kirja", k);
        } catch (Exception ex) {
            return "error";
        }
        return "lisaa_kirjalle_tagi";
    }

    @PostMapping("/{id}/lisaa_kirjalle_tagi")
    @ResponseBody
    public RedirectView lisaaTagiTietokantaan(@PathVariable String id, @RequestParam(value = "tagi") String tagi) throws Exception {

        Kirja k = kirjaDao.haeKirja(id);
        try {
            kirjaDao.lisaaTagi(id, tagi);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/kirja/" + id + "/info");

    }

    @PostMapping("/e/{id}/lisaa_kirjalle_tagi")
    @ResponseBody
    public RedirectView lisaaTagiTietokantaanEtusivulta(@PathVariable String id, @RequestParam(value = "tagi") String tagi) throws Exception {

        Kirja k = kirjaDao.haeKirja(id);
        try {
            kirjaDao.lisaaTagi(id, tagi);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/");

    }

    @PostMapping("/{id}/poista_kirja")
    @ResponseBody
    public RedirectView poistaKirja(@PathVariable String id) throws Exception {
        Kirja k = kirjaDao.haeKirja(id);
        try {
            kirjaDao.poistaKirja(id);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        viesti = "Poistettu kirja " + k.getOtsikko() + " kirjoittajalta " + k.getKirjoittaja() + "!";
        return new RedirectView("/vinkit");

    }

    @PostMapping("/e/{id}/poista_kirja")
    @ResponseBody
    public RedirectView poistaKirjaEtusivulta(@PathVariable String id) throws Exception {

        Kirja k = kirjaDao.haeKirja(id);
        try {
            kirjaDao.poistaKirja(id);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        viesti = "Poistettu kirja " + k.getOtsikko() + " kirjoittajalta " + k.getKirjoittaja() + "!";
        return new RedirectView("/");

    }

    @PostMapping("/{id}/muokkaa_kirjaa")
    @ResponseBody
    public RedirectView muokkaaKirjaa(@PathVariable String id, @RequestParam(value = "kirjoittaja") String kirjoittaja, @RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "kuvaus") String kuvaus) {
        Boolean muokattu = false;
        try {
            muokattu = kirjaDao.muokkaaKirjaa(id, kirjoittaja, otsikko, kuvaus);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        if (muokattu) {
            viesti = "Muokattu kirja " + otsikko + " kirjoittajalta " + kirjoittaja + "!";
            return new RedirectView("/vinkit");
        }

        viesti = "Kirjan nimi tai kirjailija ei voi olla tyhjä!";
        return new RedirectView("/" + id + "/muokkaa");
    }

    @PostMapping("/e/{id}/muokkaa_kirjaa")
    @ResponseBody
    public RedirectView muokkaaKirjaaEtusivulta(@PathVariable String id, @RequestParam(value = "kirjoittaja") String kirjoittaja, @RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "kuvaus") String kuvaus) {
        Boolean muokattu = false;
        try {
            muokattu = kirjaDao.muokkaaKirjaa(id, kirjoittaja, otsikko, kuvaus);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }

        if (muokattu) {
            viesti = "Muokattu kirja " + otsikko + " kirjoittajalta " + kirjoittaja + "!";
            return new RedirectView("/");
        }

        viesti = "Kirjan nimi tai kirjailija ei voi olla tyhjä!";
        return new RedirectView("/" + id + "/muokkaa");
    }

    @GetMapping("/{id}/onko_luettu")
    public RedirectView merkitseOnkoLuettu(Model model, @PathVariable String id) throws Exception {
        try {
            muutaOnkoLuettu(id);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/vinkit");
    }

    @GetMapping("/tagit/{tagi}/kirja/{kirja}/poista")
    public RedirectView poistaTagi(@PathVariable String tagi, @PathVariable String kirja) throws Exception {
        try {
            kirjaDao.poistaKirjaltaTagi(tagi, kirja);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/kirja/{kirja}/info");
    }

    @GetMapping("/e/{id}/onko_luettu")
    public RedirectView merkitseOnkoLuettuEtusivulta(Model model, @PathVariable String id) throws Exception {
        try {
            muutaOnkoLuettu(id);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/");
    }

    private void tarkistaOnkoViestia(Model model) {
        if (!viesti.isEmpty()) {
            model.addAttribute("viesti", viesti);
            viesti = "";
        }
    }

    private void muutaOnkoLuettu(String id) throws Exception {
        Kirja k = kirjaDao.haeKirja(id);
        k.merkitseLuetuksi();
        kirjaDao.muutaOnkoLuettu(k.getLuettu(), id);
    }
}
