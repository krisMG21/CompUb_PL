document.getElementById('FormularioLogin').addEventListener('submit', function (event) {
    event.preventDefault(); // Evitar recargar la página

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');

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
                console.log('Tipo de usuario:', data.userType);
                if (data.userType === 'admin') {
                    console.log('Redirigiendo a MenuGestor.html');
                    window.location.href = 'MenuGestor.html';
                } else if (data.userType === 'cliente') {
                    console.log('Redirigiendo a MenuCliente.html');
                    window.location.href = 'MenuCliente.html';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                errorMessage.textContent = error.message || "Ocurrió un error al procesar la solicitud.";
            });
});



