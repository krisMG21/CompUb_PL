package Servlets;

import Logic.Logic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        try {
            String email = request.getParameter("username"); // Asumiendo que el campo se llama "username" en el formulario
            String password = request.getParameter("password");
            
            String userType = Logic.authenticateUser(email, password);

            if (userType != null) {
                HttpSession session = request.getSession();
                session.setAttribute("email", email);
                session.setAttribute("userType", userType);

                if ("admin".equals(userType)) {
                    response.sendRedirect("MenuGestor.jsp");
                } else if ("cliente".equals(userType)) {
                    response.sendRedirect("MenuCliente.jsp");
                } else {
                    // En caso de un tipo de usuario no esperado
                    request.setAttribute("error", "Tipo de usuario no reconocido.");
                    request.getRequestDispatcher("MenuInicio.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Email o contraseña incorrectos.");
                request.getRequestDispatcher("MenuInicio.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error en el sistema. Por favor, inténtelo más tarde.");
            request.getRequestDispatcher("MenuInicio.jsp").forward(request, response);
        }
    }
}
