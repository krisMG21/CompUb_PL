/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
 document.addEventListener("DOMContentLoaded", function () {
    // Obtener los botones del menú
    const reservaSalasButton = document.getElementById("reservaSalas");
    const reservaPuestosButton = document.getElementById("reservaPuestos");
    const estadisticasButton = document.getElementById("estadisticas");

    // Añadir eventos de clic para redirigir
    reservaSalasButton.addEventListener("click", function () {
        window.location.href = "ReservaSalas.html";//añadir esta opcion 
    });

    reservaPuestosButton.addEventListener("click", function () {
        window.location.href = "ReservaPuestos.html";//añadir esta opcion 
    });

    estadisticasButton.addEventListener("click", function () {
        window.location.href = "Estadisticas.html";//añadir esta opcion 
    });
});
