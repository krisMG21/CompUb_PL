#ifndef BUZZER_H
#define BUZZER_H

#include <Arduino.h>

class Buzzer {
    int pin;
public:
    Buzzer(int pin);
    void beep(int duration);
};

#endif // BUZZER_H
