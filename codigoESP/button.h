#ifndef BUTTON_H
#define BUTTON_H

#ifdef ARDUINO
#include <Arduino.h>
#else
    #define HIGH 1
    #define LOW 0
    #define digitalRead(pin) (0)
#endif

#include <cstdio>
#include <string>

class Button {
private:
    int pin;
public:
    Button(int pin) : pin(pin){};
    bool isPressed();
    int read();
};

#endif // BUTTON_H
