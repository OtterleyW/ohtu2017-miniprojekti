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

@Controller
public class VideoVinkkiController {

    private VideoDao videoDao;

    public VideoVinkkiController() {
        this.videoDao = new VideoDao("jdbc:sqlite:kirjasto.db");
    }

    @GetMapping("/videovinkit")
    public String listaaVideoVinkit(Model model) throws Exception {
        model.addAttribute("videot", videoDao.haeVideot());
        return "videovinkit";
    }

    @GetMapping("/videonlisaaminen")
    public ModelAndView videonlisaaminen() {
        return new ModelAndView("video");
    }
    
    @PostMapping("/lisaavideo")
    @ResponseBody
    public String lisaavideo(@RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "url") String url) {
        Boolean lisatty = false;
        try {
            lisatty = videoDao.lisaaVideo(otsikko, url);
        } catch (Exception ex) {
            return "error";
        }
        if(lisatty){
        return "Lisätty video " + otsikko + " url: " + url +  "<a href='/'>(takaisin)</a>";
        }
           return "Videon nimi tai url ei voi olla tyhjä! <a href='/videonlisaaminen'>(takaisin)</a>";
    }
    
    @GetMapping("/{id}/muokkaavideota")
    public String muokkaaVideota(Model model, @PathVariable String id) throws Exception {
        try {
            Video v = videoDao.haeVideo(id);
            model.addAttribute("video", v);
        } catch (Exception ex) {
            return "error";
        }
        return "muokkaa_videota";
    }
    
    @PostMapping("/{id}/muokkaa_videota")
    @ResponseBody
    public String muokkaaVideotaTietokantaan(@PathVariable String id, @RequestParam(value = "otsikko") String otsikko, @RequestParam(value = "url") String url) {
        Boolean muokattu = false;
        try {
            muokattu = videoDao.muokkaaVideota(id, otsikko, url);
        } catch (Exception ex) {
            return "error";
        }
        if (muokattu) {
            return "Muokattu video " + otsikko + " url " + url + "! " + "<a href='/videovinkit'>(vinkkilistaukseen)</a>";
        }

        return "Videon nimi tai url ei voi olla tyhjä! <a href='/" + id + "/muokkaavideota'>(takaisin)</a>";
    }
    
    @GetMapping("/{id}/poistavideo")
    public String poistaVideoVarmistus(Model model, @PathVariable String id) throws Exception {
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
    public String poistaVideo(@PathVariable String id) throws Exception {

        Video v = videoDao.haeVideo(id);
        try {
            videoDao.poistaVideo(id);
        } catch (Exception ex) {
            return "error";
        }
        return "Poistettu video " + v.getOtsikko() + ", jonka url on " + v.getUrl() + ". <a href='/videovinkit'>(vinkkilistaukseen)</a>";

    }

}
