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

// Declaración de objetos
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

    // WARNING: COMENTAR SI NO SE USA WIFI
    // initWifi();

    // Conexión MQTT, encapsulada en un objeto, para pasarla
    // a la sala y el cubículo y realizar allí el pubsub.
    // WARNING: COMENTAR SI NO SE USA MQTT
    // MQTT mqtt(MQTT_SERVER, MQTT_PORT, MQTT_USER, MQTT_PASS, espClient, client);

    MQTT mqtt;

    unsigned ID = 1;

    if (MODE == SALA) {
        Serial.println("SISTEMA MODO SALA");
    } else if (MODE == CUBICULO) {
        Serial.println("SISTEMA MODO CUBICULO");
    }

    Timer timer;

    int pomodoro[] = {P_LED1, P_LED2, P_LED3, P_LED4, P_LED5, P_LED_AMARILLO}; // 6 PINES DEL POMODORO, PRIMERO LOS 5 ROJOS Y LUEGO EL AMARILLO
    Button button(BUTTON);
    Leds leds(RED_LED, GREEN_LED, pomodoro, timer, button);
    Serial.println("Sistema pomodoro iniciado");

    Sensor s_luz(SLUZ, "Luz");
    Serial.println("Sensor de luz iniciado");

    Sensor s_sonido(SSONIDO, "Sonido");
    Serial.println("Sensor de sonido iniciado");

    Sensor_US s_posicion(TRIG_ULTRASONIC, ECHO_ULTRASONIC);
    Serial.println("Sensor de ultrasonido iniciado");

    Sensor_DHT s_dht(DHTPIN, DHTTYPE);
    Serial.println("Sensor de temperatura y humedad iniciado");

    Cerradura cerradura(SERVO);
    Serial.println("Servo iniciado");

    RFID escaner(RFID_RST, RFID_SS);
    Serial.println("RFID iniciado");

    if(sala) {
        sala = new Sala(ID, s_dht, cerradura, escaner, mqtt);
        Serial.println("Sala iniciada");
    }

    if (cub) {
        cub = new Cubiculo(ID, leds, s_luz, s_sonido, s_posicion, s_dht, button, mqtt);
        Serial.println("Cubiculo iniciado");
    }

}

void loop() {
    if (MODE == SALA) {
        sala->update();
    } else if (MODE == CUBICULO) {
        cub->update();
    }
}

