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
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #b3cde0; /* Azul pastel */
                color: #333;
                margin: 0;
                padding: 0;
            }
            .container {
                width: 80%;
                margin: 0 auto;
                padding: 30px;
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
            h1 {
                text-align: center;
                color: #5a6f77;
            }
            h2, h3 {
                color: #4a5568;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin: 20px 0;
            }
            th, td {
                padding: 12px;
                text-align: center;
                border: 1px solid #ddd;
            }
            th {
                background-color: #4a90e2;
                color: white;
            }
            tr:nth-child(even) {
                background-color: #f2f2f2;
            }
            .chart-container {
                text-align: center;
                margin: 30px 0;
            }
            .button-container {
                text-align: center;
                margin: 20px 0;
            }
            .button {
                background-color: #4a90e2;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-size: 16px;
                margin: 5px;
            }
            .button:hover {
                background-color: #357ab7;
            }
        </style>
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

            <!-- Botones de más información -->
            <div class="button-container">
                <button class="button" onclick="redirigirACubiculos()">Más Información sobre Cubículos</button>
                <button class="button" onclick="redirigirASalas()">Más Información sobre Salas</button>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                // Fetch data from the servlet
                fetch("/BibliotecaEPS/EstadisticasGestor")
                        .then(response => response.json())
                        .then(data => {
                            populateCubiculosTable(data.cubiculos);
                            populateSalasTable(data.salas);
                            createDoughnutChart('ocupacionRosco', data.ocupacion.ocupados, data.ocupacion.disponibles);
                            handleFullOccupancyAlert(data.ocupacion.ocupados, data.ocupacion.disponibles);
                        })
                        .catch(error => {
                            console.error('Error al obtener las estadísticas:', error);
                            alert('Ocurrió un error al cargar las estadísticas.');
                        });
            });

            // Populate cubicles table
            function populateCubiculosTable(cubiculos) {
                const cubiculosTable = document.getElementById('cubiculosTable').getElementsByTagName('tbody')[0];
                cubiculosTable.innerHTML = ""; // Clear the table
                cubiculos.forEach(cubiculo => {
                    let row = cubiculosTable.insertRow();
                    row.insertCell(0).textContent = cubiculo.id;
                    row.insertCell(1).textContent = cubiculo.nombre;
                });
            }

            // Populate rooms table
            function populateSalasTable(salas) {
                const salasTable = document.getElementById('salasTable').getElementsByTagName('tbody')[0];
                salasTable.innerHTML = ""; // Limpiar la tabla
                const now = new Date(); // Obtener la fecha y hora actuales

                // Solo comparamos las reservas que sean para el mismo día y la misma hora
                salas.forEach(sala => {
                    const reservaHora = new Date(sala.horaReserva); // Hora de la reserva

                    // Comparamos solo si la reserva es en el mismo día y la misma hora (ignorando minutos y segundos)
                    if (reservaHora.getFullYear() === now.getFullYear() && // Año
                            reservaHora.getMonth() === now.getMonth() && // Mes
                            reservaHora.getDate() === now.getDate() && // Día
                            reservaHora.getHours() === now.getHours()) { // Hora (ignorando minutos y segundos)
                        let row = salasTable.insertRow();
                        row.insertCell(0).textContent = sala.id; // ID de la sala
                        row.insertCell(1).textContent = sala.nombre; // Nombre de la sala
                        row.insertCell(2).textContent = sala.email; // Correo del ocupante
                        row.insertCell(3).textContent = reservaHora.toLocaleTimeString(); // Hora de la reserva
                    }
                });
            }


            // Display alert if occupancy is at full capacity
            function handleFullOccupancyAlert(ocupados, disponibles) {
                const total = ocupados + disponibles;
                const porcentajeOcupado = (ocupados / total) * 100;
                if (porcentajeOcupado === 100) {
                    alert('¡A full! Si encuentras algo mal aprovechado, ¡apúrate y reorganiza el lugar! Que no todo el mundo deja sus cosas y se va... ¿o sí?');
                }
            }

            // Create doughnut chart
            function createDoughnutChart(canvasId, ocupados, disponibles) {
                const ctx = document.getElementById(canvasId).getContext('2d');
                const chartData = {
                    labels: ['Ocupado', 'Disponible'],
                    datasets: [{
                            data: [ocupados, disponibles],
                            backgroundColor: ['#FF6347', '#90EE90'], // Red for occupied, green for available
                            hoverBackgroundColor: ['#FF4500', '#32CD32'],
                            borderWidth: 1
                        }]
                };

                new Chart(ctx, {
                    type: 'doughnut',
                    data: chartData,
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'top'
                            },
                            tooltip: {
                                callbacks: {
                                    label: function (tooltipItem) {
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
            }

            // Function for redirecting to the cubicles page
            function redirigirACubiculos() {
                window.location.href = "InfCG.jsp";  // Replace with your actual JSP path
            }

            // Function for redirecting to the rooms page
            function redirigirASalas() {
                window.location.href = "InfSG.jsp";  // Replace with your actual JSP path
            }
        </script>
    </body>
</html>
