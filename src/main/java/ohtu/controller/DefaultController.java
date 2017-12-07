package ohtu.controller;

import ohtu.KirjaDao;
import ohtu.VideoDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import ohtu.Kirja;
import ohtu.PodcastDao;

@Controller
public class DefaultController {
    
    private KirjaDao kirjaDao;
    private VideoDao videoDao;
    private PodcastDao podcastDao;
    
    public DefaultController() {
        this.kirjaDao = new KirjaDao("jdbc:sqlite:kirjasto.db");
        this.videoDao = new VideoDao("jdbc:sqlite:kirjasto.db");
        this.podcastDao = new PodcastDao("jdbc:sqlite:kirjasto.db");
    }

    @GetMapping("/")
    public String naytaPaasivu(Model model) throws Exception {
        model.addAttribute("kirjat", kirjaDao.haeKirjat());
        model.addAttribute("videot", videoDao.haeVideot());
        model.addAttribute("podcastit", podcastDao.haePodcastit());

        return "index";
    }

    @PostMapping("/")
    public String haeVinkeista(@RequestParam String hakusana, Model model) throws Exception {
        if (hakusana == null || hakusana.trim().isEmpty()) {
            return "redirect:/";
        }
        
        model.addAttribute("kirjat", kirjaDao.haeHakusanaaVastaavat(hakusana));
        model.addAttribute("videot", videoDao.haeHakusanaaVastaavat(hakusana));
        model.addAttribute("podcastit", podcastDao.haeHakusanaaVastaavat(hakusana));

        return "index";
    }
}
