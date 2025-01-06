<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Datos de Temperatura</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e6f2ff;
            margin: 0;
            padding: 20px;
            color: #2c3e50;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #f0f7ff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            color: #3498db;
            margin-bottom: 30px;
            font-weight: 300;
        }
        #lecturasTable {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            background-color: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        #lecturasTable thead {
            background-color: #5bc0de;
            color: white;
        }
        #lecturasTable th, #lecturasTable td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #e6f2ff;
        }
        #lecturasTable tbody tr:nth-child(even) {
            background-color: #f0f7ff;
        }
        #lecturasTable tbody tr:hover {
            background-color: #e6f2ff;
            transition: background-color 0.3s ease;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
        }
        #raw-data {
            background-color: #f1f8ff;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            font-family: monospace;
            white-space: pre-wrap;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Datos de Temperatura</h1>
        <table id="lecturasTable">
            <thead>
                <tr>
                    <th>Tipo de Sensor</th>
                    <th>Valor</th>
                    <th>ID de Sala</th>
                    <th>Fecha y Hora</th>
                </tr>
            </thead>
            <tbody>
                <!-- Data will be inserted here -->
            </tbody>
        </table>
        <div id="error" class="error" style="display: none;"></div>
        <div id="raw-data"></div>
    </div>

    <script>
        console.log("Script iniciado");

        document.addEventListener('DOMContentLoaded', function () {
            fetch('/BibliotecaEPS/Temperatura')
                .then(response => {
                    console.log("Respuesta recibida:", response);
                    if (!response.ok) {
                        throw new Error('Error en la respuesta del servidor: ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("Datos recibidos:", data);
                    document.getElementById('raw-data').textContent = "Datos crudos recibidos: " + JSON.stringify(data);
                    populateUltimasLecturas([data]);
                })
                .catch(error => {
                    console.error("Error:", error);
                    document.getElementById('error').textContent = "Error: " + error.message;
                    document.getElementById('error').style.display = 'block';
                });
        });

        function populateUltimasLecturas(lecturas) {
            const lecturasTable = document.getElementById('lecturasTable').getElementsByTagName('tbody')[0];
            lecturasTable.innerHTML = ""; // Clear the table
            lecturas.forEach(lectura => {
                let row = lecturasTable.insertRow();
                row.insertCell(0).textContent = "Temperatura";
                row.insertCell(1).textContent = formatLecturaValue("Temperatura", lectura.temperatura);
                row.insertCell(2).textContent = lectura.idSala;
                row.insertCell(3).textContent = new Date(lectura.fechaHora).toLocaleString();
            });
        }

        function formatLecturaValue(tipoSensor, valor) {
            if (tipoSensor === "Temperatura") {
                return valor + " °C";
            }
            return valor;
        }
    </script>
</body>
</html>
