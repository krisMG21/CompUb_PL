package Servlets;

import Database.ConnectionDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ConnectionDB connectionDB = new ConnectionDB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultadosConsulta = null;
        PrintWriter out = response.getWriter();

        try {
            conexionBD = connectionDB.obtainConnection(true);

            // Obtener parámetros del formulario
            String email = request.getParameter("username");
            String password = request.getParameter("password");

            if (email == null || password == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                out.write("{\"status\": \"error\", \"message\": \"Faltan parámetros (username o password)\"}");
                return;
            }

            // Consulta SQL para validar al usuario
            String sql = "SELECT tipo, nombre FROM biblioteca.Usuarios WHERE email = ? AND passw = ?";
            preparedStatement = conexionBD.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultadosConsulta = preparedStatement.executeQuery();

            if (resultadosConsulta.next()) {
                String userType = resultadosConsulta.getString("tipo");
                String userName = resultadosConsulta.getString("nombre");

                // Guardar el nombre de usuario en la sesión
                HttpSession session = request.getSession();
                session.setAttribute("userType", userType);
                session.setAttribute("userName", userName); // Guardamos el nombre del usuario

                // Respuesta JSON de éxito
                response.setStatus(HttpServletResponse.SC_OK); // 200 OK
                out.write("{\"status\": \"success\", \"userType\": \"" + userType + "\", \"userName\": \"" + userName + "\"}");
            } else {
                // Respuesta JSON en caso de error (usuario o contraseña incorrectos)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                out.write("{\"status\": \"error\", \"message\": \"Usuario o contraseña incorrectos\"}");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            out.write("{\"status\": \"error\", \"message\": \"Error en la base de datos\"}");
        } finally {
               try {
                if (resultadosConsulta != null) resultadosConsulta.close();
                if (preparedStatement != null) preparedStatement.close();
                if (conexionBD != null) connectionDB.closeConnection(conexionBD);
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
