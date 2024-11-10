 #include <cstdio>
#include <DHT.h>
#include <WiFi.h>
#include <PubSubClient.h>

#include "config.h" //Credenciales de WiFi y MQTT
#include "sala.h" //Objeto de la sala
#include "cubiculo.h" //Objeto del cubículo

#include "pomodoro.h"
#include "rfid.h"
#include "sensor.h"
#include "servo.h"

// NOTE: MODO DE EJECUCIÓN
// ==================================================
typedef enum {SALA, CUBICULO} tipo;
// #define MODE SALA
#define MODE CUBICULO


// NOTE: PINES
// ==================================================
#define RED_LED 5             // LED de ocupación del cubículo: ROJO
#define GREEN_LED 21           // LED de ocupación del cubículo: VERDE

// LEDs del pomodoro
 #define P_LED1 2
 #define P_LED2 14
 #define P_LED3 27
 #define P_LED4 16
 #define P_LED5 0
 #define P_LED_AMARILLO 15

#define BUZZER 32             // Buzzer
#define SERVO 13
#define SSONIDO 35            // Sensor de sonido
#define SLUZ 12       // Sensor de luz
#define BUTTON 33             // Botón inicio pomodoro
#define DHTPIN 3              // Pin del sensor DHT
#define DHTTYPE DHT11         // Tipo de sensor DHT
#define TRIG_ULTRASONIC 25    // Pin TRIG para sensor ultrasónico
#define ECHO_ULTRASONIC 26    // Pin ECHO para sensor ultrasónico
#define SERVO_PIN 13

#define RFID_RST 18
#define RFID_SS 19


// Objetos WiFi, MQTT y DHT
WiFiClient espClient;
PubSubClient client(espClient);

Sala* sala = nullptr;
Cubiculo* cub = nullptr;

// Conexión a WiFi
void initWifi() {
    WiFi.begin(WIFI_SSID, WIFI_PASS); // Conectar a la red WiFi
    Serial.print("Conectando a WiFi");
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("\nConectado a WiFi");
}

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);

    initWifi();

    // Conexión MQTT, encapsulada en un objeto, para pasarla
    // a la sala y el cubículo y realizar allí el pubsub.
    MQTT mqtt(MQTT_SERVER, MQTT_PORT, MQTT_USER, MQTT_PASS, espClient, client);

    mqtt.publish("esp/checklist", "1. Conexión exitosa con Wifi y MQTT");  // Mensaje de prueba

    unsigned ID = 1;

    Timer timer;
    mqtt.publish("esp/checklist", "2. Timer");  // Mensaje de prueba

    int pomodoro[] = {P_LED1, P_LED2, P_LED3, P_LED4, P_LED5, P_LED_AMARILLO}; // 6 PINES DEL POMODORO, PRIMERO LOS 5 ROJOS Y LUEGO EL AMARILLO
    Button button(BUTTON);
    Leds leds(RED_LED, GREEN_LED, pomodoro, timer, button);
    mqtt.publish("esp/checklist", "3. Leds y botón del pomodoro");  // Mensaje de prueba

    Sensor s_luz(SLUZ, "Luz");
    mqtt.publish("esp/checklist", "4. Sensor de luz");  // Mensaje de prueba

    Sensor s_sonido(SSONIDO, "Sonido");
    mqtt.publish("esp/checklist", "5. Sensor de sonido");  // Mensaje de prueba

    Sensor_US s_posicion(TRIG_ULTRASONIC, ECHO_ULTRASONIC);
    mqtt.publish("esp/checklist", "6. Sensor de ultrasonido");  // Mensaje de prueba

    Sensor_DHT s_dht(DHTPIN, DHTTYPE);
    mqtt.publish("esp/checklist", "7. Sensor de temperatura y humedad");  // Mensaje de prueba

    Cerradura cerradura(SERVO);
    mqtt.publish("esp/checklist", "8. Servo");  // Mensaje de prueba

    RFID escaner(RFID_RST, RFID_SS);
    mqtt.publish("esp/checklist", "9. RFID");  // Mensaje de prueba

    if(sala) sala = new Sala(ID, s_dht, cerradura, escaner, mqtt);
    mqtt.publish("esp/checklist", "10. Sala");  // Mensaje de prueba

    if (cub) cub = new Cubiculo(ID, leds, s_luz, s_sonido, s_posicion, s_dht, button, mqtt);
    mqtt.publish("esp/checklist", "11. Cubiculo construido");  // Mensaje de prueba
}

void loop() {
    if (MODE == SALA) {
        sala->update();
    } else if (MODE == CUBICULO) {
        cub->update();
    }
}

