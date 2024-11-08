#include <WiFi.h>
#include <PubSubClient.h>
#include <DHT.h>
// Pines de sensores
int temperature = 0;
int light = 0;
int h=0;
int pinTemperature = 34;  
int pinLight = 35;      

// Credenciales de WiFi y MQTT
#define WIFI_SSID "iPhone de Martin"
#define WIFI_PASSWORD "contrasea"
const char* mqttServer = "172.22.88.103";
const int mqttPort = 1883;
const char* mqttUser = "ubicua";
const char* mqttPassword = "ubicua";

// Tópicos MQTT
#define light_topic "station1/humidity"
#define temperature_topic "station1/temperature"

// Objetos WiFi y MQTT
WiFiClient espClient;
PubSubClient client(espClient);

// Configuración inicial
void setup(){
  pinMode(pinTemperature, INPUT);
  pinMode(pinLight, INPUT);
  Serial.begin(9600);
   dht.begin();
  // Inicializar WiFi y servidor MQTT
  initWifi();
  initMQTTServer();
}
//leer luz
void read_light_sensor() {
  int light_value = analogRead(LIGHT_SENSOR);
  Serial.print("Valor del sensor de luz: ");
  Serial.println(light_value);
}
//leer tmep y hum
void readDHT(){
  float h = dht.readHumidity();
  float temperature = dht.readTemperature();

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
void loop(){
  // Leer sensores
    read_light_sensor();
    readDHT();
  
  // Publicar información en MQTT
  client.publish(temperature_topic, String(temperature).c_str());
  client.publish(light_topic, String(light_value).c_str());
  
  // Mantener la conexión con MQTT
  if (!client.connected()) {
    reconnectMQTT();
  }
  client.loop();
  
  delay(1000);  // Ajusta según el intervalo necesario
}

// Conexión a WiFi
void initWifi(){
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConnected to WiFi");
}

// Conexión al servidor MQTT
void initMQTTServer(){
  client.setServer(mqttServer, mqttPort);
  reconnectMQTT();
}

void reconnectMQTT() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Intentar conexión
    if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
      Serial.println("Connected to MQTT");
      client.publish("esp/test", "Hello from ESP32");
    } else {
      Serial.print("Failed, rc=");
      Serial.print(client.state());
      Serial.println(" retrying in 2 seconds");
      delay(2000);
    }
  }
}
