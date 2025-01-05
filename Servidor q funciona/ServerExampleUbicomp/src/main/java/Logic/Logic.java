package Logic;

import Database.ConnectionDB;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Logic {

    public static ArrayList<Measurement> getDataFromDB() {
        ArrayList<Measurement> values = new ArrayList<Measurement>();

        ConnectionDB conector = new ConnectionDB();
        Connection con = null;
        try {
            con = conector.obtainConnection(true);
            Log.log.info("Database Connected");

            PreparedStatement ps = ConnectionDB.GetDataBD(con);
            Log.log.info("Query=>" + ps.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Measurement measure = new Measurement();
                measure.setValue(rs.getInt("VALUE"));
                measure.setDate(rs.getTimestamp("DATE"));
                values.add(measure);
            }
        } catch (SQLException e) {
            Log.log.error("Error: " + e);
            values = new ArrayList<Measurement>();
        } catch (NullPointerException e) {
            Log.log.error("Error: " + e);
            values = new ArrayList<Measurement>();
        } catch (Exception e) {
            Log.log.error("Error:" + e);
            values = new ArrayList<Measurement>();
        }
        conector.closeConnection(con);
        return values;
    }

    public static ArrayList<Measurement> setDataToDB(int value) {
        ArrayList<Measurement> values = new ArrayList<Measurement>();

        ConnectionDB conector = new ConnectionDB();
        Connection con = null;
        try {
            con = conector.obtainConnection(true);
            Log.log.info("Database Connected");

            PreparedStatement ps = ConnectionDB.SetDataBD(con);
            ps.setInt(1, value);
            ps.setTimestamp(2, new Timestamp((new Date()).getTime()));
            Log.log.info("Query=>" + ps.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Log.log.error("Error: " + e);
            values = new ArrayList<Measurement>();
        } catch (NullPointerException e) {
            Log.log.error("Error: " + e);
            values = new ArrayList<Measurement>();
        } catch (Exception e) {
            Log.log.error("Error:" + e);
            values = new ArrayList<Measurement>();
        }
        conector.closeConnection(con);
        return values;
    }

    // ConnectionDB
    public static boolean ConnectionDB(String username, String password) {
        boolean conectado = false;
        ConnectionDB conector = new ConnectionDB();
        Connection con = null;
        try {
            con = conector.obtainConnection(true);
            Log.log.info("Conexion establecida con la base de datos");

            PreparedStatement ps = ConnectionDB.ConnectionDB(con);
            ps.setString(1, username);
            ps.setString(2, password);

            Log.log.info("Sentencia SQL=>" + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                conectado = true;
            }
        } catch (SQLException e) {
            Log.log.error("Error: " + e);
        } catch (NullPointerException e) {
            Log.log.error("Error: " + e);
        } catch (Exception e) {
            Log.log.error("Error:" + e);
        }
        conector.closeConnection(con);
        return conectado;
    }

  public static String authenticateUser(String email, String password) {
    String userType = null;
    ConnectionDB Conexion = new ConnectionDB();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = Conexion.obtainConnection(true);
        Log.log.info("Conexión establecida con la base de datos para usuario: " + email);

        String sql = "SELECT tipo FROM biblioteca.Usuarios WHERE email = ? AND passw = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, password);

        Log.log.info("Sentencia SQL preparada: " + ps.toString());

        rs = ps.executeQuery();
        if (rs.next()) {
            userType = rs.getString("tipo");
            Log.log.info("Usuario autenticado con éxito. Tipo: " + userType);
        } else {
            Log.log.info("Credenciales incorrectas para el usuario: " + email);
        }
    } catch (SQLException e) {
        Log.log.error("Error SQL: " + e);
    } catch (NullPointerException e) {
        Log.log.error("Error de puntero nulo al autenticar: " + e);
    } catch (Exception e) {
        Log.log.error("Error general: " + e);
    } finally {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
            if (con != null) Conexion.closeConnection(con);
        } catch (SQLException e) {
            Log.log.error("Error al cerrar recursos: " + e);
        }
    }

    return userType;
}

}
