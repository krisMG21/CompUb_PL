<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <!-- Se define el conjunto de caracteres a UTF-8, que soporta caracteres especiales. -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- La etiqueta meta viewport ayuda a que la página sea responsiva en dispositivos móviles. -->
    <title>Iniciar sesión</title>
    <!-- Título que se muestra en la pestaña del navegador. -->
    <style>
        /* Estilos CSS para dar formato y diseño a la página. */
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f8ff; 
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
            color: #4682b4;
        }
        form {
            background-color: #ffffff; 
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
<link rel="icon" href="Fotos/favicon.png" type="image/png">

<body>
    <!-- Imagen decorativa, se muestra en la esquina superior derecha -->
    <img src="Fotos/LogoUAH.png" alt="Logo" class="header-image">

    <div>
        <!-- Título de la página -->
        <h2>Iniciar sesión</h2>
        
        <!-- Formulario para ingresar las credenciales -->
        <form action="Login" method="POST">
            <!-- Campo para el email del usuario -->
            <label for="username">Email:</label>
            <input type="text" id="username" name="username" required><br>

            <!-- Campo para la contraseña del usuario -->
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required><br>

            <!-- Botón para enviar el formulario -->
            <button type="submit">Iniciar sesión</button>
        </form>

        <!-- para mostrar mensajes de error, si el servlet genera alguno -->
        <div id="errorMessage">
            <!-- Aquí se muestra el error si existe (en caso de que el servlet lo mande) -->
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
    </div>

</body>
</html>
