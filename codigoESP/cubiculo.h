#ifndef SALA_H
#define SALA_H

#include "leds.h"
#include "timer.h"
#include "sensor.h"
#include "button.h"

class Cubiculo {
private:
    bool occupied;
    Leds leds;
    Sensor s_luz;
    Sensor s_sonido;
    Sensor_US s_posicion;
    Sensor_DHT s_dht;
    Button button;
    // Ventilador
    Timer timer;

public:
    Cubiculo(
        const Leds& leds,
        const Sensor& s_luz,
        const Sensor& s_sonido,
        const Sensor_US& s_posicion,
        const Sensor_DHT& s_dht,
        const Button& button):
        leds(leds),
        s_luz(s_luz),
        s_sonido(s_sonido),
        s_posicion(s_posicion),
        s_dht(s_dht),
        button(button) {
        occupied = false;
    };

    void update();


};
#endif //SALA_H
