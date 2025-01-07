package Servlets;

import Database.ConnectionDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

@WebServlet(name = "Reserva", urlPatterns = {"/Reserva", "/Reserva/*"})
public class Reserva extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Reserva() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonResponse = new JSONObject();
        String email = request.getParameter("email");
        String idSalaStr = request.getParameter("idSala");
        String horaReservaStr = request.getParameter("horaReserva");

        if (email == null || email.isEmpty() || idSalaStr == null || horaReservaStr == null) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Parámetros incompletos. Verifique email, idSala y horaReserva.");
            return;
        }

        try (Connection conexionBD = new ConnectionDB().obtainConnection(true)) {
            int idSala = Integer.parseInt(idSalaStr);
            Timestamp horaReserva = Timestamp.valueOf(horaReservaStr);

            if (reservaExiste(conexionBD, idSala, horaReserva)) {
                sendErrorResponse(response, HttpServletResponse.SC_CONFLICT, "Ya existe una reserva en esta sala para la fecha y hora especificada.");
                return;
            }

            String sql = "INSERT INTO biblioteca.Reservas (email_usuario, idSala_sala, horaReserva) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = conexionBD.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setInt(2, idSala);
                preparedStatement.setTimestamp(3, horaReserva);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Reserva realizada con éxito");
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al realizar la reserva.");
                }
            }
        } catch (IllegalArgumentException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Formato de parámetros inválido.");
        } catch (SQLException e) {
            log("Error en la base de datos: " + e.getMessage(), e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno en el servidor.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String fecha = request.getParameter("fecha");

        if ((email == null || email.isEmpty()) && (fecha == null || fecha.isEmpty())) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Debe proporcionar al menos un filtro (email o fecha).");
            return;
        }

        try {
            JSONArray reservas = getReservasFiltradas(email, fecha);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(reservas.toString());
        } catch (SQLException e) {
            log("Error al consultar las reservas: " + e.getMessage(), e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno en el servidor.");
        }
    }



    private boolean reservaExiste(Connection conexionBD, int idSala, Timestamp horaReserva) throws SQLException {
        String checkSql = "SELECT COUNT(*) AS conflictCount FROM biblioteca.Reservas WHERE idSala_sala = ? AND horaReserva = ?";
        try (PreparedStatement checkStmt = conexionBD.prepareStatement(checkSql)) {
            checkStmt.setInt(1, idSala);
            checkStmt.setTimestamp(2, horaReserva);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next() && rs.getInt("conflictCount") > 0;
            }
        }
    }

    private JSONArray getReservasFiltradas(String email, String fecha) throws SQLException {
        String sql = "SELECT idReserva, email_usuario, idSala_sala, horaReserva FROM biblioteca.Reservas WHERE 1=1";
        if (email != null && !email.isEmpty()) sql += " AND email_usuario = ?";
        if (fecha != null && !fecha.isEmpty()) sql += " AND DATE(horaReserva) = ?";

        try (Connection conn = new ConnectionDB().obtainConnection(true);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int index = 1;
            if (email != null && !email.isEmpty()) stmt.setString(index++, email);
            if (fecha != null && !fecha.isEmpty()) stmt.setString(index, fecha);

            JSONArray reservasJson = new JSONArray();
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
            return reservasJson;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", false);
        jsonResponse.put("message", message);
        response.setStatus(statusCode);
        response.getWriter().write(jsonResponse.toString());
    }

    private void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", message);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(jsonResponse.toString());
    }
}
