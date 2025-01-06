<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <link rel="icon" href="Fotos/favicon.png" type="image/png">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserva de Salas</title>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- jQuery UI -->
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    
    <style>
        /* Estilos para hacer la interfaz más bonita y en tonos azul pastel */
        body {
            font-family: Arial, sans-serif;
            background-color: #f1f8fc;
            margin: 0;
            padding: 0;
        }

        .contenedor {
            display: flex;
            justify-content: space-between;
            padding: 20px;
        }

        .container {
            background-color: #e0f7fa;
            border-radius: 8px;
            padding: 20px;
            width: 45%;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
        }

        h1, h2 {
            color: #0277bd;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #01579b;
        }

        input, select {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
            border: 1px solid #0288d1;
        }

        input[type="submit"] {
            background-color: #0288d1;
            color: white;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #0277bd;
        }

        .mensaje {
            padding: 10px;
            margin-top: 15px;
            border-radius: 5px;
            display: none;
        }

        .exito {
            background-color: #81c784;
            color: #2e7d32;
        }

        .error {
            background-color: #e57373;
            color: #d32f2f;
        }

        #listaReservas ul {
            list-style-type: none;
            padding-left: 0;
        }

        #listaReservas li {
            padding: 10px;
            margin-bottom: 10px;
            background-color: #bbdefb;
            border-radius: 6px;
            display: flex;
            justify-content: space-between;
        }

        #listaReservas button {
            background-color: #d32f2f;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
            border-radius: 4px;
        }

        #listaReservas button:hover {
            background-color: #c62828;
        }

.imagen-lateral {
    width: 100%;
    max-width: 400px;  /* Ajusta este valor para controlar el tamaño máximo */
    height: 350px;  /* Ajusta la altura según lo que necesites */
    object-fit: cover;  /* Asegura que la imagen mantenga sus proporciones */
    border-radius: 10px;
}

    </style>
</head>
<body>
    <div class="contenedor">
        <!-- Formulario de reserva -->
        <div class="container">
            <h1>Reserva de Salas</h1>
            <form id="reservaForm">
                <label for="sala">Selecciona una sala:</label>
                <select id="sala" name="idSala" required>
                    <option value="">--Selecciona una sala--</option>
                    <option value="1">Sala 1</option>
                    <option value="2">Sala 2</option>
                </select>

                <label for="fecha">Selecciona una fecha:</label>
                <input type="text" id="fecha" name="fecha" required>

                <label for="hora">Selecciona una hora:</label>
                <select id="hora" name="hora" required>
                    <%
                    for (int i = 8; i <= 20; i++) {
                        String hora = String.format("%02d:00", i);
                    %>
                        <option value="<%= hora %>"><%= hora %></option>
                    <% } %>
                </select>

                <input type="submit" value="Reservar Sala">
            </form>
            <div id="mensajeReserva" class="mensaje"></div>
        </div>

        <!-- Ver y cancelar reservas -->
        <div class="container">
            <h2>Mis Reservas</h2>
            <button id="verReservas">Ver Mis Reservas</button>
            <div id="listaReservas"></div>
        </div>

        <!-- Imagen lateral -->
        <img src="Fotos/MapaBibliotecaN.png" alt="Imagen Lateral" class="imagen-lateral">
    </div>

<script>
    $(function() {
        var diasNoLaborables = ["2025-01-01", "2025-12-25"]; // Días festivos

        $("#fecha").datepicker({
            dateFormat: "yy-mm-dd",
            minDate: 0, // Solo permitir fechas futuras
            beforeShowDay: function(date) {
                var day = date.getDay();
                var formattedDate = $.datepicker.formatDate('yy-mm-dd', date);
                if (day === 0 || day === 6 || diasNoLaborables.includes(formattedDate)) {
                    return [false, "disabled-date", "No disponible"];
                }
                return [true, "enabled-date", "Disponible"];
            }
        });

        $("#reservaForm").on('submit', function(e) {
            e.preventDefault();
            var sala = $("#sala").val();
            var fecha = $("#fecha").val();
            var hora = $("#hora").val();
            var email = '<%= session.getAttribute("email") != null ? session.getAttribute("email") : "" %>';

            if (!sala || !fecha || !hora) {
                $("#mensajeReserva").html("Por favor, completa todos los campos.")
                    .removeClass("exito").addClass("error").fadeIn();
                return;
            }

            var horaReserva = fecha + " " + hora + ":00";

            $.ajax({
                url: 'Reserva',
                type: 'POST',
                data: { email, idSala: sala, horaReserva },
                success: function(response) {
                    if (response.success) {
                        $("#mensajeReserva").html(response.message)
                            .removeClass("error").addClass("exito").fadeIn();
                    } else {
                        $("#mensajeReserva").html(response.message)
                            .removeClass("exito").addClass("error").fadeIn();
                    }
                },
                error: function(jqXHR, textStatus) {
                    var errorMessage = textStatus === 'timeout' ? 
                        "La solicitud tardó demasiado tiempo." : 
                        "Error al procesar la reserva.";
                    $("#mensajeReserva").html(errorMessage)
                        .removeClass("exito").addClass("error").fadeIn();
                }
            });
        });

        // Redirigir a MisReservas.jsp cuando se haga clic en "Ver Mis Reservas"
        $("#verReservas").on('click', function() {
            window.location.href = "MisReservas.jsp";  // Redirigir a MisReservas.jsp
        });

        
    });
</script>

</body>
</html>