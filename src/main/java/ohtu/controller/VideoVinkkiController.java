package ohtu.controller;

import ohtu.Video;
import ohtu.VideoDao;
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
public class VideoVinkkiController {

    private VideoDao videoDao;
    private String viesti; //käyttäjälle näytettävä virheviesti tai ilmoitus

    public VideoVinkkiController() {
        this.videoDao = new VideoDao("jdbc:sqlite:kirjasto.db");
        this.viesti = "";
    }

    @GetMapping("/videovinkit")
    public String listaaVideoVinkit(Model model) throws Exception {
        tarkistaOnkoViestia(model);
        model.addAttribute("videot", videoDao.haeVideotKatsotunPerusteella("0"));
        model.addAttribute("katsotutVideot", videoDao.haeVideotKatsotunPerusteella("1"));
        return "videovinkit";
    }

    @GetMapping("/videonlisaaminen")
    public String videonlisaaminen(Model model) {
        tarkistaOnkoViestia(model);
        return "video";
    }

    @PostMapping("/lisaavideo")
    @ResponseBody
    public RedirectView lisaavideo(@RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "url") String url, @RequestParam(value = "kuvaus") String kuvaus) {
        Boolean lisatty = false;
        try {
            lisatty = videoDao.lisaaVideo(otsikko, url, kuvaus);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        if (lisatty) {
            viesti = "Lisätty video " + otsikko + " url: " + url;
            return new RedirectView("/videovinkit");
        }
        viesti = "Videon nimi tai url ei voi olla tyhjä!";
        return new RedirectView("/videonlisaaminen");
    }

    @GetMapping("/{id}/muokkaavideota")
    public String muokkaaVideota(Model model, @PathVariable String id) throws Exception {
        return videonMuokkaaminen(model, id);
    }

    @GetMapping("/e/{id}/muokkaavideota")
    public String muokkaaVideotaEtusivulta(Model model, @PathVariable String id) throws Exception {
        return videonMuokkaaminen(model, id);
    }

    private String videonMuokkaaminen(Model model, String id) throws Exception {
        try {
            Video v = videoDao.haeVideo(id);
            tarkistaOnkoViestia(model);
            model.addAttribute("video", v);
        } catch (Exception ex) {
            return "error";
        }
        return "muokkaa_videota";
    }

    @GetMapping("/{id}/lisaavideolletagi")
    public String lisaaTagi(Model model, @PathVariable String id) throws Exception {
        return taginLisays(model, id);
    }

    @GetMapping("/e/{id}/lisaavideolletagi")
    public String lisaaTagiEtusivulta(Model model, @PathVariable String id) throws Exception {
        return taginLisays(model, id);
    }

    private String taginLisays(Model model, String id) throws Exception {
        try {
            Video v = videoDao.haeVideo(id);
            model.addAttribute("video", v);
        } catch (Exception ex) {
            return "error";
        }
        return "lisaa_videolle_tagi";
    }

    @PostMapping("/{id}/lisaa_videolle_tagi")
    @ResponseBody
    public RedirectView lisaaTagiTietokantaan(@PathVariable String id, @RequestParam(value = "tagi") String tagi) throws Exception {

        Video v = videoDao.haeVideo(id);
        try {
            videoDao.lisaaTagi(id, tagi);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/video/" + id + "/info");

    }

    @GetMapping("/tagit/{tagi}/video/{video}/poista")
    public RedirectView poistaTagi(@PathVariable String tagi, @PathVariable String video) throws Exception {
        try {
            videoDao.poistaVideoltaTagi(tagi, video);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/video/{video}/info");
    }

    @PostMapping("/e/{id}/lisaa_videolle_tagi")
    @ResponseBody
    public RedirectView lisaaTagiTietokantaanEtusivulta(@PathVariable String id, @RequestParam(value = "tagi") String tagi) throws Exception {

        Video v = videoDao.haeVideo(id);
        try {
            videoDao.lisaaTagi(id, tagi);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        return new RedirectView("/");

    }

    @PostMapping("/{id}/muokkaa_videota")
    @ResponseBody
    public RedirectView muokkaaVideotaTietokantaan(@PathVariable String id, @RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "url") String url, @RequestParam(value = "kuvaus") String kuvaus) {
        Boolean muokattu = false;
        try {
            muokattu = videoDao.muokkaaVideota(id, otsikko, url, kuvaus);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        if (muokattu) {
            viesti = "Muokattu video " + otsikko + " url " + url + "!";
            return new RedirectView("/videovinkit");
        }

        viesti = "Videon nimi tai url ei voi olla tyhjä!";
        return new RedirectView("/" + id + "/muokkaavideota");
    }

    @PostMapping("/e/{id}/muokkaa_videota")
    @ResponseBody
    public RedirectView muokkaaVideotaTietokantaanEtusivulta(@PathVariable String id, @RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "url") String url, @RequestParam(value = "kuvaus") String kuvaus) {
        Boolean muokattu = false;
        try {
            muokattu = videoDao.muokkaaVideota(id, otsikko, url, kuvaus);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        if (muokattu) {
            viesti = "Muokattu video " + otsikko + " url " + url + "!";
            return new RedirectView("/");
        }

        viesti = "Videon nimi tai url ei voi olla tyhjä!";
        return new RedirectView("/" + id + "/muokkaavideota");
    }

    @GetMapping("/{id}/poistavideo")
    public String poistaVideoVarmistus(Model model, @PathVariable String id) throws Exception {
        return videonPoisto(model, id);
    }

    @GetMapping("/e/{id}/poistavideo")
    public String poistaVideoVarmistusEtusivulta(Model model, @PathVariable String id) throws Exception {
        return videonPoisto(model, id);
    }

    private String videonPoisto(Model model, String id) throws Exception {
        try {
            Video v = videoDao.haeVideo(id);
            model.addAttribute("video", v);
        } catch (Exception ex) {
            return "error";
        }
        return "poista_video";
    }

    @PostMapping("/{id}/poista_video")
    @ResponseBody
    public RedirectView poistaVideo(@PathVariable String id) throws Exception {

        Video v = videoDao.haeVideo(id);
        try {
            videoDao.poistaVideo(id);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        viesti = "Poistettu video " + v.getOtsikko() + ", jonka url on " + v.getUrl() + ".";
        return new RedirectView("/videovinkit");
    }

    @PostMapping("/e/{id}/poista_video")
    @ResponseBody
    public RedirectView poistaVideoEtusivulta(@PathVariable String id) throws Exception {

        Video v = videoDao.haeVideo(id);
        try {
            videoDao.poistaVideo(id);
        } catch (Exception ex) {
            return new RedirectView("/error");
        }
        viesti = "Poistettu video " + v.getOtsikko() + ", jonka url on " + v.getUrl() + ".";
        return new RedirectView("/");

    }

    @GetMapping("/video/{id}/info")
    public String naytaInfo(Model model, @PathVariable String id) throws Exception {
        try {
            Video v = videoDao.haeVideo(id);
            model.addAttribute("video", v);
            model.addAttribute("tagit", videoDao.haeTagitVideonIdnPerusteella(id));
        } catch (Exception ex) {
            return "error";
        }
        return "videon_infosivu";
    }

    @GetMapping("/{id}/onko_katsottu")
    public RedirectView merkitseOnkoKatsottu(Model model, @PathVariable String id) throws Exception {
        try {
            muutaOnkoKatsottu(model, id);

        } catch (Exception ex) {
            return new RedirectView("error");
        }
        return new RedirectView("/videovinkit");
    }

    @GetMapping("/e/{id}/onko_katsottu")
    public RedirectView merkitseOnkoKatsottuEtusivulta(Model model, @PathVariable String id) throws Exception {
        try {
            muutaOnkoKatsottu(model, id);

        } catch (Exception ex) {
            return new RedirectView("error");
        }
        return new RedirectView("/");
    }

    private void muutaOnkoKatsottu(Model model, String id) throws Exception {
        Video v = videoDao.haeVideo(id);
        v.merkitseLuetuksi();
        videoDao.muutaOnkoLuettu(v.getLuettu(), id);
    }

    private void tarkistaOnkoViestia(Model model) {
        if (!viesti.isEmpty()) {
            model.addAttribute("viesti", viesti);
            viesti = "";
        }
    }

}
