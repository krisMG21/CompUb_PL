#include <cstdio>
#include <DHT.h>
#include <WiFi.h>
#include <PubSubClient.h>

#include "sala.h"
#include "cubiculo.h"

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

// Credenciales de WiFi y MQTT
#define WIFI_SSID "iPhone de Martin"
#define WIFI_PASSWORD "contrasea"
const char* mqttServer = "172.20.10.2";  // Dirección del servidor MQTT
const int mqttPort = 1883;
const char* mqttUser = "ubicua";
const char* mqttPassword = "ubicua";

// Tópicos MQTT
//Solo luz
#define light_topic "station1/light"

// Objetos WiFi, MQTT y DHT
WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);

    Timer timer;

    Leds leds(RED_LED, GREEN_LED, pomodoro, timer);

    Sensor s_luz(SLUZ, "Luz");
    Sensor s_sonido(SSONIDO, "Sonido");
    Sensor_US s_posicion(TRIG_ULTRASONIC, ECHO_ULTRASONIC);
    Sensor_DHT s_dht(DHTPIN);
    Button button(BUTTON);

    Cubiculo cub(leds, s_luz, s_sonido, s_posicion, s_dht, button);
    Sala sala(leds, cub);
}

void read_light_sensor() {
    int light_value = analogRead(SLUZ);
    Serial.print("Valor del sensor de luz: ");
    Serial.println(light_value);
}

void read_sound_sensor() {
    int sound_value = analogRead(SSONIDO);
    Serial.print("Valor del sensor de sonido: ");
    Serial.println(sound_value);
}

void readDHT(){
    float h = dht.readHumidity();
    float t = dht.readTemperature();

    if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
    }
    Serial.print("Humedad: ");
    Serial.println(h);
    Serial.print("Temperatura: ");
    Serial.print(t);
    Serial.println("°C ");
}

void loop() {
    read_sound_sensor();
    read_light_sensor();
    readDHT();
    delay(1000);
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


