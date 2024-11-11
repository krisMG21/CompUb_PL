#ifndef CUBICULO_H
#define CUBICULO_H
#include "pomodoro.h"
#include "sensor.h"
#include "mqtt.h"

#include <string>

class Cubiculo {
private:
    unsigned ID;
    int state;

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
#endif //CUBICULO_H
