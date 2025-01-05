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
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            color: #333;
        }
        .container {
            max-width: 1000px;
            margin: 20px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #4CAF50;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: center;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        .chart-container {
            margin-top: 40px;
            text-align: center;
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
                    <!-- Las filas de cubículos libres se insertarán aquí mediante JavaScript -->
                </tbody>
            </table>

            <!-- Tabla de salas libres -->
            <h2>Salas Libres</h2>
            <table id="salasTable">
                <thead>
                    <tr>
                        <th>ID Sala</th>
                        <th>Nombre</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Las filas de salas libres se insertarán aquí mediante JavaScript -->
                </tbody>
            </table>
        </div>

        <!-- Sección para gráficos -->
        <div class="chart-container">
            <h3>Estadísticas de Reservas</h3>
            <p>Aquí se podrían incluir gráficos de estadísticas (ej. número de reservas por hora, etc.).</p>
        </div>
    </div>

    <script>
        // Ejecutar cuando se cargue la página
        window.onload = function() {
            // Realizar solicitud al servlet
            fetch("/BibliotecaEPS/EstadisticasCliente")
                .then(response => response.json())
                .then(data => {
                    // Insertar cubículos libres en la tabla
                    const cubiculosTable = document.getElementById('cubiculosTable').getElementsByTagName('tbody')[0];
                    cubiculosTable.innerHTML = ""; // Limpiar la tabla
                    data.cubiculos.forEach(cubiculo => {
                        let row = cubiculosTable.insertRow();
                        row.insertCell(0).textContent = cubiculo.id;
                        row.insertCell(1).textContent = cubiculo.nombre;
                    });

                    // Insertar salas libres en la tabla
                    const salasTable = document.getElementById('salasTable').getElementsByTagName('tbody')[0];
                    salasTable.innerHTML = ""; // Limpiar la tabla
                    data.salas.forEach(sala => {
                        let row = salasTable.insertRow();
                        row.insertCell(0).textContent = sala.id;
                        row.insertCell(1).textContent = sala.nombre;
                    });
                })
                .catch(error => {
                    console.error('Error al obtener las estadísticas:', error);
                    alert('Ocurrió un error al cargar las estadísticas.');
                });
        };
    </script>
</body>
</html>
