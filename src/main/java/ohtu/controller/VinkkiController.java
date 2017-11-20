package ohtu.controller;

import ohtu.KirjaDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VinkkiController {

    private KirjaDao kirjaDao;

    @GetMapping("/vinkit")
    public String listaaVinkit(Model model) throws Exception {
        model.addAttribute("kirjat", kirjaDao.haeKirjat());
        return "vinkit";
    }
}
