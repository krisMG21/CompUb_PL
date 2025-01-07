package Servlets;

import Database.ConnectionDB;
import Mqtt.MQTTBroker;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;
import org.json.JSONArray;
import Logic.Log;
import Logic.Logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "Reserva", urlPatterns = {"/Reserva", "/Reserva/*"})
public class Reserva extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Reserva() {
        super();
    }

    // Método POST para realizar una reserva
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ConnectionDB connectionBD = new ConnectionDB();
        Connection conexionBD = null;
        
        JSONObject jsonResponse = new JSONObject();
        
        String email = request.getParameter("email");
        String idSalaStr = request.getParameter("idSala");
        String horaReservaStr = request.getParameter("horaReserva");
        
        //Validación de parámetros
        if (email == null || email.isEmpty() || idSalaStr == null || horaReservaStr == null){
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Parámetros incompletos. Verifique email, idSala y horaReserva.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try {
            int idSala = Integer.parseInt(idSalaStr);
            Timestamp horaReserva = Timestamp.valueOf(horaReservaStr);
            
            conexionBD = connectionBD.obtainConnection(true);

            // Comprobar si ya existe una reserva en la misma sala a la misma hora
            String checkSql = "SELECT COUNT(*) AS conflictCount FROM biblioteca.Reservas WHERE idSala_sala = ? AND horaReserva = ?";
            try (PreparedStatement checkStmt = conexionBD.prepareStatement(checkSql)) {
                checkStmt.setInt(1, idSala);
                checkStmt.setTimestamp(2, horaReserva);
                
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt("conflictCount") > 0) {
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "Ya existe una reserva en esta sala para la fecha y hora especificada.");
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.getWriter().write(jsonResponse.toString());
                        return;
                    }
                }
            }

            // Insertar nueva reserva
            String sql = "INSERT INTO biblioteca.Reservas (email_usuario, idSala_sala, horaReserva) VALUES (?, ?, ?)";
            
            try(PreparedStatement preparedStatement = conexionBD.prepareStatement(sql)){
                preparedStatement.setString(1, email);
                preparedStatement.setInt(2, idSala);
                preparedStatement.setTimestamp(3, horaReserva);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Reserva realizada con éxito");
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Error al realizar la reserva");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (IllegalArgumentException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Formato de parámetros inválido.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException e) {
            Log.log.error("Error en la base de datos: " + e.getMessage(), e);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error en la base de datos: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(jsonResponse.toString());
        } finally {
            if (conexionBD != null) {
                connectionBD.closeConnection(conexionBD);
            }
        }
    }

    // Método GET para obtener las reservas filtradas
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String fecha = request.getParameter("fecha");
        
        // Validar filtros
        if ((email == null || email.isEmpty()) && (fecha == null || fecha.isEmpty())) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", false);
            errorResponse.put("message", "Debe proporcionar al menos un filtro (email o fecha).");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(errorResponse.toString());
            return;
        }

        JSONArray reservas = getReservasFiltradas(email, fecha);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(reservas.toString());
        
        //response.setCharacterEncoding("UTF-8");

        //PrintWriter out = response.getWriter();
        //out.print(reservas.toString());
        //out.flush();
    }

    private JSONArray getReservasFiltradas(String email, String fecha) {
        JSONArray reservasJson = new JSONArray();

        String sql = "SELECT idReserva, email_usuario, idSala_sala, horaReserva FROM biblioteca.Reservas";

        if (email != null && !email.isEmpty()) {
            sql += " AND email_usuario = ?";
        }
        if (fecha != null && !fecha.isEmpty()) {
            sql += " AND DATE(horaReserva) = ?";
        }

        try (Connection conn = new ConnectionDB().obtainConnection(true); PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (email != null && !email.isEmpty()) {
                stmt.setString(index++, email);
            }
            if (fecha != null && !fecha.isEmpty()) {
                stmt.setString(index, fecha);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    JSONObject reserva = new JSONObject();
                    reserva.put("idReserva", rs.getInt("idReserva"));
                    reserva.put("email", rs.getString("email_usuario"));
                    reserva.put("idSala", rs.getInt("idSala_sala"));
                    reserva.put("horaReserva", rs.getTimestamp("horaReserva").toString());
                    reservasJson.put(reserva);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservasJson;
    }

    // Método DELETE para cancelar una reserva
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String idSalaStr = request.getParameter("idSala");
        String horaReservaStr = request.getParameter("horaReserva");

        JSONObject jsonResponse = new JSONObject();

        // Validar parámetros
        if (email == null || email.isEmpty() || idSalaStr == null || horaReservaStr == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Parámetros incompletos. Verifique email, idSala y horaReserva.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(jsonResponse.toString());
            return;
        }
        
        try {
            int idSala = Integer.parseInt(idSalaStr);
            Timestamp horaReserva = Timestamp.valueOf(horaReservaStr);

            boolean success = Logic.cancelarReserva(email, idSala, horaReserva);

            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Reserva cancelada con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Error al cancelar la reserva.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Formato de parámetros inválido.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    public void destroy() {
        // Limpiar recursos, si es necesario
    }
}
