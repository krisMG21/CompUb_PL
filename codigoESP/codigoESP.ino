#include <cstdio>
#include <DHT.h>
#include <WiFi.h>
#include <PubSubClient.h>

#define RED_LED 2             // LED de ocupación del cubículo: ROJO
#define GREEN_LED 0           // LED de ocupación del cubículo: VERDE
#define BUZZER 15             // Buzzer
#define SSONIDO 35            // Sensor de sonido
#define LIGHT_SENSOR 12       // Sensor de luz
#define BUTTON 21             // Botón inicio pomodoro
#define DHTPIN 3              // Pin del sensor DHT
#define DHTTYPE DHT11         // Tipo de sensor DHT
#define TRIG_ULTRASONIC 25    // Pin TRIG para sensor ultrasónico
#define ECHO_ULTRASONIC 26    // Pin ECHO para sensor ultrasónico

DHT dht(DHTPIN, DHTTYPE);

#include "sala.h"
#include "cubiculo.h"

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  dht.begin();
  pinMode(RED_LED,OUTPUT);
  pinMode(GREEN_LED,OUTPUT);
  pinMode(BUZZER,OUTPUT);
  pinMode(BUTTON,INPUT);
  pinMode(SSONIDO, INPUT);
  pinMode(SLUZ, INPUT);
  pinMode(TRIG_ULTRASONIC, OUTPUT);
  pinMode(ECHO_ULTRASONIC, INPUT);
}

void read_light_sensor() {
  int light_value = analogRead(SLUZ);
  Serial.print("Valor del sensor de luz: ");
  Serial.println(light_value);
}

void read_sound_sensor() {
  int sound_value = analogRead(SSONIDO);
  Serial.print("Valor del sensor de sonido: ");
  Serial.println(sound_value);
}

void readDHT(){
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  if (isnan(h) || isnan(t)) {
  Serial.println("Failed to read from DHT sensor!");
  return;
  }
  Serial.print("Humedad: ");
  Serial.println(h);
  Serial.print("Temperatura: ");
  Serial.print(t);
  Serial.println("°C ");
}

void loop() {
    read_sound_sensor();
    read_light_sensor();
    readDHT();
    delay(1000);
}
