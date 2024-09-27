## **Sistema Inteligente de Reservas de Espacios con Arduino y Sensores**

### **Objetivo del Proyecto**
Desarrollar un sistema que permita a los estudiantes y profesores reservar salas de estudio, laboratorios o aulas a través de una aplicación o plataforma web, gestionando de manera eficiente los recursos y asegurando el acceso automatizado a los mismos mediante el uso de sensores de presencia y cerraduras electrónicas.

### **Modelo de 4 Capas**

#### **1. Capa de Dispositivos (Hardware)**

Esta capa se encarga de recolectar información en tiempo real sobre la ocupación de las salas y controlar los accesos a las mismas.

- **Sensores de Movimiento PIR**: Detectan la presencia de personas dentro de las salas para determinar si la sala está ocupada o no. Estos sensores proporcionan información en tiempo real.
  
- **Cerraduras Electrónicas (Controladas por Arduino)**: Estas cerraduras aseguran que solo aquellos que hayan reservado la sala puedan acceder. Se puede usar un relé conectado al Arduino para controlar la cerradura.

- **Pantallas LCD o LEDs Externos**: Ubicados fuera de las salas para mostrar el estado de la misma (disponible/ocupada/reservada). Podrían usarse LEDs de diferentes colores para indicar el estado (verde: disponible, rojo: ocupada, amarillo: reservada).

- **Arduino**: Actúa como el centro de control, procesando los datos de los sensores y controlando las cerraduras. Dependiendo de la conectividad requerida, puede ser un **Arduino Uno** con **ESP8266** para conectividad Wi-Fi o directamente un **ESP32** que ya cuenta con conectividad Wi-Fi.

- **Tarjeta RFID (Opcional)**: Para permitir la entrada de los usuarios mediante tarjetas de identificación, como las que utilizan los estudiantes en el campus.

**Componentes necesarios**:
  - Arduino Uno o ESP32
  - Sensor de movimiento PIR
  - Cerradura electrónica
  - Módulo de relé
  - Módulo Wi-Fi (ESP8266) o integrado (ESP32)
  - Pantalla LCD o LEDs indicadores
  - Lector RFID (opcional)

#### **2. Capa de Red**

El propósito de esta capa es conectar los dispositivos de hardware con el servidor o la nube para transmitir y recibir datos.

- **Conectividad Wi-Fi o Ethernet**: El Arduino se conecta a la red a través de Wi-Fi (ESP8266/ESP32) o mediante Ethernet (si se usa un módulo de red), permitiendo que los datos sean enviados a un servidor o servicio en la nube para procesarlos.
  
- **Protocolo MQTT o HTTP**: Se puede usar el protocolo **MQTT** para enviar los datos sobre el estado de la sala (ocupada/disponible) y controlar la cerradura electrónicamente, o bien **HTTP** para enviar y recibir datos entre el Arduino y el servidor web.

- **Servidor**: Los datos se envían a un servidor central (puede ser un servidor local o en la nube) para gestionar las reservas y los accesos.

#### **3. Capa de Servicios de Middleware**

Esta capa se encarga de procesar los datos enviados por los dispositivos, gestionar la lógica del sistema y almacenar la información sobre el uso y reservas de las salas.

- **Servidor de Base de Datos**: Se utilizará una base de datos (por ejemplo, **MySQL** o **Firebase**) para almacenar las reservas y el estado de cada sala. Cada vez que se realiza una reserva, el sistema guarda los detalles (hora, usuario, duración) y actualiza el estado de la sala en tiempo real.

- **Algoritmo de Gestión de Reservas**: El servidor cuenta con un algoritmo que revisa las solicitudes de reserva y las cruza con la disponibilidad de las salas, evitando reservas duplicadas o errores de ocupación. También controla cuándo las cerraduras deben desbloquearse o bloquearse.

- **Sistema de Autenticación**: Los usuarios deberán autenticarse mediante credenciales (usuario y contraseña) o con su tarjeta de identificación (si se usa el módulo RFID), asegurando que solo las personas autorizadas accedan a las salas reservadas.

- **API RESTful**: Un servicio RESTful permitirá que la aplicación web o móvil interactúe con el sistema, solicitando y actualizando datos en la base de datos sobre las reservas y el estado de las salas.

#### **4. Capa de Aplicación (Software)**

La capa de aplicación es donde los usuarios interactúan con el sistema. Ofrece una interfaz para que los estudiantes y profesores reserven las salas, vean la disponibilidad en tiempo real y reciban notificaciones sobre el estado de sus reservas.

- **Aplicación Web o Móvil**: Los usuarios pueden acceder a una plataforma web o aplicación móvil para buscar salas disponibles, reservarlas y visualizar el estado en tiempo real.
  
  - **Funcionalidades**:
    - Ver todas las salas disponibles (en tiempo real).
    - Realizar y cancelar reservas.
    - Consultar historial de reservas.
    - Ver detalles como capacidad de la sala, ubicación y equipo disponible.
    - Notificaciones sobre la proximidad de una reserva o alertas si el acceso es denegado.
  
  - **Lenguajes de Programación**: La interfaz web puede ser desarrollada en **HTML, CSS y JavaScript**, utilizando frameworks como **React** o **Vue.js** para crear una experiencia fluida. Para la aplicación móvil, se pueden usar tecnologías como **Flutter** o **React Native**.

- **Panel de Control para Administradores**: El personal de administración puede acceder a un panel para gestionar las salas, verificar patrones de uso y recibir alertas de problemas en las cerraduras o sensores.

- **Notificaciones Automáticas**: El sistema puede enviar notificaciones por correo electrónico o mensajes en la aplicación para avisar a los usuarios cuando una sala esté disponible o para recordarles que su reserva está a punto de expirar.

### **Flujo de Funcionamiento del Sistema**

1. **Reserva**: Un usuario ingresa a la aplicación web o móvil, selecciona una sala y elige un horario. El sistema verifica la disponibilidad y confirma la reserva.
  
2. **Actualización del Estado**: Los sensores PIR monitorean la presencia de personas en las salas. Si la sala está ocupada sin una reserva, se puede generar una alerta.

3. **Acceso**: Cuando el usuario llega a la sala, puede escanear su tarjeta RFID o ingresar un código de acceso para desbloquear la cerradura electrónica (controlada por el Arduino).

4. **Monitoreo en Tiempo Real**: El estado de la sala se actualiza automáticamente en el sistema (disponible, ocupada, reservada) y se muestra en la aplicación. Los LED o pantallas externas indican visualmente el estado de la sala.

5. **Finalización de Reserva**: Cuando la reserva termina o el usuario abandona la sala, la cerradura se bloquea y el sistema actualiza la disponibilidad.

---

### **Beneficios del Sistema**
- **Optimización de recursos**: Las salas no estarán subutilizadas, ya que el sistema garantiza que estén ocupadas solo cuando hayan sido reservadas.
- **Automatización del acceso**: Mejora la seguridad y evita el uso indebido de las instalaciones.
- **Facilidad de uso**: Los usuarios pueden hacer reservas desde cualquier lugar y recibir notificaciones cuando la sala esté lista.
- **Datos útiles**: Los administradores pueden analizar patrones de uso para optimizar la gestión de los espacios.

---

### **Posible Expansión**
Este sistema puede expandirse fácilmente a otros tipos de espacios en el campus, como laboratorios, áreas de trabajo colaborativo e incluso auditorios. También se podría integrar con sistemas de climatización para ahorrar energía ajustando el aire acondicionado o la calefacción en función de la ocupación de las salas.

Con estos componentes, tienes una solución completa para la gestión inteligente de espacios en un campus universitario.
