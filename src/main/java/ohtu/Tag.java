/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu;

/**
 *
 * @author Jenni
 */
public class Tag {

    private String nimi;
    private String id;

    public Tag(String nimi) {
        this.nimi = nimi;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
}
