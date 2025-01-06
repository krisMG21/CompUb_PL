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
    <title>Puestos de lectura</title>
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
            align-items: center;
            height: 100vh;
            flex-direction: column;
        }

        .container {
            width: 90%;
            max-width: 1000px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h1 {
            color: #4682b4; /* Azul pastel */
            font-size: 2rem;
            margin-bottom: 20px;
        }

        h2 {
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
        <h1>Puestos de lectura</h1>

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
                    <!-- Contenido dinámicamente añadido -->
                </tbody>
            </table>
        </div>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            // Fetch data from the servlet
            fetch("/BibliotecaEPS/EstadisticasCliente")
                .then(response => response.json())
                .then(data => {
                    // Llenar la tabla de cubículos con los datos recibidos
                    populateTable('cubiculosTable', data.cubiculos);
                })
                .catch(error => {
                    console.error('Error al obtener las estadísticas:', error);
                    alert('Ocurrió un error al cargar las estadísticas.');
                });
        });

        // Función para llenar la tabla con los cubículos libres
        function populateTable(tableId, cubiculos) {
            const tableBody = document.getElementById(tableId).getElementsByTagName('tbody')[0];
            tableBody.innerHTML = ""; // Limpiar la tabla
            cubiculos.forEach(cubiculo => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = cubiculo.id;
                row.insertCell(1).textContent = cubiculo.nombre;
            });
        }
    </script>
</body>
</html>
