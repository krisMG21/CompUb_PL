#include "servo.h"

Cerradura::Cerradura(int pin):servoPin(pin) {
    Servo servo;
    ESP32PWM::allocateTimer(0);           // Asigna un temporizador para el PWM del servo
    servo.setPeriodHertz(50);           // Configura la frecuencia del servo a 50 Hz
    servo.attach(servoPin, 1000, 2000); // Asocia el servo al pin con un rango de pulso
}

void Cerradura::abrir() {
    position = 90;
    servo.write(position);
}

void Cerradura::cerrar() {
    position = 0;
    servo.write(position);
}
