#include <cstdio>
#include <DHT.h>
#include <WiFi.h>
#include <PubSubClient.h>

#include "config.h" //Credenciales de WiFi y MQTT
#include "sala.h" //Objeto de la sala
#include "cubiculo.h" //Objeto del cubículo

// NOTE: MODO DE EJECUCIÓN
// ==================================================
typdef enum {SALA, CUBICULO} tipo;
#define MODE SALA
// #define MODE CUBICULO


// NOTE: PINES
// ==================================================
#define RED_LED 2             // LED de ocupación del cubículo: ROJO
#define GREEN_LED 0           // LED de ocupación del cubículo: VERDE
#define BUZZER 15             // Buzzer
#define SSONIDO 35            // Sensor de sonido
#define SLUZ 12       // Sensor de luz
#define BUTTON 21             // Botón inicio pomodoro
#define DHTPIN 3              // Pin del sensor DHT
#define DHTTYPE DHT11         // Tipo de sensor DHT
#define TRIG_ULTRASONIC 25    // Pin TRIG para sensor ultrasónico
#define ECHO_ULTRASONIC 26    // Pin ECHO para sensor ultrasónico


// Objetos WiFi, MQTT y DHT
WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);

    initWifi();

    // Conexión MQTT, encapsulada en un objeto, para pasarla
    // a la sala y el cubículo y realizar allí el pubsub.
    MQTT mqtt(MQTT_SERVER, MQTT_PORT, MQTT_USER, MQTT_PASS, espClient, client);

    switch (MODE) {
        case SALA:
            Serial.println("Modo: sala");
            unsigned ID = 1;

            Sensor_DHT s_dht(DHTPIN);
            Servo cerradura(SERVO_PIN);
            RFID escaner(RFID_RST, RFID_SS);

            MQTT mqtt(MQTT_SERVER, MQTT_PORT, MQTT_USER, MQTT_PASS, espClient, client);

            Sala sala(ID, s_dht, cerradura, escaner, mqtt);

            break;

        case CUBICULO:
            Serial.println("Modo: cubículo");
            unsigned ID = 1;

            Timer timer;

            Leds leds(RED_LED, GREEN_LED, pomodoro, timer);
            Button button(BUTTON);

            Sensor s_luz(SLUZ, "Luz");
            Sensor s_sonido(SSONIDO, "Sonido");
            Sensor_US s_posicion(TRIG_ULTRASONIC, ECHO_ULTRASONIC);
            Sensor_DHT s_dht(DHTPIN);

            Cubiculo cub(ID, leds, s_luz, s_sonido, s_posicion, s_dht, button);

            break;
    }
}

void loop() {
    if (MODE == SALA) {
        sala.update();
    } else if (MODE == CUBICULO) {
        cub.update();
    }
}

// Conexión a WiFi
void initWifi() {
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Conectando a WiFi");
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("\nConectado a WiFi");
}
