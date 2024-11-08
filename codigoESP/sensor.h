#include "arduino.h"

#ifndef SENSOR_H
#define SENSOR_H


#include <cstdio>
#include <string>

class Sensor { //Sensores analogicos
private:
    int pin;
    std::string nombre;
public:
    Sensor(int pin, std::string nombre):pin(pin), nombre(nombre) {
        pinMode(pin, INPUT);
    };
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
    bool ocupado();

};

class Sensor_DHT { //Sensor de temperatura y humedad
private:
    int pin;
    DHT dht;

public:
    Sensor_DHT(int pin):pin(pin) {
        pinMode(pin, INPUT);
        dht = dht(pin, DHTTYPE);
    };
    float readTemperature();
    float readHumidity();
    void printDHT();
};


#endif // SENSOR_H
