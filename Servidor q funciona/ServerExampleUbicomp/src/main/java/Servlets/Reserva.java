package Servlets;

import Database.ConnectionDB;
import Mqtt.MQTTBroker;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import Logic.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "Reserva", urlPatterns = {"/Reserva"})
public class Reserva extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Reserva() {
        super();
    }

    // Método POST para realizar una reserva (modificado con validación)
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

            // Comprobar si ya existe una reserva en la misma sala a la misma hora
            String checkSql = "SELECT COUNT(*) AS conflictCount FROM biblioteca.Reservas WHERE idSala_sala = ? AND horaReserva = ?";
            try (PreparedStatement checkStmt = conexionBD.prepareStatement(checkSql)) {
                checkStmt.setInt(1, idSala);
                checkStmt.setTimestamp(2, horaReserva);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt("conflictCount") > 0) {
                        JSONObject jsonResponse = new JSONObject();
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "Ya existe una reserva en esta sala para la fecha y hora especificada.");
                        response.getWriter().write(jsonResponse.toString());
                        return;
                    }
                }
            }

            // Insertar nueva reserva
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
            if (preparedStatement != null) try {
                preparedStatement.close();
            } catch (SQLException e) {
                Log.log.error("Error al cerrar PreparedStatement", e);
            }
            if (conexionBD != null) {
                connectionBD.closeConnection(conexionBD);
            }
        }
    }

    // Método GET para obtener las reservas filtradas
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener parámetros de la solicitud (filtros)
        String email = request.getParameter("email");
        String fecha = request.getParameter("fecha");

        // Obtener las reservas filtradas desde la base de datos
        JSONArray reservas = getReservasFiltradas(email, fecha);

        // Configurar la respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Enviar el resultado como respuesta JSON
        PrintWriter out = response.getWriter();
        out.print(reservas.toString());
        out.flush();
    }

    // Método para obtener las reservas filtradas desde la base de datos
    private JSONArray getReservasFiltradas(String email, String fecha) {
        JSONArray reservasJson = new JSONArray();

        // Cadena SQL básica
        String sql = "SELECT email_usuario, idSala_sala, horaReserva FROM biblioteca.Reservas WHERE 1=1";

        // Añadir condiciones si existen los filtros
        if (email != null && !email.isEmpty()) {
            sql += " AND email_usuario = ?";
        }
        if (fecha != null && !fecha.isEmpty()) {
            sql += " AND DATE(horaReserva) = ?";
        }

        try (Connection conn = new ConnectionDB().obtainConnection(true);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    @Override
    public void destroy() {
        // Limpiar recursos, si es necesario
    }
}
