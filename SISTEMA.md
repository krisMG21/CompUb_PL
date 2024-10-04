# Sistema

## Estructura

Consta de una aplicación web, que se encargará de gestionar los espacios de
estudio, y de gestionar las reservas de los usuarios.

A la aplicación web se aporta información desde los distintos sensores de los
siguientes espacios:

1. Salas de la biblioteca: con el estado de ocupación, ruido, climatización
2. Cubículos de la biblioteca: con el estado de ocupación, ruido, climatización

## Aplicación

La reserva de las salas se pretende realizar desde una plataforma web (o móvil)
que permita a los estudiantes y profesores reservar las salas, ver la disponibilidad
en tiempo real y recibir notificaciones sobre el estado de sus reservas.

Se podrá acceder desde la web (si puede ser adaptada para móvil tmbn).
Lo suyo es tener un sistema de usuarios y permisos.

Según los features que tenemos planteados de momentos, aportaría:

1. Mapa de salas y cubículos: para ver la disponibilidad de ellos, zonas más
tranquilas según ruido, y está por ver si datos de climatización (se reservan
para analisis estadístico o también lo aportamos al usuario en tiempo real)

2. Reservas: para reservar una sala, y ver el estado de la reserva

3. Notificaciones: para recibir notificaciones sobre el estado de las reservas,
y para los admins, todos aquellos avisos que se generen por incidencias de ruido,
reservas sin cumplir, etc.

4. Acceso por QR (si no se hace por tarjeta): para acceder a las salas.
Encima de cada cerradura se encontraría un QR que, escaneado desde la aplicación,
abriría la sala de haberla reservado el usuario.

5. (ADMINS) Análisis estadístico: para ver el estado de los datos recolectados
sobre la climatización, el ruido, la afluencia de personas,... y maybe un
análisis visual de la semana, el mes, etc.

## Salas

Las salas deberán tener un método de control de acceso, que bloquee la sala cuando
está ocupada, y permita el acceso, con la apropiada identificación, cuando están
disponibles.

Tendrán sensores de ocupación, ruido, y climáticos

Todos ellos interactuarán con el sistema de reservas y las acciones de los usuarios,
si está vacía cuando empieza la reserva y no han llegado los usuarios, se notificará,
si se vacía y cierra antes de finalizar la reserva, el usuario podrá liberarla,
si hay demasiado ruido se notificará a los responsables, etc.

## Cubículos

Los cubículos estarán dotados de sensores de ocupación y ruido, así como un LED
elevado que indicará si el cubículo está o no ocupado (verde / rojo).

Se podrá visualizar desde la aplicación un mapa de las salas, con el estado de cada
cubículo, y la posición de cada uno.

Igual que como sucedía con las salas, un nivel excesivo de ruido alertará a los
responsables, mientras que po debajo de ese nivel, solo aconsejará a los usuarios
a la hora de coger sitio.

Se pueden implementar add-ons como un cronómetro y buzzer para implementar la
técnica Pomodoro.

## Sensores en la entrada

Que llevan cuenta del número de personas presentes en la biblioteca y del total
de personas que han entrado a lo largo del día.
