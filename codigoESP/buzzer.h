#include <Arduino.h>

class Buzzer {
private:
    const int buzzerPin;

public:
    Buzzer(int buzzerPin);
    void beep(int duration);
};
