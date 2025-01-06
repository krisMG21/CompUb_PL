<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <link rel="icon" href="Fotos/favicon.png" type="image/png">

    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de la Biblioteca</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h1>Gestión de la Biblioteca</h1>

        <!-- Tabla de cubículos ocupados -->
        <div class="availability">
            <h2>Cubículos Ocupados</h2>
            <table id="cubiculosTable">
                <thead>
                    <tr>
                        <th>ID Cubículo</th>
                        <th>Nombre</th>
                        <th>Ocupante</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Content dynamically added -->
                </tbody>
            </table>

            <h2>Salas Reservadas en Tiempo Real</h2>
            <table id="salasTable">
                <thead>
                    <tr>
                        <th>ID Sala</th>
                        <th>Nombre</th>
                        <th>Ocupante</th>
                        <th>Hora de Reserva</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Content dynamically added -->
                </tbody>
            </table>
        </div>

        <!-- Gráfico -->
        <div class="chart-container">
            <h3>Grado de Ocupación de Cubículos</h3>
            <canvas id="ocupacionRosco" width="400" height="400"></canvas>
        </div>
    </div>
    <script src="Scripts/EstadisticasGestor.js"></script>
</body>
</html>
