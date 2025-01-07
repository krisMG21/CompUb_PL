<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Verificar si el usuario está autenticado
    String userEmail = (String) session.getAttribute("email");
    if (userEmail == null) {
        response.sendRedirect("MenuInicio.jsp");
        return; 
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>    
    <link rel="icon" href="Fotos/favicon.png" type="image/png">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Datos de Sensores de Todos los Cubículos</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e6f2ff;
            margin: 0;
            padding: 20px;
            color: #2c3e50;
        }
        .container {
            max-width: 1000px;
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
        <h1>Datos de Sensores de Todos los Cubículos</h1>
        <table id="lecturasTable">
            <thead>
                <tr>
                    <th>ID de Cubículo</th>
                    <th>Temperatura</th>
                    <th>Humedad</th>
                    <th>Sonido</th>
                    <th>Luz</th> <!-- Nueva columna para Luz -->
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
    document.addEventListener('DOMContentLoaded', function () {
        fetch('/BibliotecaEPS/InfCubiculo') // Cambia la URL según tu servlet
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                populateUltimasLecturas(data);
            })
            .catch(error => {
                console.error("Error:", error);
                document.getElementById('error').textContent = "Error: " + error.message;
                document.getElementById('error').style.display = 'block';
            });
    });

    function populateUltimasLecturas(lecturas) {
        const lecturasTable = document.getElementById('lecturasTable').getElementsByTagName('tbody')[0];
        const alertasContainer = document.createElement('div'); // Contenedor para todas las alertas
        alertasContainer.id = 'alertas';
        alertasContainer.style.marginTop = '20px';
        alertasContainer.style.padding = '10px';
        document.body.appendChild(alertasContainer);

        lecturasTable.innerHTML = ""; // Limpiar la tabla
        alertasContainer.innerHTML = ""; // Limpiar alertas

        if (!Array.isArray(lecturas)) {
            lecturas = [lecturas];
        }

        lecturas.forEach(lectura => {
            let row = lecturasTable.insertRow();
            row.insertCell(0).textContent = lectura.idCubiculo;
            row.insertCell(1).textContent = formatLecturaValue("Temperatura", lectura.temperatura);
            row.insertCell(2).textContent = formatLecturaValue("Humedad", lectura.humedad);
            row.insertCell(3).textContent = formatLecturaValue("Sonido", lectura.sonido);
            row.insertCell(4).textContent = formatLecturaValue("Luz", lectura.luz); // Agregar valor de luz
            row.insertCell(5).textContent = new Date(lectura.fechaHora).toLocaleString();

            // Check for temperature alerts
            addTemperatureAlert(alertasContainer, lectura.idCubiculo, lectura.temperatura);
        });
    }

    function formatLecturaValue(tipoSensor, valor) {
        if (valor === null || valor === undefined) return "N/A";
        switch(tipoSensor) {
            case "Temperatura": return valor + " °C";
            case "Humedad": return valor + " %";
            case "Sonido": return valor + " dB";
            case "Luz": return valor + " lux"; // Formato para la luz
            default: return valor;
        }
    }

    function addTemperatureAlert(container, idCubiculo, temperatura) {
        let alertMessage = "";
        if (temperatura > 27) {
            alertMessage = `⚠️ Cubículo ${idCubiculo}: ¡Hace mucho calor! La temperatura ha superado los 27°C (${temperatura}°C).`;
        } else if (temperatura < 17) {
            alertMessage = `⚠️ Cubículo ${idCubiculo}: ¡Hace mucho frío! La temperatura está por debajo de los 17°C (${temperatura}°C).`;
        }

        if (alertMessage) {
            const alertDiv = document.createElement('div');
            alertDiv.textContent = alertMessage;
            alertDiv.style.marginBottom = '10px';
            alertDiv.style.padding = '10px';
            alertDiv.style.borderRadius = '8px';
            alertDiv.style.backgroundColor = temperatura > 27 ? '#f8d7da' : '#d4edda';
            alertDiv.style.color = temperatura > 27 ? '#721c24' : '#155724';
            container.appendChild(alertDiv);
        }
    }
</script>

</body>
</html>
