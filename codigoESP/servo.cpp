#include "servo.h"

Cerradura::Cerradura(int pin):servoPin(pin) {
    Servo myservo;
    ESP32PWM::allocateTimer(0);           // Asigna un temporizador para el PWM del servo
    myservo.setPeriodHertz(50);           // Configura la frecuencia del servo a 50 Hz
    myservo.attach(servoPin, 1000, 2000); // Asocia el servo al pin con un rango de pulso
}

void Cerradura::set_position(int new_position) {
    myservo.write(new_position);
    position = new_position;
}

void Cerradura::abrir() {
    set_position(90);
}

void Cerradura::cerrar() {
    set_position(0);
}

void Cerradura::mover(int added) {
    set_position(position + added);
}
