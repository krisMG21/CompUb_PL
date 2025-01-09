package Servlets;

import Database.ConnectionDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


public class LecturasSensores extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONArray sensoresData = obtenerDatosSensores();

        System.out.println("Datos de sensores: " + sensoresData.toString());

        PrintWriter out = response.getWriter();
        out.print(sensoresData.toString());
        out.flush();
    }

    private JSONArray obtenerDatosSensores() {
        JSONArray sensoresData = new JSONArray();
        ConnectionDB connectionDB = new ConnectionDB();
        
        try (Connection connection = connectionDB.obtainConnection(true)) {
            if (connection == null) {
                throw new SQLException("No database connection available.");
            }

            String query = "SELECT s.idSala, " +
                           "MAX(CASE WHEN l.idSensor = 1 THEN l.valor END) AS humedad, " +
                           "MAX(CASE WHEN l.idSensor = 2 THEN l.valor END) AS temperatura, " +
                           "MAX(CASE WHEN l.idSensor = 3 THEN l.valor END) AS sonido, " +
                           "MAX(CASE WHEN l.idSensor = 4 THEN l.valor END) AS luz, " +
                           "MAX(l.fechaHora) AS fechaHora " +
                           "FROM biblioteca.Salas s " +
                           "LEFT JOIN biblioteca.LecturaSensores l ON s.idSala = l.idSala " +
                           "GROUP BY s.idSala";

            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    JSONObject salaData = new JSONObject();
                    salaData.put("idSala", rs.getInt("idSala"));
                    salaData.put("temperatura", rs.getObject("temperatura"));
                    salaData.put("humedad", rs.getObject("humedad"));
                    salaData.put("sonido", categorizarRuido(rs.getInt("sonido")));
                    salaData.put("luz", categorizarLuminosidad(rs.getInt("luz")));
                    salaData.put("fechaHora", rs.getString("fechaHora"));
                    sensoresData.put(salaData);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject errorData = new JSONObject();
            errorData.put("error", "Error en la base de datos: " + e.getMessage());
            sensoresData.put(errorData);
        }

        return sensoresData;
    }

    private String categorizarRuido(int valor) {
        if (valor < 30) {
            return "Bajo";
        } else if (valor <= 60) {
            return "Medio";
        } else {
            return "Alto";
        }
    }
    
    private String categorizarLuminosidad(int valor) {
        if (valor < 100) {
            return "Bajo";
        } else if (valor <= 300) {
            return "Medio";
        } else {
            return "Alto";
        }
    }
}
