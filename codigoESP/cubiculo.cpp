#include "cubiculo.h"

Cubiculo::Cubiculo(
    const unsigned ID,
    const Leds& leds,
    const Sensor& s_luz,
    const Sensor& s_sonido,
    const Sensor_US& s_posicion,
    const Sensor_DHT& s_dht,
    const MQTT& mqtt):

    ID(ID),
    leds(leds),
    s_luz(s_luz),
    s_sonido(s_sonido),
    s_posicion(s_posicion),
    s_dht(s_dht),
    mqtt(mqtt) {
    state = 0;
}

/** Publica cada vez llamada la información de uno de los sensores,
 * no haciéndolo todo de golpe para solaparse un poco con el resto de
 * funciones del sistema.
*/
void Cubiculo::update() {
    std::string topic = "cubiculo/";
    topic += std::to_string(ID);

    //Leer sensor de luz
    int light_value = s_luz.read();
    mqtt.publish(topic+"/luz", std::to_string(light_value));

    //Leer sensor de sonido
    int sound_value = s_sonido.read();
    mqtt.publish(topic+"/ruido", std::to_string(sound_value));

    //Leer sensor de ocupación
    bool ocupado = s_posicion.ocupado();
    leds.set_ocupado(ocupado);
    mqtt.publish(topic+"/distancia_temp", std::to_string(s_posicion.read()));
    mqtt.publish(topic+"/ocupado", std::to_string(ocupado));

    //Leer temperatura del sensor DHT
    float temp = s_dht.readTemperature();
    mqtt.publish(topic+"/temp", std::to_string(temp));

    //Leer humedad del sensor DHT
    float hum = s_dht.readHumidity();
    mqtt.publish(topic+"/hum", std::to_string(hum));

    //Actualizar los leds del pomodoro
    leds.update_pomodoro();
}
