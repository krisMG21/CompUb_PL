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
// #define MODE SALA
#define MODE CUBICULO


// NOTE: PINES
// ==================================================
#define RED_LED 2             // LED de ocupación del cubículo: ROJO
#define GREEN_LED 0           // LED de ocupación del cubículo: VERDE

// LEDs del pomodoro
// #define P_LED1 0
// #define P_LED2 0
// #define P_LED3 0
// #define P_LED4 0
// #define P_LED5 0
// #define P_LED_AMARILLO 0

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

    mqtt.publish("esp/checklist", "1. Conexión exitosa con Wifi y MQTT");  // Mensaje de prueba

    switch (MODE) {
        case SALA:
            Serial.println("Modo: sala");
            unsigned ID = 1;

            Sensor_DHT s_dht(DHTPIN);
            mqtt.publish("esp/checklist", "2. Sensor de temperatura y humedad");  // Mensaje de prueba
            Servo cerradura(SERVO_PIN);
            mqtt.publish("esp/checklist", "3. Servo");  // Mensaje de prueba
            RFID escaner(RFID_RST, RFID_SS);
            mqtt.publish("esp/checklist", "4. RFID");  // Mensaje de prueba

            Sala sala(ID, s_dht, cerradura, escaner, mqtt);
            mqtt.publish("esp/checklist", "5. Sala");  // Mensaje de prueba

            break;

        case CUBICULO:
            Serial.println("Modo: cubículo");
            unsigned ID = 1;

            Timer timer;
            mqtt.publish("esp/checklist", "2. Timer");  // Mensaje de prueba

            int* pomodoro = []; // 6 PINES DEL POMODORO, PRIMERO LOS 5 ROJOS Y LUEGO EL AMARILLO
            Leds leds(RED_LED, GREEN_LED, pomodoro, timer);
            Button button(BUTTON);
            mqtt.publish("esp/checklist", "3. Leds y botón del pomodoro");  // Mensaje de prueba


            Sensor s_luz(SLUZ, "Luz");
            mqtt.publish("esp/checklist", "4. Sensor de luz");  // Mensaje de prueba

            Sensor s_sonido(SSONIDO, "Sonido");
            mqtt.publish("esp/checklist", "5. Sensor de sonido");  // Mensaje de prueba

            Sensor_US s_posicion(TRIG_ULTRASONIC, ECHO_ULTRASONIC);
            mqtt.publish("esp/checklist", "6. Sensor de ultrasonido");  // Mensaje de prueba

            Sensor_DHT s_dht(DHTPIN);
            mqtt.publish("esp/checklist", "7. Sensor de temperatura y humedad");  // Mensaje de prueba

            Cubiculo cub(ID, leds, s_luz, s_sonido, s_posicion, s_dht, button);
            mqtt.publish("esp/checklist", "8. Cubiculo construido");  // Mensaje de prueba

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
