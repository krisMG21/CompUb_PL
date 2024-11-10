#include "sala.h"

Sala::Sala(const unsigned ID, const Sensor_DHT& s_dht, const Cerradura& cerradura, const RFID& rfid, const MQTT& mqtt):
    ID(ID),
    s_dht(s_dht),
    cerradura(cerradura),
    escaner(rfid),
    mqtt(mqtt) {

    // MQTT.subscribe("sala/reserva");
    ocupada = false;
    reservada = false;
}

void Sala::reservar(Usuario& new_usuario, int time) {
    ocupada = true;
    reservada = true;

    startTime = millis();
    reservedTime = time * 60 * 1000;
}

void Sala::abrir() {
    ocupada = true;
    cerradura.abrir();
}

void Sala::cerrar() {
    ocupada = false;
    cerradura.cerrar();
}

bool Sala::is_reservada() {
    elapsedTime = millis() - startTime;
    return (elapsedTime < reservedTime);
}

void Sala::update() {
    std::string topic = "sala/";
    topic += std::to_string(ID);
    switch (state) {
        case 0:{ //Lectura de temperatura
            float temp = s_dht.readTemperature();
            mqtt.publish(topic+"temp", std::to_string(temp));
            state++;
            break;
            }
        case 1:{ //Lectura de humedad
            float hum = s_dht.readHumidity();
            mqtt.publish(topic+"hum", std::to_string(hum));
            state++;
            break;
            }
        case 2: { //Comprobar si sigue reservada
            mqtt.publish(topic+"reserva", std::to_string(reservada));
            state = 0;
            break;
        }
        // case 3:{
            // LEER DATOS DE TOPIC DE RESERVA
            // Utilizar MQTT, debe haber hecho subscribe a los datos de reserva
        // }

        default:{
            state = 0;
            break;
            }

        // Leer RFID
        unsigned long lectura = escaner.read();
        // Contrastaremos la ID con la base de datos o lo que toque, de momento solo subimos la info
        if (escaner.read()) {
            mqtt.publish("sala/uid", std::to_string(lectura));
        }
        state++;
        break;

    }
}
