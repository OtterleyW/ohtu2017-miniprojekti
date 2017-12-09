/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author lmantyla
 */
public class DaoHelper {

    public static void createAndExecuteStatement(String tietokantaosoite, String komento, String... muuttujat) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement(komento);

        int luku = 1;
        while (luku <= muuttujat.length) {
            stmt.setString(luku, muuttujat[luku - 1]);
            luku++;
        }

        stmt.execute();
        stmt.close();
        conn.close();
    }
}
