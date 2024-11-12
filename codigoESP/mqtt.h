#ifndef MQTT_H
#define MQTT_H

#include <PubSubClient.h>
#include <WiFiClient.h>
#include <map>
#include <functional>
#include <string>

class MQTT {
private:
    WiFiClient espClient;
    PubSubClient client;
    const char* mqttServer;  // Dirección del servidor MQTT
    const int mqttPort;
    const char* mqttUser;
    const char* mqttPassword;

    // static void callback(char* topic, std::byte* payload, unsigned int length);

    static MQTT* instance;

public:
    MQTT(char* MQTT_SERVER, int MQTT_PORT, char* MQTT_USER, char* MQTT_PASS, WiFiClient espClient, PubSubClient client);

    void initMQTTServer();
    void reconnectMQTT();

    // void subscribe(const std::string& topic, std::function<void(const std::string&)> cb);
    // void loop();
    void publish(const std::string& topic, const std::string& message);
};

#endif // MQTT_H
