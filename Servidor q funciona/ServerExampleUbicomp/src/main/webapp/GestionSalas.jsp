<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Timestamp" %>
<%
    String username = (String) session.getAttribute("email");
    if (username == null) {
        response.sendRedirect("MenuInicio.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Reservas de Salas</title>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
        <style>
            /* Estilos generales */
            body {
                font-family: Arial, sans-serif;
                background-color: #f2f7fc;
                color: #333;
                margin: 0;
                padding: 0;
            }
            header {
                background-color: #a1c6ea;
                color: white;
                padding: 20px 0;
                text-align: center;
                font-size: 24px;
            }
            .container {
                max-width: 900px;
                margin: 20px auto;
                padding: 20px;
                background-color: white;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
            h2 {
                color: #3b6a92;
            }
            .form-group {
                margin-bottom: 15px;
            }
            label {
                display: block;
                margin-bottom: 8px;
                font-size: 16px;
                color: #3b6a92;
            }
            input[type="email"], select, input[type="text"], input[type="date"] {
                width: 100%;
                padding: 10px;
                margin: 5px 0 10px 0;
                border: 1px solid #ddd;
                border-radius: 5px;
                font-size: 14px;
                box-sizing: border-box;
                background-color: #f9f9f9;
            }
            button {
                background-color: #a1c6ea;
                color: white;
                border: none;
                padding: 10px 20px;
                font-size: 16px;
                cursor: pointer;
                border-radius: 5px;
                transition: background-color 0.3s ease;
            }
            button:hover {
                background-color: #5a92bb;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                padding: 10px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            th {
                background-color: #a1c6ea;
                color: white;
            }
            td[colspan="3"] {
                text-align: center;
                color: #999;
            }
            #mensajeReserva {
                margin-top: 15px;
                padding: 10px;
                border-radius: 5px;
                font-size: 14px;
            }
            .exito {
                background-color: #d4edda;
                color: #155724;
            }
            .error {
                background-color: #f8d7da;
                color: #721c24;
            }
        </style>
    </head>
    <body>
        <header>
            <h1>Gestión de Reservas de Salas</h1>
        </header>

        <div class="container">
            <!-- Botón para eliminar reservas -->
            <div style="text-align: center; margin-bottom: 20px;">
                <a href="EliminarReservas.jsp">
                    <button>Eliminar Reservas</button>
                </a>
            </div>

            <!-- Formulario para realizar una nueva reserva -->
            <h2>Realizar Reserva</h2>
            <form id="reservaForm">
                <div class="form-group">
                    <label for="email">Correo Electrónico del Solicitante:</label>
                    <input type="email" id="email" name="email" required />
                </div>
                <div class="form-group">
                    <label for="idSala">ID de la Sala:</label>
                    <select id="idSala" name="idSala" required>
                        <option value="">--Selecciona una sala--</option>
                        <!-- Opciones dinámicas cargadas desde el backend -->
                    </select>
                </div>
                <div class="form-group">
                    <label for="fecha">Selecciona una fecha:</label>
                    <input type="text" id="fecha" name="fecha" required />
                </div>
                <div class="form-group">
                    <label for="horaReserva">Hora de la Reserva:</label>
                    <select id="horaReserva" name="horaReserva" required>
                        <option value="">--Selecciona una hora--</option>
                        <% for (int i = 8; i <= 20; i++) { 
                            String hora = String.format("%02d:00", i);
                        %>
                            <option value="<%= hora %>"><%= hora %></option>
                        <% } %>
                    </select>
                </div>
                <button type="submit">Realizar Reserva</button>
            </form>
            <div id="mensajeReserva"></div>

            <hr>

            <!-- Consultar Reservas Futuras -->
            <h2>Consultar Reservas Futuras</h2>
            <div>
                <button onclick="fetchReservas()">Cargar Reservas Futuras</button>
            </div>

            <hr>

            <!-- Mostrar las Reservas Futuras -->
            <h2>Lista de Reservas Futuras</h2>
            <table id="reservasTable">
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>ID Sala</th>
                        <th>Hora de Reserva</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Las reservas futuras se agregarán aquí -->
                </tbody>
            </table>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
        <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
        <script>
            $(function () {
                // Cargar salas disponibles
                cargarSalas();

                // Configuración del calendario
                const diasNoLaborables = ["2025-01-01", "2025-12-25"];
                $("#fecha").datepicker({
                    dateFormat: "yy-mm-dd",
                    minDate: 0,
                    beforeShowDay: function (date) {
                        const day = date.getDay();
                        const formattedDate = $.datepicker.formatDate('yy-mm-dd', date);
                        return [day !== 0 && day !== 6 && !diasNoLaborables.includes(formattedDate)];
                    }
                });

                // Manejar el envío del formulario
                $("#reservaForm").on("submit", function (e) {
                    e.preventDefault();
                    const email = $("#email").val();
                    const idSala = $("#idSala").val();
                    const fecha = $("#fecha").val();
                    const hora = $("#horaReserva").val();

                    if (!email || !idSala || !fecha || !hora) {
                        mostrarMensaje("Por favor, completa todos los campos.", false);
                        return;
                    }

                    $.post('Reserva', { email, idSala, horaReserva: `${fecha} ${hora}:00` })
                        .done(response => mostrarMensaje(response.message, response.success))
                        .fail(() => mostrarMensaje("Error al realizar la reserva.", false));
                });
            });

            function cargarSalas() {
                fetch('SalaDisponible')
                    .then(response => response.json())
                    .then(data => {
                        const select = $('#idSala');
                        select.empty().append('<option value="">--Selecciona una sala--</option>');
                        data.forEach(sala => {
                            select.append(`<option value="${sala.id}">Sala ${sala.id}</option>`);
                        });
                    })
                    .catch(error => console.error('Error al cargar las salas:', error));
            }

            function mostrarMensaje(mensaje, exito) {
                const div = $("#mensajeReserva");
                div.text(mensaje).removeClass("exito error").addClass(exito ? "exito" : "error").fadeIn();
            }

            function fetchReservas() {
                fetch('Reserva')
                    .then(response => response.json())
                    .then(data => {
                        const tableBody = $('#reservasTable tbody').empty();
                        if (data.length > 0) {
                            data.forEach(r => tableBody.append(
                                `<tr><td>${r.email}</td><td>${r.idSala}</td><td>${r.horaReserva}</td></tr>`
                            ));
                        } else {
                            tableBody.append('<tr><td colspan="3">No hay reservas futuras.</td></tr>');
                        }
                    })
                    .catch(error => console.error('Error al cargar reservas:', error));
            }
        </script>
    </body>
</html>
