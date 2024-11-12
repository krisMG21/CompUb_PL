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

    // mqtt.subscribe("sala/reserva", [this](const std::string& message) {
    //     std::string value = message;
    //     if (value == "true") {
    //         reservar(message.toInt(), message.toInt());
    //     } else {
    //         cancelarReserva();
    //     }
    // });
}



void Sala::reservar(unsigned long userID, int time) {
    if (!is_reservada()) {
        userID = userID;
        reservada = true;
        startTime = millis();
        reservedTime = time * 60 * 1000;
    }
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

void Sala::cancelarReserva() {
    reservada = false;
    startTime = 0;
    reservedTime = 0;
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
            state++;
            break;
        }
        case 3: { // Subir el valor del rfid si hay uno nuevo
            unsigned long value = escaner.read();
            if (value) {
                userID = value;
                last_reading = millis();
                mqtt.publish(topic+"uid", std::to_string(value));
                mqtt.publish(topic+"last_reading", std::to_string(last_reading));
            }
            state++;
            break;
        }
        case 4:{
            
        }

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
