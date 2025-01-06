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
    <title>Estadísticas de la Biblioteca</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="styles.css">

    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f0f8ff; /* Azul pastel suave */
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            flex-direction: column;
            min-height: 100vh; /* Garantiza que el body tenga al menos el tamaño de la ventana */
            overflow-x: hidden; /* Evitar desplazamiento horizontal */
        }

        .container {
            width: 90%;
            max-width: 1200px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
            margin-bottom: 20px; /* Deja espacio para scroll */
            overflow: hidden;
        }

        h1 {
            color: #4682b4; /* Azul pastel */
            font-size: 2rem;
            margin-bottom: 20px;
        }

        h2, h3 {
            color: #4682b4;
            font-size: 1.5rem;
            margin-bottom: 10px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            border: 1px solid #c6d9f1;
            text-align: left;
            font-size: 1rem;
        }

        th {
            background-color: #e6f0ff; /* Azul pastel claro */
        }

        tr:nth-child(even) {
            background-color: #f7f7f7;
        }

        tr:hover {
            background-color: #e1f0ff; /* Azul pastel suave al pasar el ratón */
        }

        .chart-container {
            margin-top: 30px;
            padding: 20px;
            background-color: #f7f7f7;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .alert {
            display: none;
            background-color: #ffcccc;
            color: #ff0000;
            padding: 15px;
            margin-top: 20px;
            border-radius: 5px;
            font-size: 1.2rem;
        }

        .emoji {
            font-size: 2rem;
        }

        button {
            background-color: #4682b4;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 1rem;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 20px;
        }

        button:hover {
            background-color: #5a9bd6;
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
    </div>

    <script src="Scripts/EstadisticasCliente.js"></script>

</body>
</html>
