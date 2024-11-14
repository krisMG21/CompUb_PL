#include "mqtt.h"
#include <Arduino.h>

// MQTT* MQTT::instance = nullptr;

MQTT::MQTT(char* mqttServer, int mqttPort, char* mqttUser, char* mqttPassword, WiFiClient& espClient, PubSubClient& client):
    mqttServer(mqttServer),
    mqttPort(mqttPort),
    mqttUser(mqttUser),
    mqttPassword(mqttPassword),
    espClient(espClient),
    client(client) {
    // instance = this;

    // initMQTTServer();
    // client.setCallback(callback);
}

// void MQTT::callback(char* topic, std::byte* payload, unsigned int length) {
//     if (instance != nullptr) {
//         std::string topicStr(topic);
//         std::string message(reinterpret_cast<char*>(payload), length);
//         // Llamar al callback correspondiente si existe
//         if (topic == "sala/1/user_reserva") {
//             sala->reservar(std::stoul(message), 60);
//         }
//     }
// }

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
        } else {
            Serial.print("Fallo, rc=");
            Serial.print(client.state());
            Serial.println(" Reintentando en 2 segundos...");
            delay(2000);
        }
    }
}


// void MQTT::subscribe(const std::string& topic) {
//     client.subscribe(topic.c_str());
// }
//
// void MQTT::loop() {
//     client.loop();
// }
//
void MQTT::publish(const std::string topic, const std::string message) {
    Serial.print(topic.c_str());
    Serial.print("/");
    Serial.println(message.c_str());
    // Serial.println("ANTES DE PUBLISH");
    // client.publish(topic.c_str(), message.c_str());
    // Serial.println("DESPUES DE PUBLISH");
    // delay(1000);
}
