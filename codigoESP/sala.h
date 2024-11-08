#ifndef SALA_H
#define SALA_H

#include "leds.h"
#include "timer.h"
#include "sensor.h"
#include "button.h"

class Sala {
private:
    bool occupied;
    Timer timer;
    Leds leds;
    Sensor s_luz;
    Sensor s_sonido;
    Sensor_US s_posicion;
    Button button;

public:
    Sala(
        const Leds& leds, const Sensor& s_luz, const Sensor& s_sonido, const Sensor_US& s_posicion, const Button& button): leds(leds), s_luz(s_luz), s_sonido(s_sonido), s_posicion(s_posicion), button(button) {
        occupied = false;
    };

    void update();


};
#endif //SALA_H
