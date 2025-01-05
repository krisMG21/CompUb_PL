<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mapa EPS UAH</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #b3d9ff; /* Azul pastel */
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .main-container {
            width: 90%;
            max-width: 800px; /* Reducción del ancho máximo del contenedor */
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .map-container {
            width: 100%;
            height: auto;
            display: flex;
            justify-content: center;
            align-items: center;
            overflow: hidden;
        }

        .map-container img {
            max-width: 80%; /* La imagen ocupará un 80% del contenedor */
            max-height: 80%; /* Limita también la altura */
            object-fit: contain; /* Mantiene la proporción */
            border-radius: 10px;
        }
    </style>
</head>
<body>
    <!-- Contenedor principal -->
    <div class="main-container">
        <!-- Contenedor para mostrar el mapa -->
        <div class="map-container">
            <img src="<%= request.getContextPath() %>/Fotos/MapaBiblioteca.jpg" alt="Mapa">
        </div>
    </div>
</body>
</html>
