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
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estadísticas de la Biblioteca</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h1>Estadísticas de la Biblioteca</h1>

        <!-- Tabla de cubículos libres -->
        <div class="availability">
            <h2>Cubículos Libres</h2>
            <table id="cubiculosTable">
                <thead>
                    <tr>
                        <th>ID Cubículo</th>
                        <th>Nombre</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Content dynamically added -->
                </tbody>
            </table>

            <h2>Salas Libres</h2>
            <table id="salasTable">
                <thead>
                    <tr>
                        <th>ID Sala</th>
                        <th>Nombre</th>
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

        <div id="alertMessage" class="alert" style="display:none;">
            <span class="emoji">😅</span> Ufff, ¡Cuánta gente! <span class="emoji">📚</span><br>
            Entendemos que quizás quieras ir a otra biblioteca, ¡ánimo!
        </div>
    </div>
    <script src="Scripts/EstadisticasCliente.js"></script>
</body>
</html>
