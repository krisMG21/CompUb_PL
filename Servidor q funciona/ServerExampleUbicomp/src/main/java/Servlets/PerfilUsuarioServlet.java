package Servlets;

import Database.ConnectionDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//@WebServlet(name = "PerfilUsuarioServlet", urlPatterns = {"/PerfilUsuarioServlet"})
public class PerfilUsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public PerfilUsuarioServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuración de la respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Variables para la conexión a la base de datos
        ConnectionDB connectionDB = new ConnectionDB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultadosConsulta = null;

        // Obtener el escritor de respuesta
        PrintWriter out = response.getWriter();

        try {
            // Obtener la conexión a la base de datos
            conexionBD = connectionDB.obtainConnection(true);

            // Leer el correo electrónico del usuario autenticado desde la sesión
            String emailUsuario = (String) request.getSession().getAttribute("email");

            if (emailUsuario == null || emailUsuario.isEmpty()) {
                // Usuario no autenticado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"error\": \"No hay un usuario autenticado.\"}");
                return;
            }

            // Consulta SQL para obtener los datos del usuario
            String sql = "SELECT nombreApellido, email FROM bibliotecaUsuarios WHERE email = ?";
            preparedStatement = conexionBD.prepareStatement(sql);

            // Asignar el valor al parámetro
            preparedStatement.setString(1, emailUsuario);

            // Ejecutar la consulta
            resultadosConsulta = preparedStatement.executeQuery();

            if (resultadosConsulta.next()) {
                // Usuario encontrado, devolver los datos en formato JSON
                String nombreApellido = resultadosConsulta.getString("nombreApellido");
                String correo = resultadosConsulta.getString("email");

                // Construcción del JSON de respuesta
                out.write("{");
                out.write("\"nombre\": \"" + escapeJson(nombreApellido) + "\",");
                out.write("\"correo\": \"" + escapeJson(correo) + "\"");
                out.write("}");
            } else {
                // Usuario no encontrado
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Usuario no encontrado.\"}");
            }

        } catch (SQLException ex) {
            Logger.getLogger(PerfilUsuarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"Error en el servidor. Por favor, inténtelo más tarde.\"}");
        } finally {
            // Liberar recursos
            try {
                if (resultadosConsulta != null) {
                    resultadosConsulta.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexionBD != null) {
                    connectionDB.closeConnection(conexionBD);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PerfilUsuarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método para escapar caracteres especiales en JSON.
     * 
     * @param value Texto a escapar.
     * @return Texto escapado para JSON.
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
