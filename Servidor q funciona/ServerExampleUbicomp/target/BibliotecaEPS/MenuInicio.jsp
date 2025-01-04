<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f8ff; /* Azul pastel */
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            position: relative;
        }
        h2 {
            text-align: center;
            color: #4682b4; /* Azul acero */
        }
        form {
            background-color: #ffffff; /* Fondo blanco */
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 300px;
        }
        label {
            font-weight: bold;
            color: #4682b4;
        }
        input {
            width: calc(100% - 10px);
            padding: 8px;
            margin: 5px 0 15px;
            border: 1px solid #c6d9f1;
            border-radius: 5px;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #4682b4;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #5a9bd6;
        }
        #errorMessage {
            margin-top: 10px;
            text-align: center;
            color: red;
        }
        .header-image {
            position: absolute;
            top: 10px;
            right: 10px;
            width: 100px;
            height: auto;
        }
    </style>
</head>
<body>
    <!-- Imagen decorativa -->
    <img src="Fotos/LogoUAH.png" alt="Logo" class="header-image">

    <div>
        <h2>Iniciar sesión</h2>
        <!--<form id="FormularioLogin" method="POST">-->
        <form action="LoginServlet" method="POST">
            <label for="username">Email:</label>
            <input type="text" id="username" name="username" required><br>

            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required><br>

            <button type="submit">Iniciar sesión</button>
        </form>

        <!-- Área para mostrar mensajes de error -->
        <div id="errorMessage">
            <!-- Si el servlet manda un error, se muestra aquí -->
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
    </div>

    <!--<script src="./Scripts/FormularioLogin.js"></script>-->
</body>
</html>
