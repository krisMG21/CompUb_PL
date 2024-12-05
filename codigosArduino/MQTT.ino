#include <WiFi.h>
#include <PubSubClient.h>

// Pines de sensores
#define LIGHT_SENSOR 12       // Sensor de luz

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

// Configuración inicial
void setup() {
  Serial.begin(9600);
  pinMode(LIGHT_SENSOR, INPUT);

  // Inicializar WiFi y servidor MQTT
  initWifi();
  initMQTTServer();
}
//No lee bien
// Leer luz
int read_light_sensor() {
  int light_value = analogRead(LIGHT_SENSOR);
  Serial.print("Valor del sensor de luz: ");
  Serial.println(light_value);
  return light_value;
}

void loop() {
  // Leer el sensor de luz
  int light_value = read_light_sensor();
  
  
  // Publicar información en MQTT si está conectado
  if (client.connected()) {
    client.publish(light_topic, String(light_value).c_str());
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
    // Intentar conexión con credenciales
    if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
      Serial.println("Conectado a MQTT");
      client.publish("esp/test", "Hello from ESP32");  // Mensaje de prueba
    } else {
      Serial.print("Fallo, rc=");
      Serial.print(client.state());
      Serial.println(" Reintentando en 2 segundos...");
      delay(2000);
    }
  }
}