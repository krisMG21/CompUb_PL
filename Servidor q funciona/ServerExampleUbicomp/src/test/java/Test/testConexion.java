package Test;

import Database.ConnectionDB;
import java.sql.Connection;

public class testConexion {
    public static void main(String[] args) {
        ConnectionDB db = new ConnectionDB();
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
