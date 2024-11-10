#include "sala.h"

Sala::Sala(const unsigned ID, const Sensor_DHT& s_dht, const Servo& cerradura, const int RFID_RST, const int RFID_SS, const MQTT& mqtt):
    ID(ID),
    s_dht(s_dht),
    cerradura(cerradura),
    escaner(RFID_RST, RFID_SS),
    mqtt(mqtt) {

    // MQTT.subscribe("sala/reserva");
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

bool Sala::is_reservada() {
    elapsedTime = millis() - startTime;
    return (elapsedTime < reservedTime);
}

void Sala::update() {
    std::string topic = "sala/"+ (std::to_string(ID));
    switch (state) {
        case 0:{ //Lectura de temperatura
            float temp = s_dht.readTemperature();
            mqtt.publish(topic+"/temp", String(temp));
            Serial.println("PUBLISHED: "+topic+"/temp:" + String(temp));
            state++;
            break;
            }
        case 1:{ //Lectura de humedad
            float hum = s_dht.readHumidity();
            mqtt.publish(topic+"/hum", String(hum));
            Serial.println("PUBLISHED: "+topic+"/hum:" + String(hum));
            state++;
            break;
            }
        case 2: { //Comprobar si sigue reservada
            mqtt.publish(topic+"/reservado", String(is_reservada()));
            Serial.println("PUBLISHED: "+topic+"/reservado:" + String(is_reservada()));
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
            mqtt.publish("sala/uid", String(lectura));
        }
        state++;
        break;

    }
}
