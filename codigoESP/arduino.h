#ifndef ARDUINO_H
#define ARDUINO_H

#include <string>

#ifdef ARDUINO
#include <Arduino.h>
#else
    #define HIGH 1
    #define LOW 0
    #define INPUT 0
    #define OUTPUT 1

    #define pinMode(pin, mode) ((void)0)
    #define digitalRead(pin) (0)
    #define digitalWrite(pin, val) ((void)0)
    #define analogRead(pin) (0.0)
    #define analogWrite(pin, val) ((void)0)

    #define delayMicroseconds(ms) ((void)0)
    #define pulseIn(pin, state) (0)
    #define millis() (0)
    #define delay(ms) ((void)0)

    #define String(x) (std::to_string(x))

    #define DHTTYPE DHT11
    #define dht(pin, type) DHT()
#endif

class SerialClass {
public:
    void print(std::string message);
    void print(int message);
    void println(std::string message);
    void println(int message);
};
SerialClass Serial;

class DHT {
public:
    DHT();
    void begin();
    int readTemperature();
    int readHumidity();
};

class Servo {
public:
    Servo();
    void write(int position);
    void attach(int pin, int min, int max);
    void setPeriodHertz(int hertz);
};

#endif // ARDUINO_H
