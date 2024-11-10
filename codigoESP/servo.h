#ifndef SERVO_H
#define SERVO_H

#include <ESP32Servo.h>

class Cerradura {
private:
    Servo myservo;
    int servoPin;
    int position;
public:
    Cerradura(int pin);
    void set_position(int position);
    void abrir();
    void cerrar();
    void mover(int position);
};

#endif // SERVO_H
