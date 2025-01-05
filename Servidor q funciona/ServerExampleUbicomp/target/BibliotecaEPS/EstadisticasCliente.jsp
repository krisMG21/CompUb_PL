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
        /* Estilos generales */
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #E8F6FD; /* Fondo azul pastel */
            color: #444;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1100px;
            margin: 40px auto;
            padding: 30px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #4A90E2;
            font-size: 2.5em;
            margin-bottom: 20px;
        }

        /* Tabla de cubículos y salas */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 40px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 12px;
            text-align: center;
        }
        th {
            background-color: #4A90E2;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }

        /* Contenedor de gráficos */
        .chart-container {
            margin-top: 50px;
            text-align: center;
        }

        /* Estilo para el mensaje simpático */
        .alert {
            background-color: #A8D0E6; /* Azul pastel */
            color: #333;
            padding: 25px;
            border-radius: 12px;
            text-align: center;
            font-size: 1.2em;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            margin-top: 30px;
        }
        .alert .emoji {
            font-size: 35px;
            margin: 0 10px;
        }
        .alert p {
            margin: 0;
            font-weight: bold;
        }

        /* Estilos adicionales para el gráfico de rosco */
        .chart-container h3 {
            font-size: 1.8em;
            color: #4A90E2;
            margin-bottom: 20px;
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
            <h3>Grado de Ocupación de Cubículos</h3>
            <canvas id="ocupacionRosco" width="400" height="400"></canvas>
        </div>

        <!-- Sección de alerta -->
        <div id="alertMessage" class="alert" style="display:none;">
            <span class="emoji">😅</span> Ufff, ¡Cuánta gente! <span class="emoji">📚</span><br>
            Entendemos que quizás quieras ir a otra biblioteca, ¡ánimo! 
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

                    // Datos para el gráfico de rosco (ocupación)
                    const ocupados = data.ocupacion.ocupados;
                    const disponibles = data.ocupacion.disponibles;

                    // Calcular el porcentaje de ocupación
                    const total = ocupados + disponibles;
                    const porcentajeOcupado = (ocupados / total) * 100;

                    // Si la ocupación supera el 70%, mostrar el mensaje
                    if (porcentajeOcupado > 90) {
                        document.getElementById('alertMessage').style.display = 'block';
                    }

                    // Datos para el gráfico de rosco
                    const chartData = {
                        labels: ['Ocupado', 'Disponible'],
                        datasets: [{
                            data: [ocupados, disponibles],
                            backgroundColor: ['#FF6347', '#90EE90'],  // Rojo para ocupado, verde para disponible
                            hoverBackgroundColor: ['#FF4500', '#32CD32'],
                            borderWidth: 1
                        }]
                    };

                    // Crear el gráfico de rosco
                    const ctx = document.getElementById('ocupacionRosco').getContext('2d');
                    const ocupacionRosco = new Chart(ctx, {
                        type: 'doughnut',  // Tipo de gráfico: rosco
                        data: chartData,
                        options: {
                            responsive: true,
                            plugins: {
                                legend: {
                                    position: 'top',  // Posición de la leyenda
                                },
                                tooltip: {
                                    callbacks: {
                                        label: function(tooltipItem) {
                                            const dataset = tooltipItem.dataset;
                                            const total = dataset.data.reduce((sum, value) => sum + value, 0);
                                            const currentValue = dataset.data[tooltipItem.dataIndex];
                                            const percentage = Math.round((currentValue / total) * 100);
                                            return currentValue + ' (' + percentage + '%)';
                                        }
                                    }
                                }
                            }
                        }
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
