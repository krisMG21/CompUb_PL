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

//@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

        ConnectionDB connectionDB = new ConnectionDB();
        Connection conexionBD = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultadosConsulta = null;

        try {
            conexionBD = connectionDB.obtainConnection(true);

            // Obtener parámetros del formulario
            String email = request.getParameter("username");
            String password = request.getParameter("password");

            // Consulta SQL
            String sql = "SELECT tipo FROM biblioteca.Usuarios WHERE email = ? AND passw = ?";
            preparedStatement = conexionBD.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultadosConsulta = preparedStatement.executeQuery();

            PrintWriter out = response.getWriter();
            if (resultadosConsulta.next()) {
                String userType = resultadosConsulta.getString("tipo");
                // Respuesta JSON en caso de éxito
                out.write("{\"status\": \"success\", \"userType\": \"" + userType + "\"}");
            } else {
                // Respuesta JSON en caso de error
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                out.write("{\"status\": \"error\", \"message\": \"Usuario o contraseña incorrectos\"}");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Error en el servidor\"}");
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
