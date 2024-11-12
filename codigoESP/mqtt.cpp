#include "mqtt.h"
#include <Arduino.h>

MQTT* MQTT::instance = nullptr;

// void MQTT::callback(char* topic, std::byte* payload, unsigned int length) {
//     std::string topicStr(topic);
//     std::string message(reinterpret_cast<char*>(payload), length);
//     // Llamar al callback correspondiente si existe
//     if (topicStr == "")
// }

MQTT::MQTT(char* mqttServer, int mqttPort, char* mqttUser, char* mqttPassword, WiFiClient& espClient, PubSubClient& client):
    mqttServer(mqttServer),
    mqttPort(mqttPort),
    mqttUser(mqttUser),
    mqttPassword(mqttPassword),
    espClient(espClient),
    client(client) {
    initMQTTServer();
    instance = this;
}

void MQTT::initMQTTServer() {
    client.setServer(mqttServer, mqttPort);
    //reconnectMQTT();
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


// void MQTT::subscribe(const std::string& topic, std::function<void(const std::string&)> cb) {
//     client.subscribe(topic.c_str());
//     callbacks[topic] = cb;
// }
//
// void MQTT::loop() {
//     client.loop();
// }

void MQTT::publish(const std::string topic, const std::string message) {
    Serial.println("Publishing message");
    Serial.println(message.c_str());
    //client.publish(topic.c_str(), message.c_str());
}
