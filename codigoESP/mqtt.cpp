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
    Serial.println("MQTT server connected");
    // client.setServer(mqttServer, mqttPort);
    // Serial.print("Servidor MQTT cargado.");
    // reconnectMQTT();
}

void MQTT::reconnectMQTT() {
    Serial.println("Connecting to MQTT");
    // while (!client.connected()) {
    //     Serial.print("Intentando conexión MQTT...");
    //     // Intentar conexión con credenciales
    //     if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
    //       Serial.println("Conectado a MQTT");
    //       client.publish("esp/test", "Hello from ESP32");  // Mensaje de prueba
    //     } else {
    //       Serial.print("Fallo, rc=");
    //       Serial.print(client.state());
    //       Serial.println(" Reintentando en 2 segundos...");
    //       delay(2000);
    //     }
    // }
}

void MQTT::publish(std::string topic, std::string message) {
    Serial.print("Publishing: ");
    Serial.print(topic);
    Serial.print(" - ");
    Serial.println(message);

    client.publish(topic.c_str(), message.c_str());

    // if (client.connected()) {
    //     client.publish(topic.c_str(), message.c_str());
    // } else {
    //     reconnectMQTT();  // Reintentar conexión si no está conectado
    // }
}
