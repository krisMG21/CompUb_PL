#include "DHT.h"

#define DHTPIN 2 // Pin al que está conectado el DHT22
#define DHTTYPE DHT22 // Modelo del sensor DHT

DHT dht(DHTPIN, DHTTYPE);

void setup() {
Serial.begin(9600);
dht.begin();
}

void loop() {
float temperatura = dht.readTemperature();
float humedad = dht.readHumidity();

if (isnan(temperatura) || isnan(humedad)) {
Serial.println("Error al leer del sensor!");
return;
}

Serial.print("Temperatura: ");
Serial.print(temperatura);
Serial.println(" C");

Serial.print("Humedad: ");
Serial.print(humedad);
Serial.println(" %");

delay(2000);
}