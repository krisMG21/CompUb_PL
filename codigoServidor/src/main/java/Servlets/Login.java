/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fatim
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final long serialVersionUID = 1L;
    //Para simulas bbdd
    private final String[][] usuariosDB = {
        {"Usuario", "B", "0"}, // Estudiante
        {"UsuarioG", "A", "1"}      // Gestor
    };
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    // Método para manejar las solicitudes POST (el formulario envía datos por POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configurar la respuesta en formato JSON
        
        try{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Connection conexionBD = null;
        Statement statementSQL = null;
        ResultSet resultadosConsulta=null;
        int cnt = 0;
        // Leer los parámetros enviados desde el formulario
        String user = request.getParameter("username");
        String passw = request.getParameter("password");
        
        statementSQL = conexionBD.createStatement();
        resultadosConsulta = statementSQL.executeQuery("SELECT email, passw, tipo FROM Usuarios WHERE UNAME = '"+user+"' AND passw = '"+passw+"'");

        while (resultadosConsulta.next()){
                String username = resultadosConsulta.getString(1);
                String password = resultadosConsulta.getString(2);
                String userType = resultadosConsulta.getString(3);
                if(user.equals(username)&&passw.equals(password)){ //si el usuario dado coincide con el de la bbdd y lo mismo con la pass 
                    cnt++;
                    response.getWriter().write("{\"result\": \"Inicio de sesión exitoso\"}");
                    break;
                }
            }
            if (cnt == 0){
                response.getWriter().write("{\"result\": \"Usuario o contraseña incorrectos\"}");
            }

        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid numbers\"}");
        } catch (SQLIntegrityConstraintViolationException  ex){
            response.getWriter().write("{\"result\": " + "\"Error de inicio de sesion.\"" + "}");
        } catch (SQLException ex) { 
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // Validar las credenciales contra los usuarios de la "base de datos"
        for (String[] user : usuariosDB) {
            if (user[0].equals(username) && user[1].equals(password)) {
                userType = user[2]; // Obtener el tipo de usuario (0 para estudiante, 1 para gestor)
                break;
            }
        }

        // Preparar la respuesta JSON
        PrintWriter out = response.getWriter();
        if (userType != null) {
            // Usuario encontrado, enviar tipo de usuario como respuesta JSON
            out.println("{\"status\":\"success\",\"userType\":" + userType + "}");
        } else {
            // Usuario no encontrado, enviar error
            out.println("{\"status\":\"error\",\"message\":\"Usuario o contraseña incorrectos\"}");
        }
    }

    // Método para manejar solicitudes GET (opcional, dependiendo de tu necesidad)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("LoginServlet está funcionando.");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //@Override
   /* protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }*/

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   /* @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }*/

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
