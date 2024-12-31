package Servlets;

import Database.ConectionDDBB;
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

@WebServlet(name = "PerfilUsuarioServlet", urlPatterns = {"/PerfilUsuarioServlet"})
public class PerfilUsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public PerfilUsuarioServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ConectionDDBB connectionDB = new ConectionDDBB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultadosConsulta = null;

        PrintWriter out = response.getWriter();

        try {
            // Obtener la conexión a la base de datos
            conexionBD = connectionDB.obtainConnection(true);

            // Leer el correo electrónico del usuario autenticado desde la sesión
            String emailUsuario = (String) request.getSession().getAttribute("emailUsuario");

            if (emailUsuario == null || emailUsuario.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"error\": \"No hay un usuario autenticado.\"}");
                return;
            }

            // Crear la consulta SQL para obtener los datos del usuario
            String sql = "SELECT nombre, apellidos, email FROM Usuarios WHERE email = ?";
            preparedStatement = conexionBD.prepareStatement(sql);

            // Asignar el valor al parámetro
            preparedStatement.setString(1, emailUsuario);

            // Ejecutar la consulta
            resultadosConsulta = preparedStatement.executeQuery();

            if (resultadosConsulta.next()) {
                // Usuario encontrado, devolver los datos en formato JSON
                String nombre = resultadosConsulta.getString("nombre");
                String apellidos = resultadosConsulta.getString("apellidos");
                String correo = resultadosConsulta.getString("email");

                out.write("{");
                out.write("\"nombre\": \"" + nombre + "\",");
                out.write("\"apellidos\": \"" + apellidos + "\",");
                out.write("\"correo\": \"" + correo + "\"");
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
        } catch (NullPointerException ex) {
            Logger.getLogger(PerfilUsuarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"Error inesperado en el servidor.\"}");
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
}
