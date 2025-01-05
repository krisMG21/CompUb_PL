package Servlets;

import Database.ConnectionDB;
import Mqtt.MQTTBroker;
import Mqtt.MQTTPublisher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import Logic.Log;

//@WebServlet(name = "Reserva", urlPatterns = {"/Reserva"})
public class Reserva extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private MQTTBroker mqttBroker;
    
    public Reserva() {
        super();
        mqttBroker = new MQTTBroker(); // Asume que tienes un constructor por defecto o configura según sea necesario
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ConnectionDB connectionBD = new ConnectionDB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        
        try {
            conexionBD = connectionBD.obtainConnection(true);
            
            String email = request.getParameter("email");
            int idSala = Integer.parseInt(request.getParameter("idSala"));
            Timestamp horaReserva = Timestamp.valueOf(request.getParameter("horaReserva"));
            
            String sql = "INSERT INTO biblioteca.Reservas (email_usuario, idSala_sala, horaReserva) VALUES (?, ?, ?)";
            preparedStatement = ConnectionDB.getStatement(conexionBD, sql);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, idSala);
            preparedStatement.setTimestamp(3, horaReserva);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Reserva realizada con éxito");
                
                // Programar la publicación del estado de la reserva
                scheduleReservationStatePublish(idSala, horaReserva, email);
                
                response.getWriter().write(jsonResponse.toString());
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Error al realizar la reserva");
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (SQLException e) {
            Log.log.error("Error en la base de datos: " + e.getMessage(), e);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error en la base de datos: " + e.getMessage());
            response.getWriter().write(jsonResponse.toString());
        } finally {
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) { Log.log.error("Error al cerrar PreparedStatement", e); }
            if (conexionBD != null) connectionBD.closeConnection(conexionBD);
        }
    }
    
    private void scheduleReservationStatePublish(int idSala, Timestamp horaReserva, String email) {
        long delay = horaReserva.getTime() - System.currentTimeMillis();
        scheduler.schedule(() -> {
            publishReservationState(idSala, 1, email);
            // Programar el fin de la reserva (asumiendo 1 hora de duración)
            scheduler.schedule(() -> publishReservationState(idSala, 0, null), 1, TimeUnit.HOURS);
        }, delay, TimeUnit.MILLISECONDS);
    }
    
    private void publishReservationState(int idSala, int state, String email) {
        String uidTopic = "sala/" + idSala + "/uid";
        String reservaTopic = "sala/" + idSala + "/reserva";
        
        if (state == 1 && email != null) {
            String idTarjeta = obtenerIdTarjeta(email);
            MQTTPublisher.publish(mqttBroker, uidTopic, idTarjeta);
        } else {
            MQTTPublisher.publish(mqttBroker, uidTopic, "0");
        }
        
        MQTTPublisher.publish(mqttBroker, reservaTopic, String.valueOf(state));
    }
    
    private String obtenerIdTarjeta(String email) {
        ConnectionDB connectionBD = new ConnectionDB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String idTarjeta = "0";
        
        try {
            conexionBD = connectionBD.obtainConnection(true);
            preparedStatement = ConnectionDB.GetTarjetaFromUsuario(conexionBD);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                idTarjeta = resultSet.getString("idTarjeta");
            }
        } catch (SQLException e) {
            Log.log.error("Error al obtener idTarjeta: " + e.getMessage(), e);
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { Log.log.error("Error al cerrar ResultSet", e); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) { Log.log.error("Error al cerrar PreparedStatement", e); }
            if (conexionBD != null) connectionBD.closeConnection(conexionBD);
        }
        
        return idTarjeta;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ConnectionDB connectionBD = new ConnectionDB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            conexionBD = connectionBD.obtainConnection(true);
            
            String sql = "SELECT * FROM Reservas WHERE horaReserva > NOW() ORDER BY horaReserva";
            preparedStatement = ConnectionDB.getStatement(conexionBD, sql);
            resultSet = preparedStatement.executeQuery();
            
            JSONObject jsonResponse = new JSONObject();
            while (resultSet.next()) {
                JSONObject reserva = new JSONObject();
                reserva.put("email", resultSet.getString("email_usuario"));
                reserva.put("idSala", resultSet.getInt("idSala_sala"));
                reserva.put("horaReserva", resultSet.getTimestamp("horaReserva").toString());
                jsonResponse.append("reservas", reserva);
            }
            
            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException e) {
            Log.log.error("Error en la base de datos: " + e.getMessage(), e);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", "Error en la base de datos: " + e.getMessage());
            response.getWriter().write(jsonResponse.toString());
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { Log.log.error("Error al cerrar ResultSet", e); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) { Log.log.error("Error al cerrar PreparedStatement", e); }
            if (conexionBD != null) connectionBD.closeConnection(conexionBD);
        }
    }
    
    @Override
    public void destroy() {
        super.destroy();
        scheduler.shutdownNow();
    }
}
