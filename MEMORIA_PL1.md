# Indice PL1

1. [Introducción](#introducción)
2. [Análisis del problema](#análisis-del-problema)
3. [Proyectos de referencia](#proyectos-de-referencia)
4. [Objetivos y alcance del proyecto](#objetivos-y-alcance-del-proyecto)
5. [Propuesta de desarrollo](#propuesta-de-desarrollo)
   * 5.1. [Arquitectura del sistema](#arquitectura-del-sistema)
      * 5.1.1. [Salas de la biblioteca](#salas-de-la-biblioteca)
         * [Software sala](#software-sala)
         * [Hardware sala](#hardware-sala)
      * 5.1.2. [Cubículos de la biblioteca](#cubículos-de-la-biblioteca)
         * [Software cubículo](#software-cubículo)
         * [Hardware cubículo](#hardware-cubículo)
   * 5.2. [Tecnología a utilizar](#tecnología-a-utilizar)
   * 5.3. [Mockup de aplicaciones](#mockup-de-aplicaciones)
   * 5.4. [Plan de desarrollo](#plan-de-desarrollo)
6. [Conclusiones](#conclusiones)
7. [Bibliografía](#bibliografía)

## Introducción

   Como estudiantes sabemos que muchas veces es complicado encontrar un espacio
   tranquilo para hacer trabajos en grupo o simplemente estudiar.
   Con nuestro proyecto proponemos una solución a estos problemas, gracias a una
   aplicación hecha  facilitar la reserva de salas y cubículos en bibliotecas.

   Esta aplicación no solo nos ayudará a gestionar correctamente los cubículos o
   salas libres, pudiendo reservarlos, sino que también se integrarán sensores
   para medir la ocupación, ruido y climatización, optimizando el uso de los
   espacios.
   Esta plataforma esta destinada a la gestión de espacios y no a la gestión de
   los libros.

   Para mejorar la productividad, cada cubículo contará con un botón Pomodoro,
   permitiendo a los estudiantes gestionar su tiempo de estudio de forma eficiente.

## Análisis del problema

   El acceso a espacios de estudio como puede ser una biblioteca en época de
   exámenes suele ser caótico, con dificultad para saber qué salas o cubículos
   están disponibles.
   Muchos estudiantes perdemos el tiempo buscando un lugar adecuado, y las
   reservas no siempre se utilizan de manera eficiente, ya que a veces hay
   cubículos sin personas, pero ocupados por sus cosas. Además, la falta de
   control sobre el ruido y la climatización puede afectar la concentración.

## Proyectos de referencia

Como proyectos de referencia tenemos a:

* Condeco [(link)](https://www.condecosoftware.com/es/):
   Condeco es una empresa dedicada a software integrado de reserva de espacio
   de trabajo aunque está destinada principalmente a las oficinas.
   La diferencia que marcamos con ellos seria el público al que está dirigido y
   el uso de sensores para el control del ambiente.

* Roomzilla [(link)](https://www.softwareadvice.ie/software/262110/roomzilla):
   Software dedicado a  la reserva de salas en oficinas, coworking y universidades.
   La diferencia con ellos es la implementación de los sensores.

* Zityhub [(link)](https://zityhub.com/space-management):
   Empresa que implementa software de reserva de espacios, gestión y control de
   accesos a empresas a nivel de aplicación. También aporta estadísticas y análisis
   sobre los datos obtenidos.

* Universidad de Álcala  [(link)]( https://biblioteca.uah.es/conoce-la-biblioteca/servicios/reserva-de-puestos-de-lectura/):
  En la misma Universidad de Alcalá cuentan con un sistema para poder reservar
  asientos y salas en un intervalo de tiempo, aunque la reserva de asiento no esta
  implementado en la Escuela Politecnica de la Universidad de Alcalá.
  Además este sistema no cuenta con sensores para controlar la climatización de
  salas y/o cubículos, mostrar la disponibilidad de forma física mediante led y
  en la plataforma.

## Objetivos y alcance del proyecto

* Desarrollar una plataforma que se pueda utilizar tanto desde la web como desde
dispositivos móviles.
* Crear un sistema de reserva de espacios en la biblioteca para los usuarios de esta.
* Incorporar un mapa  que muestre la disponibilidad de salas y cubículos.
* Ofrecer estadísticas detalladas para optimizar la gestión de los espacios.
* Implementar el acceso a las salas mediante códigos QR o tarjetas para ayudar a
los usuarios a la hora de hacer uso de la biblioteca.
* Destinado en un principio a los usuarios de la biblioteca de la politécnica de
la UAH, ya que es nuestra biblioteca de referencia.

## Propuesta de desarrollo

### Arquitectura del sistema

> [!NOTE]
> ¿Tendremos que detallar todos los componentes intermedios?
> Sensores, Actuadores <-> Controlador -> Middleware <-> Aplicación
>(BBDD, Frontend, Backend)

Consta de una aplicación web, que se encargará de gestionar los espacios de
estudio, y de gestionar las reservas de los usuarios.

A la aplicación web se aporta información desde los distintos sensores de los
siguientes espacios:

1. Salas de la biblioteca: con el estado de ocupación, ruido, climatización
2. Cubículos de la biblioteca: con el estado de ocupación, ruido, climatización

#### Salas de la biblioteca

Las salas de la biblioteca se encuentran conectadas a un sistema de reservas
que permite a los usuarios reservar las salas, y recibir notificaciones sobre
el estado de las reservas.

##### Software sala

Para poder gestionar las reservas, se utilizará un sistema de usuarios y permisos
que permita a los usuarios registrarse y gestionar sus reservas.

Se podrá acceder desde la web (si puede ser adaptada para móvil tmbn).

La apliación web recibe información de los sensores (ocupación, climatización y
ruido). Podremos monitorizar el uso de la sala con el de ocupación y comprobarlo
con las resresas de los usuarios.

##### Hardware sala

Los sensores de ocupación, climatización y ruido se encuentran conectados al
controlador principal, desde el que se envía la información a la aplicación web,
con fines de monitorización y estadísticos.

La sala cuenta también con una cerradura inteligente, que se encarga controlar
el acceso a la sala. Se activa desde la aplicación web, por parte del usuario
que ha reservado la sala, cuando quiere acceder a ella.

#### Cubículos de la biblioteca

Los cubículos de la biblioteca están equipados, aparte de con sensores de
ocupación, climatización y ruido, con un sistema Pomodoro para el estudio.

##### Software cubículo

La aplicación web recibe la información de todos los sensores, saca estadísticas
detalladas para los gestores y la resumen para mostrarla en el mapa del usuario.

##### Hardware cubículo

El cubículo cuenta con un sensor de ocupación (conectado a un led (verde/rojo) ),
climatización y ruido, y un sistema Pomodoro para el estudio.

Este sistema consta de un display de tiempo, un botón de inicio y un buzzer
para indicar la finalización del sprint del pomodoro.

### Tecnología a utilizar

#### Hardware

En cuanto a los componentes de hardware del sistema, usamos los siguientes a lo
largo de ambos espacios:

##### Actuadores

* Buzzer activo: Indica la finalización del sprint del pomodoro
* Servo motor: Para la cerradura inteligente
* Led: Para indicar la ocupación de los espacios

##### Sensores

* Temperatura y humedad `DHT11`
* Movimiento: `HC-SRSOI PIR Sensor`
* Sonido: `Sound sensor module`
* Lector tarjetas RFID `RC522`  básico o RFID comercial
* Luz (fotoresistores)
* Botón

##### Controladores

* Arduino: `ESP32`, conectividad Wi-Fi

#### Software

La aplicación web aporta, de cara a los usuarios, información sobre el estado de
los espacios de estudio, y permite gestionar las reservas de los usuarios.
Además, tendrá a su disposición un mapa que muestre la disponibilidad de las
salas y cubículos, así como datos resumidos de ruido y climatización.

De cara a los gestores, aporta la información recogida de los sensores y las
estadísticas derivadas de ellos, como puede ser el uso de las salas y los cubículos,
la climatización y ruido a lo largo del día,...

### Mockup de aplicaciones

### Plan de desarrollo

### Presupuesto

## Conclusiones

## Bibliografía
