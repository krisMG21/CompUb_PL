
//No se usa
document.getElementById('FormularioLogin').addEventListener('submit', function (event) {
    event.preventDefault(); // Evitar recargar la página

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');

    // Limpiar mensajes de error anteriores
    errorMessage.textContent = '';

    // Realizar la solicitud POST al servlet
    fetch('/BibliotecaEPS/Login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    })
    .then(response => response.json())  // Aseguramos que la respuesta sea en JSON
    .then(data => {
        if (data.status === 'success') {
            // Redirigir según el tipo de usuario
            if (data.userType === 'admin') {
                window.location.href = 'MenuGestor.jsp';
            } else if (data.userType === 'cliente') {
                window.location.href = 'MenuCliente.html';
            }
        } else {
            // Si hay error, mostramos el mensaje
            throw new Error(data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        errorMessage.textContent = error.message || "Ocurrió un error al procesar la solicitud.";
    });
});
