#include "mqtt.h"

MQTT::MQTT(char* mqttServer, int mqttPort, char* mqttUser, char* mqttPassword, WiFiClient& espClient, PubSubClient& client):
    mqttServer(mqttServer),
    mqttPort(mqttPort),
    mqttUser(mqttUser),
    mqttPassword(mqttPassword),
    espClient(espClient),
    client(client) {
    initMQTTServer();
}

// Conexión al servidor MQTT
void MQTT::initMQTTServer() {
  client.setServer(mqttServer, mqttPort);
  reconnectMQTT();
}

void MQTT::reconnectMQTT() {
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
