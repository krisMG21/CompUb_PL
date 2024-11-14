#include "sala.h"
// #include "functions.h"

Sala::Sala(const unsigned ID, const Sensor_DHT& s_dht, const Cerradura& cerradura, const RFID& rfid, const MQTT& mqtt):
    ID(ID),
    s_dht(s_dht),
    cerradura(cerradura),
    escaner(rfid),
    mqtt(mqtt) {

    // MQTT.subscribe("sala/reserva");
    ocupada = false;
    reservada = false;

    startTime = 0;
    elapsedTime = 0;
    reservedTime = 0;

    std::string topic = "sala/";
    topic += std::to_string(ID);

    // mqtt.subscribe(topic+"user_reserva");
}

void Sala::setUserID(unsigned long userID) {
    this->userID = userID;
}

void Sala::setReservedTime(int time) {
    this->reservedTime = time * 60 * 1000;
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
    elapsedTime = 0;
    reservedTime = 0;
}

void Sala::update() {
    std::string topic = "sala/";
    topic += std::to_string(ID);

    //Lectura de temperatura
    float temp = s_dht.readTemperature();
    mqtt.publish(topic+"/temp", std::to_string(temp));

    //Lectura de humedad
    float hum = s_dht.readHumidity();
    mqtt.publish(topic+"/hum", std::to_string(hum));

    //Comprobar si sigue reservada
    reservada = is_reservada();
    mqtt.publish(topic+"/reserva", std::to_string(reservada));

    // Subir el valor del rfid si hay uno nuevo
    escaner.read();

    // if (value != 0) {
    //     userID = value;
    //     last_reading = millis();
    //     mqtt.publish(topic+"uid", std::to_string(value));
    //     mqtt.publish(topic+"last_reading", std::to_string(last_reading));
    // }

    // No borramos el valor cuando no haya lectura, sino que
    // lo preservamos y damos tiempo a la app para leerlo
    //
    // Puede borrarse una vez se procese allí, pero para
    // asegurar que se lee un valor reciente, podemos aportar
    // el tiempo de lectura.

}
