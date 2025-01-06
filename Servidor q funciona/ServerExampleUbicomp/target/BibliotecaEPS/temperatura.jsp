<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Datos de Temperatura</title>
    <style>
        /* ... (keep the existing styles) ... */
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
                    populateUltimasLecturas([data]); // Wrap data in an array to match the expected format
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
