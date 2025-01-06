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
    <style>
        /* Estilo Global */
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f0f8ff; /* Azul pastel */
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: #ffffff; /* Blanco para el contenido */
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #4682b4; /* Azul acero */
            font-size: 36px;
        }

        h2, h3 {
            color: #4682b4; /* Azul pastel para títulos secundarios */
            font-size: 28px;
        }

        /* Tablas */
        table {
            width: 100%;
            margin: 20px 0;
            border-collapse: collapse;
            background-color: #f9f9f9;
            border-radius: 8px;
            overflow: hidden;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #b0e0e6; /* Azul pálido */
            color: #ffffff;
        }
        tr:nth-child(even) {
            background-color: #f1f1f1;
        }

        /* Contenedor del gráfico */
        .chart-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 40px;
            flex-direction: column; /* Asegura que el gráfico y título estén alineados correctamente */
        }
        canvas {
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 100%; /* Hace que el gráfico sea responsivo */
        }

        /* Alertas */
        .alert {
            background-color: #ffeb3b;
            color: #555555;
            padding: 15px;
            border-radius: 10px;
            margin-top: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 18px;
        }

        .emoji {
            font-size: 24px;
            margin-right: 10px;
        }

        /* Estilo para la disponibilidad */
        .availability {
            margin-bottom: 40px;
        }

        /* Botones */
        .button {
            background-color: #4682b4;
            color: #fff;
            padding: 10px 20px;
            border-radius: 5px;
            border: none;
            font-size: 16px;
            cursor: pointer;
            text-align: center;
        }

        .button:hover {
            background-color: #5f9ea0; /* Azul más oscuro */
        }
    </style>
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

        <!-- Botón de acción (opcional) -->
        <button class="button" onclick="location.href='MenuInicio.jsp'">Regresar al Menú Principal</button>
    </div>
    <script src="Scripts/EstadisticasCliente.js"></script>
</body>
</html>
