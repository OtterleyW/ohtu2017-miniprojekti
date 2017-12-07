/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jenni
 */
public class TagDao {

    private String tietokantaosoite;

    public TagDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public List<String> lisaaTagit(List<String> tagilista) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Kirja");
        
        List<String> tagi_idt = new ArrayList<String>();

        for (String tagi : tagilista) {
            boolean loytyyko = false;
            String tagi_id = "";
            while (rs.next()) {
                if (rs.getString("name").equals(tagi)) {
                    loytyyko = true;
                    tagi_id = rs.getString("id");
                }
            }
            
            if(!loytyyko){
                tagi_id = lisaaTagi(tagi);
            }
            
            tagi_idt.add(tagi_id);
        }
    

    rs.close ();

    conn.close ();
    
    return tagi_idt;
}

public String lisaaTagi(String nimi) throws Exception{
        Tag tagi = new Tag(nimi);
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Tag(name) "
                + "VALUES ( ? )");
        stmt.setString(1, tagi.getNimi());
        stmt.execute();
        stmt.close();
        conn.close();
        
        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating tag failed, no rows affected.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                tagi.setId(String.valueOf(generatedKeys.getLong(1)));
            } else {
                throw new SQLException("Creating tag failed, no ID obtained.");
            }
        }
        
        return tagi.getId();
    }
    
    public Tag haeTagi(String id) throws Exception{
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tag WHERE id = ?");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        Tag tagi = new Tag(rs.getString("nimi"));
        tagi.setId(id);

        rs.close();
        stmt.close();
        conn.close();

        return tagi;
    }
    
    public List<Tag> haeTagit() throws Exception {
        List<Tag> tagit = new ArrayList();

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Tag");

        while (rs.next()) {
            String id = rs.getString("id");
            String nimi = rs.getString("name");

            Tag t = new Tag(nimi);
            t.setId(id);

            tagit.add(t);
        }

        rs.close();
        conn.close();

        return tagit;
    }

}
