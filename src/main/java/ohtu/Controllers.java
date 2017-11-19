package ohtu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView testi() {

        return new ModelAndView("kirja");
    }
}
