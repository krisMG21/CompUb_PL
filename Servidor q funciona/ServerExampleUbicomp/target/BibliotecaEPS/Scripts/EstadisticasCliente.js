document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the servlet
    fetch("/BibliotecaEPS/EstadisticasCliente")
        .then(response => response.json())
        .then(data => {
            populateTable('cubiculosTable', data.cubiculos);
            populateTable('salasTable', data.salas);

            const ocupados = data.ocupacion.ocupados;
            const disponibles = data.ocupacion.disponibles;
            displayAlertIfNeeded(ocupados, disponibles);
            createDoughnutChart('ocupacionRosco', ocupados, disponibles);
        })
        .catch(error => {
            console.error('Error al obtener las estadísticas:', error);
            alert('Ocurrió un error al cargar las estadísticas.');
        });
});

// Populate table with data
function populateTable(tableId, items) {
    const tableBody = document.getElementById(tableId).getElementsByTagName('tbody')[0];
    tableBody.innerHTML = ""; // Clear table
    items.forEach(item => {
        const row = tableBody.insertRow();
        row.insertCell(0).textContent = item.id;
        row.insertCell(1).textContent = item.nombre;
    });
}

// Display alert if occupancy is too high
function displayAlertIfNeeded(ocupados, disponibles) {
    const total = ocupados + disponibles;
    const porcentajeOcupado = (ocupados / total) * 100;
    if (porcentajeOcupado > 90) {
        document.getElementById('alertMessage').style.display = 'block';
    }
}

// Create a doughnut chart
function createDoughnutChart(canvasId, ocupados, disponibles) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) {
        console.error(`Canvas con ID "${canvasId}" no encontrado.`);
        return;
    }
    if (typeof ocupados !== 'number' || typeof disponibles !== 'number') {
        console.error('Datos no válidos para el gráfico:', { ocupados, disponibles });
        return;
    }
    const ctx = canvas.getContext('2d');
    const chartData = {
        labels: ['Ocupado', 'Disponible'],
        datasets: [{
            data: [ocupados, disponibles],
            backgroundColor: ['#FF6347', '#90EE90'],
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