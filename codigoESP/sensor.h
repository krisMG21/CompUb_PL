#ifndef SENSOR_H
#define SENSOR_H
#ifdef ARDUINO
#include <Arduino.h>
#else
    #define HIGH 1
    #define LOW 0
    #define pinMode(pin, mode) ((void)0)
    #define digitalRead(pin) (0)
    #define digitalWrite(pin, val) ((void)0)
    #define analogRead(pin) (0)
    #define delayMicroseconds(ms) ((void)0)
    #define pulseIn(pin, state) (0)
#endif

#include <cstdio>
#include <string>

class Sensor { //Sensores analogicos
private:
    int pin;
    std::string nombre;
public:
    Sensor(int pin, std::string nombre):pin(pin), nombre(nombre) {};
    int read();
};

class Sensor_US { //Sensor de ultrasonido
private:
    int pin_trigger;
    int pin_echo;
public:
    Sensor_US(int pin_trigger, int pin_echo):pin_trigger(pin_trigger), pin_echo(pin_echo) {
        pinMode(pin_trigger, OUTPUT);
        pinMode(pin_echo, INPUT);
    };
    int readDistance();

};

#endif // SENSOR_H
