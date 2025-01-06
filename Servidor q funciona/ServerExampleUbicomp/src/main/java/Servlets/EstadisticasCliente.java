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

@WebServlet(name = "EstadisticasCliente", urlPatterns = {"/EstadisticasCliente"})
public class EstadisticasCliente extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EstadisticasCliente.class.getName());

    private static final String CUBICULOS_TABLE = "biblioteca.Cubiculos";
    private static final String SALAS_TABLE = "biblioteca.Salas";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ConnectionDB connectionDB = new ConnectionDB();
        Connection connection = null;

        JSONObject jsonResponse = new JSONObject();
        try {
            connection = connectionDB.obtainConnection(true);

            JSONObject estadisticas = getEstadisticas(connection);
            response.getWriter().write(estadisticas.toString());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los datos", e);
            jsonResponse.put("error", "Error al obtener los datos: " + e.getMessage());
            response.getWriter().write(jsonResponse.toString());
        } finally {
            if (connection != null) {
                connectionDB.closeConnection(connection);
            }
        }
    }

    private JSONObject getEstadisticas(Connection connection) throws SQLException {
        JSONObject estadisticas = new JSONObject();

        String queryCubiculos = "SELECT COUNT(*) AS total, SUM(ocupado) AS ocupados FROM " + CUBICULOS_TABLE;
        String queryCubiculosLibres = "SELECT idCubiculo FROM " + CUBICULOS_TABLE + " WHERE ocupado = 0";
        String querySalas = "SELECT idSala FROM " + SALAS_TABLE + " WHERE ocupada = 0";

        try (PreparedStatement stmtCubiculos = connection.prepareStatement(queryCubiculos);
             PreparedStatement stmtCubiculosLibres = connection.prepareStatement(queryCubiculosLibres);
             PreparedStatement stmtSalas = connection.prepareStatement(querySalas);
             ResultSet rsCubiculos = stmtCubiculos.executeQuery();
             ResultSet rsCubiculosLibres = stmtCubiculosLibres.executeQuery();
             ResultSet rsSalas = stmtSalas.executeQuery()) {

            JSONObject ocupacionCubiculos = new JSONObject();
            if (rsCubiculos.next()) {
                int total = rsCubiculos.getInt("total");
                int ocupados = rsCubiculos.getInt("ocupados");
                ocupacionCubiculos.put("total", total);
                ocupacionCubiculos.put("ocupados", ocupados);
                ocupacionCubiculos.put("disponibles", total - ocupados);
            }

            JSONArray cubiculosLibres = new JSONArray();
            while (rsCubiculosLibres.next()) {
                JSONObject cubiculo = new JSONObject();
                cubiculo.put("id", rsCubiculosLibres.getInt("idCubiculo"));
                cubiculo.put("nombre", "Cubículo " + rsCubiculosLibres.getInt("idCubiculo"));
                cubiculosLibres.put(cubiculo);
            }

            JSONArray salasLibres = new JSONArray();
            while (rsSalas.next()) {
                JSONObject sala = new JSONObject();
                sala.put("id", rsSalas.getInt("idSala"));
                sala.put("nombre", "Sala " + rsSalas.getInt("idSala"));
                salasLibres.put(sala);
            }

            estadisticas.put("cubiculos", cubiculosLibres);
            estadisticas.put("salas", salasLibres);
            estadisticas.put("ocupacion", ocupacionCubiculos);
        }

        return estadisticas;
    }

}
