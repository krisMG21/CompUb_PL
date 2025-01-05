<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>

<%
    String username = (String) session.getAttribute("email");
    if (username == null) {
        response.sendRedirect("MenuInicio.jsp");
        return; 
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Gestor</title>
    <style>
        /* General body styles */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e1f5fe; /* Azul pastel de fondo */
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            box-sizing: border-box;
        }

        /* Title styling */
        h1 {
            color: #0277bd; /* Azul más oscuro para el título */
            margin-bottom: 30px;
            font-size: 2.5rem;
            font-weight: 600;
        }

        /* Container for buttons */
        .menu-container {
            display: flex;
            flex-direction: column;
            gap: 25px;
            align-items: center;
        }

        /* Button styling */
        button {
            padding: 15px 30px;
            font-size: 18px;
            background-color: #81d4fa; /* Azul pastel para los botones */
            color: #fff;
            border: 2px solid #4fc3f7;
            border-radius: 12px;
            cursor: pointer;
            width: 280px;
            text-align: center;
            transition: all 0.3s ease;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        /* Hover effect for buttons */
        button:hover {
            background-color: #4fc3f7;
            transform: translateY(-3px);
        }

        /* Active button effect */
        button:active {
            background-color: #0288d1;
            transform: translateY(1px);
        }

        /* Responsive styles */
        @media (max-width: 600px) {
            h1 {
                font-size: 2rem;
            }

            button {
                width: 220px;
            }
        }
    </style>
</head>
<body>

        <h1>Bienvenido,<%= session.getAttribute("email") %></h1> <!--CAMBIAR POR EL  VERDADERO NOMBRE DEL USUUARIO-->

        <div class="menu-container">
            <button onclick="window.location.href='gestion.html'">Gestión de Salas </button>
            <button onclick="window.location.href='gestion.html'">Gestión de puestos de lectura </button>
            <button onclick="window.location.href='MapaBiblioteca.jsp'">Mapa de la bilioteca </button>
            <button onclick="window.location.href='https://uah.es/es/'">Información </button>
            <button onclick="window.location.href='estadisticas.html'">Estadísticas</button>
        </div>

</body>
</html>
