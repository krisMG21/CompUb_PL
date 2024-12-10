package Servlets;

import db.ConnectionDB;
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

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public Login(){
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ConnectionDB connectionDB = new ConnectionDB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultadosConsulta = null;

        try {
            // Obtener la conexión a la base de datos
            conexionBD = connectionDB.obtainConnection(true);

            // Leer los parámetros enviados desde el formulario
            String user = request.getParameter("username");
            String passw = request.getParameter("password");

            // Obtener el PreparedStatement predefinido para encontrar el usuario con su contraseña
            preparedStatement = ConnectionDB.GetUsuarioPassw(conexionBD);

            // Asignar los valores a los parámetros del PreparedStatement
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, passw);

            // Ejecutar la consulta
            resultadosConsulta = preparedStatement.executeQuery();

            if (resultadosConsulta.next()) {
                // Usuario encontrado, obtener el tipo de usuario
                String userType = resultadosConsulta.getString("tipo");
                response.getWriter().write("{\"result\": \"Inicio de sesión exitoso\", \"userType\": \"" + userType + "\"}");
            } else {
                // Usuario no encontrado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"result\": \"Usuario o contraseña incorrectos\"}");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"result\": \"Error en el servidor. Por favor, inténtelo más tarde.\"}");
        } catch (NullPointerException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"result\": \"Error inesperado en el servidor.\"}");
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
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
