document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the servlet
    fetch("/BibliotecaEPS/EstadisticasGestor")
        .then(response => response.json())
        .then(data => {
            populateCubiculosTable(data.cubiculos);
            populateSalasTable(data.salas);
            createDoughnutChart('ocupacionRosco', data.ocupacion.ocupados, data.ocupacion.disponibles);
            handleFullOccupancyAlert(data.ocupacion.ocupados, data.ocupacion.disponibles);
        })
        .catch(error => {
            console.error('Error al obtener las estadísticas:', error);
            alert('Ocurrió un error al cargar las estadísticas.');
        });
});

// Populate cubicles table
function populateCubiculosTable(cubiculos) {
    const cubiculosTable = document.getElementById('cubiculosTable').getElementsByTagName('tbody')[0];
    cubiculosTable.innerHTML = ""; // Clear the table
    cubiculos.forEach(cubiculo => {
        let row = cubiculosTable.insertRow();
        row.insertCell(0).textContent = cubiculo.id;
        row.insertCell(1).textContent = cubiculo.nombre;
        row.insertCell(2).textContent = cubiculo.ocupante;
    });
}

// Populate rooms table
function populateSalasTable(salas) {
    const salasTable = document.getElementById('salasTable').getElementsByTagName('tbody')[0];
    salasTable.innerHTML = ""; // Clear the table
    const now = new Date(); // Get current time
    salas.forEach(sala => {
        const reservaHora = new Date(sala.horaReserva);
        if (reservaHora >= now) { // Only include current or future reservations
            let row = salasTable.insertRow();
            row.insertCell(0).textContent = sala.id;
            row.insertCell(1).textContent = sala.nombre;
            row.insertCell(2).textContent = sala.ocupante;
            row.insertCell(3).textContent = reservaHora.toLocaleTimeString();
        }
    });
}

// Display alert if occupancy is at full capacity
function handleFullOccupancyAlert(ocupados, disponibles) {
    const total = ocupados + disponibles;
    const porcentajeOcupado = (ocupados / total) * 100;
    if (porcentajeOcupado === 100) {
        alert('¡A full! Si encuentras algo mal aprovechado, ¡apúrate y reorganiza el lugar! Que no todo el mundo deja sus cosas y se va... ¿o sí?');
    }
}

// Create doughnut chart
function createDoughnutChart(canvasId, ocupados, disponibles) {
    const ctx = document.getElementById(canvasId).getContext('2d');
    const chartData = {
        labels: ['Ocupado', 'Disponible'],
        datasets: [{
            data: [ocupados, disponibles],
            backgroundColor: ['#FF6347', '#90EE90'],  // Red for occupied, green for available
            hoverBackgroundColor: ['#FF4500', '#32CD32'],
            borderWidth: 1
        }]
    };

    new Chart(ctx, {
        type: 'doughnut',
        data: chartData,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top'
                },
                tooltip: {
                    callbacks: {
                        label: function (tooltipItem) {
                            const dataset = tooltipItem.dataset;
                            const total = dataset.data.reduce((sum, value) => sum + value, 0);
                            const currentValue = dataset.data[tooltipItem.dataIndex];
                            const percentage = Math.round((currentValue / total) * 100);
                            return currentValue + ' (' + percentage + '%)';
                        }
                    }
                }
            }
        }
    });
}
