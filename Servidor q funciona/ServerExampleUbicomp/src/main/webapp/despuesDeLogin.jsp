<%-- 
    Document   : depuesDeLogin
    Created on : 4 ene 2025, 21:13:33
    Author     : fatim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>

<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("MenuInicio.jsp");
        return; 
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <h1>Buenassss, <%= session.getAttribute("username") %>!</h1>
        <div id="divOcultoUsuario" style="display:none;"><%= session.getAttribute("username") %></div>
    </body>
</html>
