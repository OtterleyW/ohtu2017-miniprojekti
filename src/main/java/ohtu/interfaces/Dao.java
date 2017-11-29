package ohtu.interfaces;

import java.util.List;

public interface Dao {
    
    Vinkki haeYksi(String id);
    List<Vinkki> haeKaikki();
    
}
