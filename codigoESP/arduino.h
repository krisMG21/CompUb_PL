#ifndef ARDUINO_H
#define ARDUINO_H

#include <string>

#ifdef ARDUINO
#include <Arduino.h>
#else
    #define HIGH 1
    #define LOW 0
    #define pinMode(pin, mode) ((void)0)
    #define digitalWrite(pin, val) ((void)0)
    #define analogRead(pin) (0)
    #define delayMicroseconds(ms) ((void)0)
    #define millis() (0)
    #define pulseIn(pin, state) (0)

    #define DHTTYPE DHT11
    #define dht(pin, type) DHT()
#endif

class SerialClass {
public:
    template<typename T>
    void print(T message);
    template<typename T>
    void println(T message);
};
extern SerialClass Serial;

class DHT {
public:
    DHT();
    void begin();
    int readTemperature();
    int readHumidity();
};

#endif // ARDUINO_H
