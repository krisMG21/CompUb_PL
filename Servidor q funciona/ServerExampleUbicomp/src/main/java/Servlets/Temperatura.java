package Servlets;

import Database.ConnectionDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Temperatura extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Establecer el tipo de contenido de la respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Obtener la temperatura de la Sala 1
        JSONObject temperaturaData = obtenerTemperaturaSala1();

        // Mostrar el contenido de temperaturaData en la consola
        System.out.println("Datos de la temperatura: " + temperaturaData.toString());

        // Escribir la respuesta como JSON
        PrintWriter out = response.getWriter();
        out.print(temperaturaData.toString());
        out.flush();
    }

    private JSONObject obtenerTemperaturaSala1() {
        JSONObject temperaturaData = new JSONObject();
        ConnectionDB connectionDB = new ConnectionDB();
        
        // Usar try-with-resources para asegurar el cierre de recursos automáticamente
        try (Connection connection = connectionDB.obtainConnection(true)) {

            if (connection == null) {
                throw new SQLException("No database connection available.");
            }

            // Consulta para obtener la temperatura de la Sala 1
            String query = "SELECT l.fechaHora, l.valor AS temperatura, s.idSala " +
                           "FROM biblioteca.LecturaSensores l " +
                           "LEFT JOIN biblioteca.Salas s ON l.idSala = s.idSala " +
                           "WHERE l.idSensor = 1 AND s.idSala = 1 " +  // Filtrar por Sala 1
                           "ORDER BY l.fechaHora DESC LIMIT 1"; // Obtener solo la última lectura

            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    // Crear el objeto JSON con los datos de la Sala 1
                    temperaturaData.put("idSala", rs.getInt("idSala"));
                    temperaturaData.put("temperatura", rs.getInt("temperatura"));
                    temperaturaData.put("fechaHora", rs.getString("fechaHora"));
                } else {
                    // Si no se encuentra la lectura de temperatura, enviar un mensaje adecuado
                    temperaturaData.put("error", "No se encontraron datos de temperatura para la Sala 1.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            temperaturaData.put("error", "Error en la base de datos: " + e.getMessage());
        }

        return temperaturaData;
    }
}
