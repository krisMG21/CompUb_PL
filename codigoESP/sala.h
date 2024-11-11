#ifndef SALA_H
#define SALA_H

#include <string>
#include <Arduino.h>

#include "sensor.h"
#include "servo.h"
#include "rfid.h"
#include "mqtt.h"

typedef struct {
    int id;
} Usuario;

class Sala {
private:
    unsigned ID;
    int state = 0;

    bool ocupada;
    bool reservada;

    unsigned long startTime;
    unsigned long elapsedTime; //Tiempo transcurrido desde la reserva
    unsigned long reservedTime;

    Cerradura cerradura;
    RFID escaner;

    Sensor_DHT s_dht;

    MQTT mqtt;

public:
    Sala(const unsigned ID, const Sensor_DHT& s_dht, const Cerradura& cerradura, const RFID& rfid, const MQTT& mqtt);

    void init(const unsigned ID, const Sensor_DHT& s_dht, const Cerradura& cerradura, const int RFID_RST, const int RFID_SS, const MQTT& mqtt);

    void reservar(Usuario& usuario, int time);

    void abrir(); //Abre la sala con el servo
    void cerrar(); //Cierra la sala con el servo

    bool is_reservada(); //Compara tiempo transcurrido con el tiempo reservado
    bool is_ocupada(); //Cuando se abre, se marca como ocupada, cuando se cierra se interpreta como libre

    void update();


};
#endif //SALA_H
