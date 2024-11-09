#include "sala.h"

#define DHTPIN 3              // Pin del sensor DHT
#define DHTTYPE DHT11         // Tipo de sensor DHT

int main() {
    Sensor_DHT s_dht(DHTPIN);
    Sala sala(s_dht);
    while (true) {
        sala.update();
    }
}
