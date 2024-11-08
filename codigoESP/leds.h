#ifndef LEDS_H
#define LEDS_H

#ifdef ARDUINO
#include <Arduino.h>
#else
    #define INPUT 0
    #define OUTPUT 1
    #define digitalWrite(pin, val) ((void)0)
    #define pinMode(pin, mode) ((void)0)
#endif

#include "timer.h"

class Leds {
private:
    int led_red;
    int led_green;
    int leds_pomodoro[6]; //Ultimo para finalización
    int pomodoro_duration;

    Timer timer;
    bool ocupado;
public:
    //Constructor
    Leds(int red, int green, int pomodoro[6], Timer& timer):timer(timer){}; //Pines de los leds
    void init();
    void set_ocupado(bool ocupado);
    void change_ocupado();
    void update();
};


#endif // LEDS_H
