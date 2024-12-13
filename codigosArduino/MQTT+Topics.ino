#include <WiFi.h>
#include <PubSubClient.h>

// Pines de sensores y actuadores
#define LIGHT_SENSOR 12       // Sensor de luz
#define TEMP_SENSOR 34        // Sensor de temperatura (simulado aquí)
#define HUMIDITY_SENSOR 35    // Sensor de humedad (simulado aquí)
#define ACTUADOR_PIN 14       // Actuador (ejemplo: control de luz)

// Credenciales de WiFi y MQTT
#define WIFI_SSID "iPhone de Martin"
#define WIFI_PASSWORD "contrasea"
const char* mqttServer = "172.20.10.2";  // Dirección del servidor MQTT
const int mqttPort = 1883;
const char* mqttUser = "ubicua";
const char* mqttPassword = "ubicua";

// Tópicos MQTT
#define LIGHT_TOPIC "station1/light"
#define TEMP_TOPIC "station1/temperature"
#define HUMIDITY_TOPIC "station1/humidity"
#define ACTUADOR_TOPIC "station1/actuator"

// Objetos WiFi, MQTT
WiFiClient espClient;
PubSubClient client(espClient);

// Configuración inicial
void setup() {
  Serial.begin(9600);
  pinMode(LIGHT_SENSOR, INPUT);
  pinMode(ACTUADOR_PIN, OUTPUT);  // Configurar el pin del actuador como salida
  
  // Inicializar WiFi y servidor MQTT
  initWifi();
  initMQTTServer();
}

// Leer sensores
int readLightSensor() {
  int light_value = analogRead(LIGHT_SENSOR);
  return light_value;
}

int readTemperatureSensor() {
  // Simulación de lectura de temperatura (puedes conectar un sensor real)
  int temp_value = analogRead(TEMP_SENSOR);
  return temp_value;
}

int readHumiditySensor() {
  // Simulación de lectura de humedad (puedes conectar un sensor real)
  int humidity_value = analogRead(HUMIDITY_SENSOR);
  return humidity_value;
}

void controlActuator(int value) {
  // Encender o apagar el actuador basado en el valor recibido
  if (value == 1) {
    digitalWrite(ACTUADOR_PIN, HIGH);  // Encender actuador
  } else {
    digitalWrite(ACTUADOR_PIN, LOW);   // Apagar actuador
  }
}

void loop() {
  // Leer los sensores
  int light_value = readLightSensor();
  int temp_value = readTemperatureSensor();
  int humidity_value = readHumiditySensor();
  
  // Publicar los datos de los sensores en los tópicos correspondientes
  if (client.connected()) {
    client.publish(LIGHT_TOPIC, String(light_value).c_str());
    client.publish(TEMP_TOPIC, String(temp_value).c_str());
    client.publish(HUMIDITY_TOPIC, String(humidity_value).c_str());
  } else {
    reconnectMQTT();  // Reintentar conexión si no está conectado
  }

  client.loop();  // Mantener la conexión
  delay(1000);    // Ajusta según el intervalo necesario
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

// Conexión al servidor MQTT
void initMQTTServer() {
  client.setServer(mqttServer, mqttPort);
  reconnectMQTT();
}

void reconnectMQTT() {
  while (!client.connected()) {
    Serial.print("Intentando conexión MQTT...");
    if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
      Serial.println("Conectado a MQTT");
      client.publish("esp/test", "Hello from ESP32");
    } else {
      Serial.print("Fallo, rc=");
      Serial.print(client.state());
      Serial.println(" Reintentando en 2 segundos...");
      delay(2000);
    }
  }
}
