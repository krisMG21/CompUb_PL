#ifndef POMODORO_H
#define POMODORO_H

#include <Arduino.h>
#include "timer.h"

class Button {
private:
    int pin;
public:
    Button(int pin) : pin(pin){};
    int read();
};


class Leds {
private:
    int led_red;
    int led_green;
    int leds_pomodoro[6]; //Ultimo para finalización
    int pomodoro_duration;

    Timer timer;
    bool ocupado;

    Button button;

public:
    //Constructor
    Leds(int red, int green, int pomodoro[6], Timer& timer, Button& button); //Pines de los leds
    void init();
    void set_ocupado(bool ocupado);
    void start_pomodoro();
    void stop_pomodoro();
    void update_pomodoro();
};


#endif // POMODORO_H