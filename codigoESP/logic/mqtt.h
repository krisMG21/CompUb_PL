#include "../arduino.h"

#ifndef MQTT_H
#define MQTT_H

#ifndef ARDUINO
     class WiFiClient{
        public:
            void begin(std::string ssid, std::string password);
            int status();
            void print(std::string message);
};
     class PubSubClient{
        public:
            void setServer(std::string server, int port);
            bool connect(std::string id, std::string user, std::string password);
            bool connected();
            void publish(std::string topic, std::string message);
            int state();
};
#endif

class MQTT {
private:
    const char* mqttServer;  // Dirección del servidor MQTT
    const int mqttPort;
    const char* mqttUser;
    const char* mqttPassword;


    WiFiClient espClient;
    PubSubClient client;

public:
    MQTT(char* mqttServer, int mqttPort, char* mqttUser, char* mqttPassword, WiFiClient& espClient, PubSubClient& client);
    void initMQTTServer();
    void reconnectMQTT();
    void tryPublish(std::string topic, std::string message);
};

#endif // MQTT_H
