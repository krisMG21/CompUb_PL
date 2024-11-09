#ifndef SERVO_H
#define SERVO_H

#ifdef ARDUINO
#include <ESP32Servo.h>
#else
    #include "../arduino.h"
#endif

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
