#include "sala.h"

Sala::Sala(const Sensor_DHT& s_dht) : s_dht(s_dht) {
    ocupada = false;
    reservada = false;
}
