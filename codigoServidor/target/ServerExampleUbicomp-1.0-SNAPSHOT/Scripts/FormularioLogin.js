document.getElementById('FormularioLogin').addEventListener('submit', function (event) {
    event.preventDefault(); // Evitar recargar la página

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');

    // Realizar la solicitud POST al servlet
    fetch('LoginServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === "success") {
            // Redirigir según el tipo de usuario
            if (data.userType === 0) {
                window.location.href = 'MenuCliente.html';
            } else if (data.userType === 1) {
                window.location.href = 'MenuGestor.html';
            }
        } else {
            // Mostrar mensaje de error
            errorMessage.textContent = data.message;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        errorMessage.textContent = "Ocurrió un error al procesar la solicitud.";
    });
});
