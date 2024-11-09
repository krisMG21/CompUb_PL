#include "sala.h"

Sala::Sala(const Sensor_DHT& s_dht, const Servo& cerradura, const MQTT& mqtt):
    s_dht(s_dht),
    cerradura(cerradura),
    mqtt(mqtt) {
    ocupada = false;
    reservada = false;
}

void Sala::reservar(Usuario& new_usuario, int time) {
    ocupada = true;
    reservada = true;
    usuario = new_usuario;

    startTime = millis();
    reservationTime = time * 60 * 1000;
}



bool Sala::is_reservada() {
    reservada = (elapsedTime < reservationTime);
    return reservada;
}

void Sala::update() {
    float temp = s_dht.readTemperature();
    float hum = s_dht.readHumidity();
    mqtt.publish("sala/temp", String(temp));
    mqtt.publish("sala/hum", String(hum));
}
