/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

   // Simulación de la base de datos
        const estadisticasBaseDeDatos = {
            salas: {
                "1": {
                    reservas: 5,
                    tiempo: 12 // horas totales reservadas
                },
                "2": {
                    reservas: 3,
                    tiempo: 6
                }
            },
            cubiculos: {
                "1": {
                    ocupado: "Sí",
                    ruido: "30%",
                    humedad: "40%"
                },
                "2": {
                    ocupado: "No",
                    ruido: "20%",
                    humedad: "35%"
                },
                "3": {
                    ocupado: "Sí",
                    ruido: "25%",
                    humedad: "45%"
                }
            }
        };

        // Función para actualizar el menú dependiente de la selección
        function actualizarMenu() {
            const tipo = document.getElementById('tipo').value;
            const elementoMenu = document.getElementById('elemento');
            
            // Limpiar las opciones del menú dependiente
            elementoMenu.innerHTML = "";

            if (tipo === "salas") {
                // Añadir opciones para Salas
                for (let i = 1; i <= 2; i++) {
                    let option = document.createElement("option");
                    option.value = i;
                    option.textContent = `Sala ${i}`;
                    elementoMenu.appendChild(option);
                }
            } else if (tipo === "cubiculos") {
                // Añadir opciones para Cubículos
                for (let i = 1; i <= 3; i++) {
                    let option = document.createElement("option");
                    option.value = i;
                    option.textContent = `Cubículo ${i}`;
                    elementoMenu.appendChild(option);
                }
            }
        }

        // Función para mostrar las estadísticas según la selección
        function mostrarEstadisticas() {
            const tipo = document.getElementById('tipo').value;
            const elemento = document.getElementById('elemento').value;
            const resultado = document.getElementById('resultado');
            const estadisticasElemento = estadisticasBaseDeDatos[tipo][elemento];

            let estadisticasText;

            if (tipo === "salas") {
                // Mostrar estadísticas para salas
                estadisticasText = `
                    <strong>Reservas:</strong> ${estadisticasElemento.reservas} veces<br>
                    <strong>Tiempo Total Reservado:</strong> ${estadisticasElemento.tiempo} horas
                `;
            } else if (tipo === "cubiculos") {
                // Mostrar estadísticas para cubículos
                estadisticasText = `
                    <strong>Ocupado:</strong> ${estadisticasElemento.ocupado}<br>
                    <strong>Porcentaje de Ruido:</strong> ${estadisticasElemento.ruido}<br>
                    <strong>Porcentaje de Humedad:</strong> ${estadisticasElemento.humedad}
                `;
            }

            document.getElementById('estadisticas').innerHTML = estadisticasText;
        }

        // Inicializar el menú desplegable al cargar la página
        window.onload = actualizarMenu;
    
