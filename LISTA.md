# Lista de la compra

## Sistema

### Reservas

La reserva de las salas se pretende realizar desde una plataforma web (o móvil)
que permita a los estudiantes y profesores reservar las salas, ver la disponibilidad
en tiempo real y recibir notificaciones sobre el estado de sus reservas.

### Salas

Las salas deberán tener un método de control de acceso, que bloquee la sala cuando
está ocupada, y permita el acceso, con la apropiada identificación, cuando están
disponibles.

Tendrán sensores de ocupación, ruido, y climáticos

Todos ellos interactuarán con el sistema de reservas y las acciones de los usuarios,
si está vacía cuando empieza la reserva y no han llegado los usuarios, se notificará,
si se vacía y cierra antes de finalizar la reserva, el usuario podrá liberarla,
si hay demasiado ruido se notificará a los responsables, etc.

### Cubículos

Los cubículos estarán dotados de sensores de ocupación y ruido, así como un LED
elevado que indicará si el cubículo está o no ocupado (verde / rojo).

Se podrá visualizar desde la aplicación un mapa de las salas, con el estado de cada
cubículo, y la posición de cada uno.

Igual que como sucedía con las salas, un nivel excesivo de ruido alertará a los
responsables, mientras que po debajo de ese nivel, solo aconsejará a los usuarios
a la hora de coger sitio.

---

## Componentes

1. Actuadores
   * 1.1. Buzzer
   * 1.2. Servo motor
   * 1.3. Led
2. Sensores:
   * 2.1. Temperatura
   * 2.2. Movimiento
   * 2.3. Sonido
   * 2.4. Lector tarjetas
   * 2.5. Luz
   * 2.6. Botón
3. Otros: ESP32

---

## Apuntes

* Ocupación de cubículos / puestos individuales tmbn
   --> Sensores de movimiento + LED
* Sensores de climatización de la biblio
   --> Sensores de temperatura + humedad + luz
* Cerraduras inteligentes para las salas
   --> Escaner de tarjeta / qr para activarla desde el movil
* Técnica Pomodoro
   --> Monitorear sprints de trabajo, 30 min por ejemplo
* Sensores de ruido / movimiento
   --> Monitorear zonas +/- concurridas / ruidosas
* Análisis estadístico de los datos recogidos
   --> Backend y BBDD
