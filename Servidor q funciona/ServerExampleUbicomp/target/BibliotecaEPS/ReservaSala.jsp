<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserva de Salas</title>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- jQuery UI -->
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    
    <style>
        /* Fondo azul pastel y estilo general */
        body {
            font-family: 'Arial', sans-serif;
            background-color: #e1f5fe; /* Azul pastel */
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            flex-wrap: wrap;
        }

        /* Contenedor principal */
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
            text-align: center;
        }

        h1 {
            color: #0277bd; /* Azul oscuro para el título */
            margin-bottom: 20px;
        }

        /* Estilo del formulario */
        form {
            display: flex;
            flex-direction: column;
            gap: 15px;
            margin-bottom: 20px;
        }

        label {
            font-size: 16px;
            font-weight: 600;
        }

        select, input[type="text"] {
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 14px;
        }

        input[type="submit"] {
            padding: 12px 20px;
            background-color: #0277bd;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #01579b;
        }

        /* Estilo para los mensajes */
        #mensajeReserva {
            margin-top: 20px;
            padding: 10px;
            border-radius: 5px;
            font-size: 16px;
            display: none;
        }

        .exito {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        /* Estilo para el calendario */
        .ui-datepicker {
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .ui-datepicker-header {
            background-color: #0277bd;
            color: white;
            font-weight: bold;
        }

        .ui-datepicker-prev, .ui-datepicker-next {
            color: #0277bd;
        }

        .ui-datepicker td a {
            color: #0277bd;
        }

        /* Imagen lateral */
        .imagen-lateral {
            max-width: 200px; /* Ajusta el tamaño */
            margin-left: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        /* Flexbox para que la imagen y el formulario estén en línea */
        .contenedor {
            display: flex;
            align-items: center;
            justify-content: center;
            flex-wrap: wrap;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <!-- Formulario -->
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
                    <option value="">--Selecciona una hora--</option>
                    <% 
                    for (int i = 8; i <= 20; i++) {
                        String hora = String.format("%02d:00", i);
                    %>
                        <option value="<%= hora %>"><%= hora %></option>
                    <% } %>
                </select>

                <input type="submit" value="Reservar Sala">
            </form>
            <div id="mensajeReserva"></div>
        </div>

        <!-- Imagen lateral -->
        <img src="Fotos/MapaBibliotecaN.png" alt="Imagen Lateral" class="imagen-lateral">
    </div>

    <script>
        $(function() {
            var diasNoLaborables = ["2025-01-01", "2025-12-25"]; // Ejemplo de días festivos

            $("#fecha").datepicker({
                dateFormat: "yy-mm-dd",
                minDate: 0,
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
        });
    </script>
</body>
</html>
