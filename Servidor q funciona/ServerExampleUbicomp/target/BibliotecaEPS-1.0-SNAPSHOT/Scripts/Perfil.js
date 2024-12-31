// Fetch del servlet para obtener los datos del usuario
fetch('PerfilUsuarioServlet')
    .then(response => {
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        return response.json();
    })
    .then(data => {
        if (data.error) {
            console.error('Error del servidor:', data.error);
        } else {
            document.getElementById('nombre').textContent = data.nombre || 'No disponible';
            document.getElementById('apellidos').textContent = data.apellidos || 'No disponible';
            document.getElementById('correo').textContent = data.correo || 'No disponible';
        }
    })
    .catch(error => console.error('Error al cargar el perfil:', error));
