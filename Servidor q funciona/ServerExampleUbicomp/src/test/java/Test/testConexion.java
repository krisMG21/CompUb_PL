package Test;

import Database.ConectionDDBB;
import java.sql.Connection;

public class testConexion {
    public static void main(String[] args) {
        ConectionDDBB db = new ConectionDDBB();
        try (Connection con = db.obtainConnection(false)) {
            if (con != null) {
                System.out.println("Connection successful: " + con);
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
