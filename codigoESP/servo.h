#ifndef SERVO_H
#define SERVO_H

#include <ESP32Servo.h>

class Cerradura {
private:
    Servo servo;
    int servoPin;
    int position;
public:
    Cerradura(int pin);
    void abrir();
    void cerrar();
};

#endif // SERVO_H
