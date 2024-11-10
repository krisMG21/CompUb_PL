#include "sala.h"

Sala::Sala(const Sensor_DHT& s_dht, const Servo& cerradura, const int RFID_RST, const int RFID_SS, const MQTT& mqtt):
    s_dht(s_dht),
    cerradura(cerradura),
    escaner(RFID_RST, RFID_SS),
    mqtt(mqtt) {
    ocupada = false;
    reservada = false;
}

void Sala::reservar(Usuario& new_usuario, int time) {
    ocupada = true;
    reservada = true;
    usuario = new_usuario;

    startTime = millis();
    reservedTime = time * 60 * 1000;
}

void Sala::abrir() {
    ocupada = true;
    cerradura.write(90);
}

void Sala::cerrar() {
    ocupada = false;
    cerradura.write(0);
}

bool Sala::is_reservada() { //Devuelve si la sala está reservada
    return (elapsedTime < reservedTime);
}

void Sala::update() {
    switch (state) {
        case 0:{
            float temp = s_dht.readTemperature();
            mqtt.publish("sala/temp", String(temp));
            state++;
            break;
            }
        case 1:{
            float hum = s_dht.readHumidity();
            mqtt.publish("sala/hum", String(hum));
            state++;
            break;
            }
        case 2: {

        }
        // case 3:{
            // pregunta por sub datos de reserva a la app, pero tdvia no hay app, no se hace no?
        // }

        default:{
            state = 0;
            break;
            }
    }
}
