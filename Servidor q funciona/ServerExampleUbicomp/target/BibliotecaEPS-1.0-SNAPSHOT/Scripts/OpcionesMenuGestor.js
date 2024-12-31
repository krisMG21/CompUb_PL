/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", function () {
    // Obtener los botones del menú
    const gestionButton = document.getElementById("gestion");
    const estadisticasButton = document.getElementById("estadisticas");

    // Añadir eventos de clic para redirigir
    gestionButton.addEventListener("click", function () {
        window.location.href = "Gestion.html";
    });

    estadisticasButton.addEventListener("click", function () {
        window.location.href = "Estadisticas.html";
    });
});

