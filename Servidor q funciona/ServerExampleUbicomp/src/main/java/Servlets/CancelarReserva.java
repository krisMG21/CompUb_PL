package Servlets;

import Logic.Logic;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import org.json.JSONObject;

@WebServlet(name = "CancelarReserva", urlPatterns = {"/CancelarReserva"})
public class CancelarReserva extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("emailReserva");
        int idSala = Integer.parseInt(request.getParameter("idSala"));
        Timestamp horaReserva = Timestamp.valueOf(request.getParameter("horaReserva"));

        boolean success = Logic.cancelarReserva(email, idSala, horaReserva);

        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Reserva cancelada con éxito.");
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error al cancelar la reserva.");
        }

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
