/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// Variable para almacenar la sala y franja seleccionada
let salaSeleccionada = "";
let franjaSeleccionada = "";

// Función que muestra las franjas horarias disponibles al seleccionar una sala
function mostrarFranjas() {
    // Obtenemos el valor de la sala seleccionada
    const sala = document.getElementById("seleccion-sala").value;

    // Ocultamos todos los contenedores de salas
    const salas = document.querySelectorAll(".sala-container");
    salas.forEach(salaContainer => salaContainer.style.display = "none");

    // Mostramos la sala seleccionada
    if (sala) {
        document.getElementById(sala).style.display = "block";
    }
}

// Función que maneja la selección de una franja horaria
function reservar(sala, franja, event) {
    // Si la franja está disponible, marcamos la selección
    if (event.target.classList.contains("hora-disponible")) {
        // Limpiamos la selección anterior, si la había
        const horas = document.querySelectorAll(".hora-disponible");
        horas.forEach(hora => hora.classList.remove("seleccionada"));

        // Marcamos la franja horaria seleccionada
        event.target.classList.add("seleccionada");

        // Guardamos la información de la sala y la franja seleccionada
        salaSeleccionada = sala;
        franjaSeleccionada = franja;

        // Habilitamos el botón de reservar
        document.getElementById("reservarBtn").disabled = false;
    }
}


// Función que realiza la reserva
function realizarReserva() {
    if (salaSeleccionada && franjaSeleccionada) {
        // Aquí puedes agregar la lógica para hacer la reserva, por ejemplo, enviar una solicitud al servidor
        alert(`Reserva realizada con éxito:\nSala: ${salaSeleccionada}\nFranja horaria: ${franjaSeleccionada}`);
        
        // Para propósitos de demostración, limpiamos las selecciones después de la reserva
        document.getElementById("seleccion-sala").value = "";
        salaSeleccionada = "";
        franjaSeleccionada = "";
        const horas = document.querySelectorAll(".hora");
        horas.forEach(hora => hora.classList.remove("seleccionada"));
        document.getElementById("reservarBtn").disabled = true;
        
        // Ocultamos las franjas horarias después de realizar la reserva
        const salas = document.querySelectorAll(".sala-container");
        salas.forEach(salaContainer => salaContainer.style.display = "none");
    }
}
