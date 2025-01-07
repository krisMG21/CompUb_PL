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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "EstadisticasGestor", urlPatterns = {"/EstadisticasGestor"})
public class EstadisticasGestor extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EstadisticasGestor.class.getName());

    private static final String CUBICULOS_TABLE = "biblioteca.Cubiculos";
    private static final String SALAS_TABLE = "biblioteca.Salas";
    private static final String RESERVAS_TABLE = "biblioteca.Reservas";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ConnectionDB connectionDB = new ConnectionDB();
        Connection connection = null;

        try {
            connection = connectionDB.obtainConnection(true);
            if (connection == null) {
                throw new SQLException("No database connection available.");
            }
            
            JSONObject estadisticas = getEstadisticas(connection);
            response.getWriter().write(estadisticas.toString());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los datos", e);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", "Error al obtener los datos: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(jsonResponse.toString());
        } finally {
            if (connection != null) {
                connectionDB.closeConnection(connection);
            }
        }
    }

    private JSONObject getEstadisticas(Connection connection) throws SQLException {
        JSONObject estadisticas = new JSONObject();

        String queryCubiculosCount = "SELECT COUNT(*) AS total FROM " + CUBICULOS_TABLE;
        String queryCubiculos = "SELECT idCubiculo, ocupado FROM " + CUBICULOS_TABLE;
        String querySalas = "SELECT s.idSala, s.ocupada, r.email_usuario, r.horaReserva " +
                            "FROM " + SALAS_TABLE + " s " +
                            "LEFT JOIN " + RESERVAS_TABLE + " r ON s.idSala = r.idSala_sala AND r.horaReserva > NOW() " +
                            "ORDER BY s.idSala";

        try (PreparedStatement stmtCubiculosCount = connection.prepareStatement(queryCubiculosCount);
             PreparedStatement stmtCubiculos = connection.prepareStatement(queryCubiculos);
             PreparedStatement stmtSalas = connection.prepareStatement(querySalas);
             ResultSet rsCubiculosCount = stmtCubiculosCount.executeQuery();
             ResultSet rsCubiculos = stmtCubiculos.executeQuery();
             ResultSet rsSalas = stmtSalas.executeQuery()) {

            int totalCubiculos = 0;
            int cubiculosOcupados = 0;
            JSONArray cubiculosLibres = new JSONArray();

            if (rsCubiculosCount.next()) {
                totalCubiculos = rsCubiculosCount.getInt("total");
            }

            while (rsCubiculos.next()) {
                if (rsCubiculos.getInt("ocupado") == 0) {
                    cubiculosLibres.put(buildCubiculoJson(rsCubiculos));
                } else {
                    cubiculosOcupados++;
                }
            }

            JSONObject ocupacionCubiculos = new JSONObject();
            ocupacionCubiculos.put("total", totalCubiculos);
            ocupacionCubiculos.put("ocupados", cubiculosOcupados);
            ocupacionCubiculos.put("disponibles", totalCubiculos - cubiculosOcupados);

            JSONArray salasInfo = new JSONArray();
            while (rsSalas.next()) {
                salasInfo.put(buildSalaJson(rsSalas));
            }

            estadisticas.put("cubiculos", cubiculosLibres);
            estadisticas.put("salas", salasInfo);
            estadisticas.put("ocupacion", ocupacionCubiculos);
        }

        return estadisticas;
    }

    private JSONObject buildCubiculoJson(ResultSet rs) throws SQLException {
        JSONObject cubiculo = new JSONObject();
        cubiculo.put("id", rs.getInt("idCubiculo"));
        cubiculo.put("nombre", "Cubículo " + rs.getInt("idCubiculo"));
        return cubiculo;
    }

    private JSONObject buildSalaJson(ResultSet rs) throws SQLException {
        JSONObject sala = new JSONObject();
        sala.put("id", rs.getInt("idSala"));
        sala.put("nombre", "Sala " + rs.getInt("idSala"));
        sala.put("ocupada", rs.getBoolean("ocupada"));
        sala.put("email", rs.getString("email_usuario") != null ? rs.getString("email_usuario") : "");
        sala.put("horaReserva", rs.getTimestamp("horaReserva"));
        return sala;
    }
}