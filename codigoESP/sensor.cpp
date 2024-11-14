#include "sensor.h"

int Sensor::read(){
    int value = analogRead(pin);
    
    return value;
}

int Sensor_US::read(){
    // Clears the trigPin
    digitalWrite(pin_trigger, LOW);
    delayMicroseconds(2);
    // Sets the trigPin on HIGH state for 10 micro seconds
    digitalWrite(pin_trigger, HIGH);
    delayMicroseconds(10);
    digitalWrite(pin_trigger, LOW);
    // Reads the echoPin, returns the sound wave travel time in microseconds
    int duration = pulseIn(pin_echo, HIGH);
    // Calculating the distance
    int distance = duration * 0.034 / 2;
    return distance;
}

bool Sensor_US::ocupado(){
    return read() < 10;
}

float Sensor_DHT::readTemperature(){
    return dht.readTemperature();
}

float Sensor_DHT::readHumidity(){
    return dht.readHumidity();
}
