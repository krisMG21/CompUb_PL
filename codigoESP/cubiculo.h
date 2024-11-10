#ifndef SALA_H
#define SALA_H

#include "comps/pomodoro.h"
#include "comps/sensor.h"
#include "logic/mqtt.h"

class Cubiculo {
private:
    unsigned ID;
    int state = 0;

    Leds leds; //Leds del pomodoro
    Button button; //Botón de inicio pomodoro
    //
    Sensor s_luz;
    Sensor s_sonido;
    Sensor_US s_posicion; //Sensor de ocupación
    Sensor_DHT s_dht; //Sensor de temperatura y humedad

    MQTT mqtt;

public:
    Cubiculo(
        const unsigned ID,
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
