/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import db.ConnectionDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.annotation.WebServlet;
import java.sql.Timestamp;

@WebServlet(name = "Reserva", urlPatterns = {"/Reserva"})
public class Reserva extends HttpServlet{
    
    private static final long serialVersionUID = 1L;
    
    public Reserva(){
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
        
        try{
            // Obtener la conexión a la base de datos
            conexionBD = connectionDB.obtainConnection(true);
            
            // Leer los parámetros enviados desde el formulario
            String user = request.getParameter("username");
            String passw = request.getParameter("password");
        } finally {
            // En vez de esto un catch con el error adecuao
            
        }
    }
}
