#ifndef SALA_H
#define SALA_H

#include <string>
#include <Arduino.h>

#include "sensor.h"
#include "servo.h"
#include "rfid.h"
#include "mqtt.h"


class Sala {
private:
    static const Sala sala;
    unsigned ID;
    unsigned long userID; // Usuario con reserva de la sala en este momento
    unsigned long last_reading;

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

    void setUserID(unsigned long userID);
    void setReservedTime(int time);

    void reservar(unsigned long userID, int time);
    void cancelarReserva();

    void abrir(); //Abre la sala con el servo
    void cerrar(); //Cierra la sala con el servo

    bool is_reservada(); //Compara tiempo transcurrido con el tiempo reservado
    bool is_ocupada(); //Cuando se abre, se marca como ocupada, cuando se cierra se interpreta como libre

    void update();


};
#endif //SALA_H
