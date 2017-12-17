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
import org.springframework.web.bind.annotation.PathVariable;

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
        model.addAttribute("kirjat", kirjaDao.haeLuettuStatuksenPerusteella("0"));
        model.addAttribute("videot", videoDao.haeVideotKatsotunPerusteella("0"));
        model.addAttribute("podcastit", podcastDao.haePodcastitKuunnellunPerusteella("0"));
        
        model.addAttribute("luetut_kirjat", kirjaDao.haeLuettuStatuksenPerusteella("1"));
        model.addAttribute("katsotut_videot", videoDao.haeVideotKatsotunPerusteella("1"));
        model.addAttribute("kuunnellut_podcastit", podcastDao.haePodcastitKuunnellunPerusteella("1"));

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

    @GetMapping("/tagit/{tagi}")
    public String kaikkiVinkitTagilla(Model model, @PathVariable String tagi) throws Exception {
        try {
            model.addAttribute("kirjat", kirjaDao.kaikkiVinkitTagilla(tagi));
            model.addAttribute("videot", videoDao.kaikkiVinkitTagilla(tagi));
            model.addAttribute("podcastit", podcastDao.kaikkiVinkitTagilla(tagi));
        } catch (Exception ex) {
            return "error";
        }
        return "kaikki_vinkit_tagilla";
    }
}
