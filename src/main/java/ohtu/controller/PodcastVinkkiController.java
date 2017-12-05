
package ohtu.controller;

import ohtu.PodcastDao;
import ohtu.VideoDao;
import org.springframework.stereotype.Controller;

@Controller
public class PodcastVinkkiController {
    
    private PodcastDao podcastDao; 
    private String viesti; //käyttäjälle näytettävä virheviesti tai ilmoitus

    public PodcastVinkkiController() {
        this.podcastDao = new PodcastDao("jdbc:sqlite:kirjasto.db");
        this.viesti = "";
    }
}
