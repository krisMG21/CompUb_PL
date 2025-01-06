<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList, java.util.Calendar, java.text.SimpleDateFormat, java.util.Date" %>
<%@page import="Logic.Logic" %>
<%@page import="Database.Reservas" %>

<%
    // Verifica si el usuario está autenticado, redirige si no
    String email = (String) session.getAttribute("email");
    if (email == null) {
        response.sendRedirect("MenuInicio.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <link rel="icon" href="Fotos/favicon.png" type="image/png">

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Mis Reservas</title>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <style>
            /* Estilos generales */
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f0f8ff; /* Color azul pastel claro */
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
                color: #4a90e2; /* Azul pastel */
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

        </style>
    </head>
    <body>
        <div class="container">
            <h1>Mis Reservas</h1>
            <div id="listaReservas">
                <%
                    // Obtén las reservas del usuario desde la clase Logic
                    ArrayList<Reservas> reservas = Logic.getReservasPorEmail(email);

                    // Elimina posibles duplicados (si alguna vez se producen)
                    Set<Reservas> reservasUnicas = new HashSet<>(reservas);  // Set eliminará duplicados
                    reservas = new ArrayList<>(reservasUnicas);

                    if (reservas.isEmpty()) {
                %>
                <p class="alert">No tienes reservas.</p>
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
                        <span>Sala <%= reserva.getIdSala() %>, Fecha: <%= sdf.format(reserva.getHoraReserva()) %></span>
                        <%
                            if (puedeCancelar) {
                        %>
                        <button class="cancelarReserva" data-id="<%= reserva.getIdSala() %>" data-hora="<%= sdf.format(reserva.getHoraReserva()) %>" data-email="<%= email %>">Cancelar</button>
                        <%
                            } else {
                        %>
                        <span class="noCancelar">No se puede cancelar</span>
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
                var email = $(this).data('email');  // Obtén el email desde el atributo de data-email

                $.ajax({
                    url: 'CancelarReserva',  // El Servlet sigue igual
                    type: 'POST',
                    data: {
                        emailReserva: email,  // Envia el email correctamente con el nombre de parámetro adecuado
                        idSala: idSala,
                        horaReserva: horaReserva
                    },
                    success: function (response) {
                        if (response.success) {
                            alert(response.message);
                            location.reload();  // Recarga la página para reflejar la cancelación
                        } else {
                            alert(response.message);  // Muestra el error
                        }
                    },
                    error: function (xhr, status, error) {
                        alert("Error al procesar la cancelación: " + error);
                    }
                });
            });
        </script>
    </body>
</html>
