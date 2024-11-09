#ifndef SALA_H
#define SALA_H

#include "comps/pomodoro.h"
#include "comps/sensor.h"
#include "logic/mqtt.h"

class Cubiculo {
private:
    bool occupied;
    int state;
    Leds leds;
    Sensor s_luz;
    Sensor s_sonido;
    Sensor_US s_posicion;
    Sensor_DHT s_dht;
    Button button;

    MQTT mqtt;

public:
    Cubiculo(
        const Leds& leds,
        const Sensor& s_luz,
        const Sensor& s_sonido,
        const Sensor_US& s_posicion,
        const Sensor_DHT& s_dht,
        const Button& button,
        const MQTT& mqtt);

    void update();
};
#endif //SALA_H
