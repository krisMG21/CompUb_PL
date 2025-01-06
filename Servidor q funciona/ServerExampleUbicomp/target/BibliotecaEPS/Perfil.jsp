<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Información del Usuario</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e1f5fe;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        h1 {
            color: #0277bd;
            margin: 10px 0;
        }

        .info-container {
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            text-align: center;
        }

        .info-container p {
            font-size: 18px;
        }

        .button-container {
            margin-top: 20px;
        }

        button {
            padding: 10px 20px;
            font-size: 16px;
            background-color: #81d4fa;
            color: #fff;
            border: 2px solid #4fc3f7;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        button:hover {
            background-color: #4fc3f7;
        }

        button:active {
            background-color: #0288d1;
        }
    </style>
    <script>
        // Realizar una solicitud AJAX para obtener los datos del usuario
        document.addEventListener("DOMContentLoaded", function() {
            fetch("PerfilUsuarioServlet")
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Error al obtener los datos del usuario.");
                    }
                    return response.json();
                })
                .then(data => {
                    // Mostrar los datos del usuario en la página
                    document.getElementById("nombre").textContent = data.nombre;
                    document.getElementById("email").textContent = data.correo;
                })
                .catch(error => {
                    console.error("Error:", error);
                    document.getElementById("info-container").innerHTML = `
                        <p>Error al cargar la información del usuario. Por favor, inténtelo más tarde.</p>`;
                });
        });
    </script>
</head>
<body>
    <h1>Información del Usuario</h1>
    <div class="info-container" id="info-container">
        <p><strong>Nombre y Apellidos:</strong> <span id="nombre">Cargando...</span></p>
        <p><strong>Email:</strong> <span id="email">Cargando...</span></p>
    </div>
</body>
</html>
