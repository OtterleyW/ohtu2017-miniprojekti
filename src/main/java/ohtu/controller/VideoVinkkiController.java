package ohtu.controller;

import ohtu.VideoDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

}
