<!DOCTYPE html>
<html lang="es">
<head>
    <link rel="icon" href="Fotos/favicon.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Datos de los Sensores</title>
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
        .table-header {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .table-header img {
            width: 24px;
            height: 24px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Datos de Sensores de Todas las Salas</h1>
        <div class="table-header">
            <h2>Lecturas de Sensores</h2>
        </div>
        <table id="lecturasTable">
            <thead>
                <tr>
                    <th>ID de Sala</th>
                    <th>Temperatura</th>
                    <th>Humedad</th>
                    <th>Sonido</th>
                    <th>Fecha y Hora</th>
                </tr>
            </thead>
            <tbody>
                <!-- Data will be inserted here -->
            </tbody>
        </table>
        <div id="error" class="error" style="display: none;"></div>
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
        lecturasTable.innerHTML = ""; // Clear the table

        // Verificar si lecturas es un array
        if (!Array.isArray(lecturas)) {
            lecturas = [lecturas]; // Si no es un array, convertirlo en uno
        }

        let alertas = []; // Almacenar alertas para temperaturas fuera del rango

        lecturas.forEach(lectura => {
            let row = lecturasTable.insertRow();
            row.insertCell(0).textContent = lectura.idSala;
            const temperatura = lectura.temperatura;
            row.insertCell(1).textContent = formatLecturaValue("Temperatura", temperatura);
            row.insertCell(2).textContent = formatLecturaValue("Humedad", lectura.humedad);
            row.insertCell(3).textContent = formatLecturaValue("Sonido", lectura.sonido);
            row.insertCell(4).textContent = new Date(lectura.fechaHora).toLocaleString();

            // Verificar si la temperatura está fuera del rango
            if (temperatura > 30 || temperatura < 10) {
                alertas.push(`Sala ${lectura.idSala}: Temperatura fuera del rango (${temperatura} °C)`);
                row.style.backgroundColor = "#f8d7da"; // Resaltar fila en caso de alerta
            }
        });

        if (alertas.length > 0) {
            showAlertas(alertas);
        }
    }

    function formatLecturaValue(tipoSensor, valor) {
        if (valor === null || valor === undefined) return "N/A";
        switch (tipoSensor) {
            case "Temperatura": return valor + " °C";
            case "Humedad": return valor + " %";
            case "Sonido": return valor + " dB";
            default: return valor; // Manejar cualquier otro tipo si es necesario
        }
    }

    function showAlertas(alertas) {
        const alertaDiv = document.createElement('div');
        alertaDiv.className = 'error';
        alertaDiv.innerHTML = `<strong>¡Atención!</strong><br>` + alertas.join('<br>');
        document.querySelector('.container').prepend(alertaDiv);
    }
</script>

</body>
</html>
