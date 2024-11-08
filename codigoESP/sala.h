#ifndef SALA_H
#define SALA_H

#include "leds.h"
#include "timer.h"
#include "sensor.h"
#include "button.h"

class Sala {
private:
    //Usuario usuario;
    bool ocupada;
    bool reservada;

    //Servo cerradura;
    //RFID escaner;

    Sensor_DHT s_dht;

public:
    Sala(const Sensor_DHT& s_dht): s_dht(s_dht) {
        ocupada = false;
        reservada = false;
    };



    void update();


};
#endif //SALA_H
