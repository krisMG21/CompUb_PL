<%@ page import="java.util.ArrayList, java.util.Calendar, java.text.SimpleDateFormat, java.util.Date" %>
<%@ page import="Logic.Logic" %>
<%@ page import="Database.Reservas" %>

<%
    // Verifica si el usuario está autenticado como administrador
    String email = (String) session.getAttribute("email");
    if (email == null) {
        response.sendRedirect("MenuInicio.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Eliminar Reservas</title>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <style>
            /* Estilos generales */
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f0f8ff;
                color: #333;
                margin: 0;
                padding: 0;
            }

            .container {
                width: 80%;
                margin: 40px auto;
                background-color: #ffffff;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            h1 {
                text-align: center;
                color: #4a90e2;
                margin-bottom: 20px;
            }

            #listaReservas {
                margin-top: 20px;
            }

            ul {
                list-style-type: none;
                padding: 0;
            }

            li {
                background-color: #e6f7ff;
                padding: 15px;
                margin: 10px 0;
                border-radius: 5px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            li span {
                font-size: 16px;
                color: #333;
            }

            .cancelarReserva {
                background-color: #4a90e2;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
                transition: background-color 0.3s;
            }

            .cancelarReserva:hover {
                background-color: #357ab7;
            }

            .noCancelar {
                font-size: 14px;
                color: #ff4d4d;
            }

            .alert {
                color: #d9534f;
                text-align: center;
                margin-top: 20px;
            }

            .form-group {
                margin-bottom: 15px;
            }

            input[type="email"], input[type="date"] {
                width: 100%;
                padding: 10px;
                margin: 5px 0;
                border: 1px solid #ddd;
                border-radius: 5px;
            }

            button {
                background-color: #4a90e2;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
            }

            button:hover {
                background-color: #357ab7;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Eliminar Reservas</h1>

            <!-- Filtro de Reservas -->
            <form method="get" action="EliminarReservas.jsp">
                <div class="form-group">
                    <label for="filtroEmail">Filtrar por Email:</label>
                    <input type="email" id="filtroEmail" name="filtroEmail" value="<%= request.getParameter("filtroEmail") != null ? request.getParameter("filtroEmail") : "" %>">
                </div>
                <div class="form-group">
                    <label for="filtroFecha">Filtrar por Fecha:</label>
                    <input type="date" id="filtroFecha" name="filtroFecha" value="<%= request.getParameter("filtroFecha") != null ? request.getParameter("filtroFecha") : "" %>">
                </div>
                <button type="submit">Filtrar</button>
            </form>

            <div id="listaReservas">
                <%
                    String filtroEmail = request.getParameter("filtroEmail");
                    String filtroFecha = request.getParameter("filtroFecha");

                    // Obtiene las reservas desde la clase Logic, filtrando por email y fecha si se pasa el parámetro
                    ArrayList<Reservas> reservas = Logic.getReservas(filtroEmail, filtroFecha);

                    if (reservas.isEmpty()) {
                %>
                <p class="alert">No hay reservas para mostrar.</p>
                <%
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                %>
                <ul>
                    <%
                        for (Reservas reserva : reservas) {
                            Date horaReserva = reserva.getHoraReserva();  // Hora de la reserva
                            calendar.add(Calendar.HOUR, 1);  // Establece la hora actual + 1
                            Date unaHoraDespues = calendar.getTime();  // La fecha/hora actual + 1 hora

                            boolean puedeCancelar = horaReserva.after(unaHoraDespues);  // Verifica si se puede cancelar
                    %>
                    <li>
                        <span>Email: <%= reserva.getEmailUsuario() %>, Sala: <%= reserva.getIdSala() %>, Fecha: <%= sdf.format(reserva.getHoraReserva()) %></span>
                        <%
                            if (puedeCancelar) {
                        %>
                        <button class="cancelarReserva" data-id="<%= reserva.getIdSala() %>" data-hora="<%= sdf.format(reserva.getHoraReserva()) %>" data-email="<%= reserva.getEmailUsuario() %>">Eliminar</button>
                        <%
                            } else {
                        %>
                        <span class="noCancelar">No se puede eliminar</span>
                        <%
                            }
                        %>
                    </li>
                    <%
                        }
                    %>
                </ul>
                <%
                    }
                %>
            </div>
        </div>

        <script>
            $(document).on('click', '.cancelarReserva', function () {
                var idSala = $(this).data('id');
                var horaReserva = $(this).data('hora');
                var emailReserva = $(this).data('email');

                if (confirm("¿Estás seguro de eliminar esta reserva?")) {
                    $.ajax({
                        url: 'CancelarReserva',
                        type: 'POST',
                        data: {
                            idSala: idSala,
                            horaReserva: horaReserva,
                            emailReserva: emailReserva
                        },
                        success: function (response) {
                            if (response.success) {
                                alert(response.message);
                                location.reload();
                            } else {
                                alert(response.message);
                            }
                        },
                        error: function (xhr, status, error) {
                            alert("Error al procesar la eliminación: " + error);
                        }
                    });
                }
            });
        </script>
    </body>
</html>
