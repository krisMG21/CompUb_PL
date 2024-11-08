#include "sensor.h"

int Sensor::read(){
    int value = analogRead(pin);
    Serial.print("Valor del sensor de " + nombre + ": ");
    Serial.println(value);
    return value;
}

int Sensor_US::readDistance(){
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
    // Prints the distance on the Serial Monitor
    Serial.print("Distance: ");
    Serial.println(distance);
    return distance;
}

bool Sensor_US::ocupado(){
    return readDistance() < 10;
}

float Sensor_DHT::readTemperature(){
    return dht.readTemperature();
}

float Sensor_DHT::readHumidity(){
    return dht.readHumidity();
}

void Sensor_DHT::printDHT(){
    float temperature_f = readTemperature();
    float humidity_f = readHumidity();
    Serial.print("Temperatura: ");
    Serial.print(temperature_f);
    Serial.print(" C");
    Serial.print(" Humedad: ");
    Serial.print(humidity_f);
    Serial.println(" %");
}
