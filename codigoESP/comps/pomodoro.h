#ifndef LEDS_H
#define LEDS_H

#include "../arduino.h"
#include "../logic/timer.h"

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
    Leds(int red, int green, int pomodoro[6], Timer& timer); //Pines de los leds
    void init();
    void set_ocupado(bool ocupado);
    void change_ocupado();
    void update();
};

class Button {
private:
    int pin;
public:
    Button(int pin) : pin(pin){};
    bool isPressed();
    int read();
};


#endif // LEDS_H
