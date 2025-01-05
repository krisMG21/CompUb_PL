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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//@WebServlet("/EstadisticasGestor")
public class EstadisticasGestor extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ConnectionDB connectionDB = new ConnectionDB();
        Connection connection = null;

        JSONObject jsonResponse = new JSONObject();
        try {
            connection = connectionDB.obtainConnection(true);

            // Obtener cubículos libres
            JSONArray cubiculosLibres = getCubiculosLibres(connection);

            // Obtener salas libres junto con el email del ocupante
            JSONArray salasLibres = getSalasLibres(connection);

            // Obtener grado de ocupación
            JSONObject ocupacionCubiculos = getOcupacionCubiculos(connection);

            // Construir la respuesta JSON
            jsonResponse.put("cubiculos", cubiculosLibres);
            jsonResponse.put("salas", salasLibres);
            jsonResponse.put("ocupacion", ocupacionCubiculos);

            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            jsonResponse.put("error", "Error al obtener los datos: " + e.getMessage());
            response.getWriter().write(jsonResponse.toString());
        } finally {
            if (connection != null) {
                connectionDB.closeConnection(connection);
            }
        }
    }

    private JSONArray getCubiculosLibres(Connection connection) throws SQLException {
        JSONArray cubiculosLibres = new JSONArray();
        String query = "SELECT idCubiculo FROM biblioteca.Cubiculos WHERE ocupado = 0";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JSONObject cubiculo = new JSONObject();
                cubiculo.put("id", rs.getInt("idCubiculo"));
                cubiculo.put("nombre", "Cubículo " + rs.getInt("idCubiculo"));
                cubiculosLibres.put(cubiculo);
            }
        }
        return cubiculosLibres;
    }

    private JSONArray getSalasLibres(Connection connection) throws SQLException {
        JSONArray salasLibres = new JSONArray();
        String query = "SELECT s.idSala, r.email_usuario " +
                       "FROM biblioteca.Salas s " +
                       "LEFT JOIN biblioteca.Reservas r ON s.idSala = r.idSala_sala AND r.horaReserva > NOW() " +
                       "WHERE s.ocupada = 0 OR r.idReservas IS NOT NULL";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JSONObject sala = new JSONObject();
                sala.put("id", rs.getInt("idSala"));
                sala.put("nombre", "Sala " + rs.getInt("idSala"));
                sala.put("email", rs.getString("email_usuario"));
                salasLibres.put(sala);
            }
        }
        return salasLibres;
    }

    private JSONObject getOcupacionCubiculos(Connection connection) throws SQLException {
        JSONObject ocupacion = new JSONObject();

        String queryOcupados = "SELECT COUNT(*) AS ocupados FROM biblioteca.Cubiculos WHERE ocupado = 1";
        try (PreparedStatement stmt = connection.prepareStatement(queryOcupados);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                ocupacion.put("ocupados", rs.getInt("ocupados"));
            }
        }

        String queryTotal = "SELECT COUNT(*) AS total FROM biblioteca.Cubiculos";
        try (PreparedStatement stmt = connection.prepareStatement(queryTotal);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                ocupacion.put("total", total);
                ocupacion.put("disponibles", total - ocupacion.getInt("ocupados"));
            }
        }

        return ocupacion;
    }
}
