package Logic;

import Database.ConnectionDB;
import Database.Reservas;
import Servlets.Reserva;
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
  
public static String getNombre(String email) {
    String nombre = null;
    ConnectionDB Conexion = new ConnectionDB();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = Conexion.obtainConnection(true);
        Log.log.info("Conexión establecida con la base de datos para usuario: " + email);

        String sql = "SELECT nombreApellido FROM biblioteca.Usuarios WHERE email = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, email);

        Log.log.info("Sentencia SQL preparada: " + ps.toString());

        rs = ps.executeQuery();
        if (rs.next()) {
            nombre = rs.getString("nombreApellido");
            Log.log.info("Nombre de usuario encontrado con éxito: " + nombre);
        } else {
            Log.log.info("Nombre no encontrado para el usuario: " + email);
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

    return nombre;
}
  
public static ArrayList<Reservas> getReservasPorEmail(String email) {
    ArrayList<Reservas> reservas = new ArrayList<>();
    ConnectionDB conector = new ConnectionDB();
    Connection con = null;
    
    try {
        con = conector.obtainConnection(true);
        String sql = "SELECT idSala_sala, horaReserva FROM biblioteca.Reservas WHERE email_usuario = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Reservas reserva = new Reservas();
            reserva.setIdSala(rs.getInt("idSala_sala"));
            reserva.setHoraReserva(rs.getTimestamp("horaReserva"));  // Cambié esto para que sea un Date
            reservas.add(reserva);
        }
    } catch (SQLException e) {
        Log.log.error("Error al obtener reservas: " + e);
    } finally {
        conector.closeConnection(con);
    }

    return reservas;
}

public static boolean cancelarReserva(String email, int idSala, Timestamp horaReserva) {
    boolean exito = false;
    ConnectionDB conector = new ConnectionDB();
    Connection con = null;

    try {
        con = conector.obtainConnection(true);
        String sql = "DELETE FROM biblioteca.Reservas WHERE email_usuario = ? AND idSala_sala = ? AND horaReserva = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, idSala);
            ps.setTimestamp(3, horaReserva);
            int filasAfectadas = ps.executeUpdate();
            exito = filasAfectadas > 0;
        }
    } catch (SQLException e) {
        Log.log.error("Error al cancelar reserva: " + e);
    } finally {
        conector.closeConnection(con);
    }

    return exito;
}
public static ArrayList<Reservas> getReservas(String filtroEmail, String filtroFecha) {
    ArrayList<Reservas> reservas = new ArrayList<>();
    ConnectionDB conector = new ConnectionDB();
    Connection con = null;

    try {
        con = conector.obtainConnection(true);
        
        // Construcción de la consulta con los filtros
        StringBuilder sql = new StringBuilder("SELECT idSala_sala, horaReserva, email_usuario FROM biblioteca.Reservas WHERE 1=1 ");
        
        if (filtroEmail != null && !filtroEmail.isEmpty()) {
            sql.append("AND email_usuario LIKE ? ");
        }
        if (filtroFecha != null && !filtroFecha.isEmpty()) {
            sql.append("AND DATE(horaReserva) = ? ");
        }
        
        PreparedStatement ps = con.prepareStatement(sql.toString());
        
        int index = 1;
        if (filtroEmail != null && !filtroEmail.isEmpty()) {
            ps.setString(index++, "%" + filtroEmail + "%");
        }
        if (filtroFecha != null && !filtroFecha.isEmpty()) {
            ps.setString(index++, filtroFecha);  // Asegúrate de que el formato de fecha sea correcto
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Reservas reserva = new Reservas();
            reserva.setIdSala(rs.getInt("idSala_sala"));
            reserva.setHoraReserva(rs.getTimestamp("horaReserva"));
            reserva.setEmailUsuario(rs.getString("email_usuario"));
            reservas.add(reserva);
        }
    } catch (SQLException e) {
        Log.log.error("Error al obtener reservas: " + e);
    } finally {
        conector.closeConnection(con);
    }

    return reservas;
}


}
