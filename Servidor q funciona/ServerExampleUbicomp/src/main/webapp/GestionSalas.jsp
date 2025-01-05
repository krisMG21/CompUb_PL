<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
        <!-- jQuery UI -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
        
        <style>
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

            input[type="email"], input[type="text"], input[type="date"], select {
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

            hr {
                border: 1px solid #e1e1e1;
                margin: 20px 0;
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

            td {
                background-color: #f9f9f9;
            }

            td[colspan="3"] {
                text-align: center;
                color: #999;
            }
        </style>
    </head>
    <body>
        <header>
            <h1>Gestión de Reservas de Salas</h1>
        </header>

        <div class="container">
            <!-- Formulario para realizar una nueva reserva -->
            <h2>Realizar Reserva</h2>
            <form id="reservaForm" action="Reserva" method="post">
                <div class="form-group">
                    <label for="email">Correo Electrónico:</label>
                    <input type="email" id="email" name="email" value="<%= username %>" required />
                </div>
                <div class="form-group">
                    <label for="idSala">ID de la Sala:</label>
                    <select id="idSala" name="idSala" required>
                        <option value="">--Selecciona una sala--</option>
                        <option value="1">Sala 1</option>
                        <option value="2">Sala 2</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="fecha">Fecha de la Reserva:</label>
                    <input type="text" id="fecha" name="fecha" required />
                </div>
                <div class="form-group">
                    <label for="hora">Hora de la Reserva:</label>
                    <select id="hora" name="hora" required>
                        <option value="">--Selecciona una hora--</option>
                        <option value="08:00">08:00</option>
                        <option value="09:00">09:00</option>
                        <option value="10:00">10:00</option>
                        <option value="11:00">11:00</option>
                        <option value="12:00">12:00</option>
                        <option value="13:00">13:00</option>
                        <option value="14:00">14:00</option>
                        <option value="15:00">15:00</option>
                        <option value="16:00">16:00</option>
                        <option value="17:00">17:00</option>
                        <option value="18:00">18:00</option>
                        <option value="19:00">19:00</option>
                        <option value="20:00">20:00</option>
                    </select>
                </div>
                <button type="submit">Realizar Reserva</button>
            </form>

            <hr>

            <!-- Consultar Reservas Futuras -->
            <h2>Consultar Reservas Futuras</h2>
            <div id="reservas">
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

        <script>
            $(function() {
                $("#fecha").datepicker({
                    dateFormat: "yy-mm-dd",
                    minDate: 0
                });

                $("#reservaForm").on('submit', function(e) {
                    e.preventDefault();
                    var email = $("#email").val();
                    var idSala = $("#idSala").val();
                    var fecha = $("#fecha").val();
                    var hora = $("#hora").val();
                    var horaReserva = fecha + " " + hora + ":00";

                    if (!email || !idSala || !fecha || !hora) {
                        alert("Por favor, completa todos los campos.");
                        return;
                    }

                    $.ajax({
                        url: 'Reserva',
                        type: 'POST',
                        data: { email: email, idSala: idSala, horaReserva: horaReserva },
                        success: function(response) {
                            alert(response.message);
                        },
                        error: function() {
                            alert("Error al realizar la reserva.");
                        }
                    });
                });
            });
        </script>

    </body>
</html>
