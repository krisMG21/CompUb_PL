package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import java.util.logging.Level;
import java.util.logging.Logger;

import logic.Log;

public class ConnectionDB {

    public Connection obtainConnection(boolean autoCommit) throws NullPointerException {
        /*Connection con = null;
        int intentos = 5;
        for (int i = 0; i < intentos; i++) {
            Log.logdb.info("Attempt {} to connect to the database", i);
            try {
                Context ctx = new InitialContext();
                // Get the connection factory configured in Tomcat
                DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/ubica");

                // Obtiene una conexion
                con = ds.getConnection();
                Calendar calendar = Calendar.getInstance();
                java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
                Log.logdb.debug("Connection creation. Bd connection identifier: {} obtained in {}", con.toString(), date.toString());
                con.setAutoCommit(autoCommit);
                Log.logdb.info("Conection obtained in the attempt: " + i);
                i = intentos;
            } catch (NamingException ex) {
                Log.logdb.error("Error getting connection while trying: {} = {}", i, ex);
            } catch (SQLException ex) {
                Log.logdb.error("ERROR sql getting connection while trying:{ }= {}", i, ex);
                throw (new NullPointerException("SQL connection is null"));
            }
        }*/
        // Establecer la conexi n con la base de datos
            Connection con = null;
            String url = "jdbc:mariadb://192.168.168.250:3306/biblioteca"; //Cambiar ip, usando la de la VM
            String usuario = "ubicua"; 
            String contrasena = "ubicua";

            try {
                con = DriverManager.getConnection(url, usuario, contrasena);
                con.setAutoCommit(autoCommit);
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            }

        return con;
    }

    public void closeTransaction(Connection con) {
        try {
            con.commit();
            Log.logdb.debug("Transaction closed");
        } catch (SQLException ex) {
            Log.logdb.error("Error closing the transaction: {}", ex);
        }
    }

    public void cancelTransaction(Connection con) {
        try {
            con.rollback();
            Log.logdb.debug("Transaction canceled");
        } catch (SQLException ex) {
            Log.logdb.error("ERROR sql when canceling the transation: {}", ex);
        }
    }

    public void closeConnection(Connection con) {
        try {
            Log.logdb.info("Closing the connection");
            if (null != con) {
                Calendar calendar = Calendar.getInstance();
                java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
                Log.logdb.debug("Connection closed. Bd connection identifier: {} obtained in {}", con.toString(), date.toString());
                con.close();
            }

            Log.logdb.info("The connection has been closed");
        } catch (SQLException e) {
            Log.logdb.error("ERROR sql closing the connection: {}", e);
            e.printStackTrace();
        }
    }

    public static PreparedStatement getStatement(Connection con, String sql) {
        PreparedStatement ps = null;
        try {
            if (con != null) {
                ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            }
        } catch (SQLException ex) {
            Log.logdb.warn("ERROR sql creating PreparedStatement:{} ", ex);
        }

        return ps;
    }
    
    // ESTO DE AQUI LO UTILIZA LOGIC; NO SE QUE COÑO HACE LOGIC
    public static PreparedStatement GetDataBD(Connection con)
    {
    	return getStatement(con,"SELECT * FROM UBICOMP.MEASUREMENT");  	
    }
    
    public static PreparedStatement SetDataBD(Connection con)
    {
    	return getStatement(con,"INSERT INTO UBICOMP.MEASUREMENT VALUES (?,?)");  	
    }

    //************** CALLS TO THE DATABASE ***************************//

    public static PreparedStatement GetTarjetaFromUsuario(Connection con) {
        return getStatement(con, "SELECT idTarjeta FROM Usuarios WHERE email=?");
    }
    
    public static PreparedStatement GetOcupacionSala(Connection con) {
        return getStatement(con, "SELECT ocupada FROM Salas WHERE idSala=?");
    }
    
    public static PreparedStatement GetOcupacionCubiculo(Connection con) {
        return getStatement(con, "SELECT ocupado FROM Cubiculos WHERE idCubiculo=?");
    }
    
    public static PreparedStatement GetLecturas(Connection con) {
        return getStatement(con, "SELECT * FROM LecturaSensores");
    }
    
    public static PreparedStatement GetUsuarioPassw(Connection con){
        return getStatement(con, "SELECT tipo FROM Usuarios WHERE email=? AND passw=?");
    }
    
    public static PreparedStatement GetVecesReserva(Connection con){ 
        return getStatement(con, "SELECT COUNT(*) FROM Reservas WHERE email_usuario=? AND idSala=?");
        //Si se quiere saber el tiempo total de las reservas en h, multiplicar por 2 el numero de reservas. 
    }
    
    public static PreparedStatement GetRuidoCubiculo(Connection con){ //Es decir, la última lectura
        return getStatement(con, "SELECT valor FROM LecturaSensores WHERE idSensor=3 AND idCubiculo=?  ORDER BY fechaHora DESC LIMIT 1;");
    }
    
    public static PreparedStatement GetHumedadCubiculo(Connection con){ //Es decir, la última lectura
        return getStatement(con, "SELECT valor FROM LecturaSensores WHERE idSensor=1 AND idCubiculo=?  ORDER BY fechaHora DESC LIMIT 1;");
    }
}