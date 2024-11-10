#ifndef SENSOR_H
#define SENSOR_H

#include <DHT.h>
#include <cstdint>
#define DHTTYPE DHT11         // Tipo de sensor DHT

#include <Arduino.h>
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
    int read(); //Devuelve la distancia en cm
    bool ocupado(); //Devuelve si la distancia es menor a 10cm

};

class Sensor_DHT { //Sensor de temperatura y humedad
private:
    DHT dht; //DHT11

public:
    Sensor_DHT(uint8_t pin, uint8_t type): dht(pin, type) {;
        pinMode(pin, INPUT);
    };
    float readTemperature(); //Devuelve la temperatura en grados Celsius
    float readHumidity(); //Devuelve la humedad en porcentaje
    void printDHT(); //Imprime la temperatura y humedad
};


#endif // SENSOR_H
