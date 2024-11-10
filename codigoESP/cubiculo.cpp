#include "cubiculo.h"

Cubiculo::Cubiculo(
    const unsigned ID,
    const Leds& leds,
    const Sensor& s_luz,
    const Sensor& s_sonido,
    const Sensor_US& s_posicion,
    const Sensor_DHT& s_dht,
    const Button& button,
    const MQTT& mqtt):

    ID(ID),
    leds(leds),
    s_luz(s_luz),
    s_sonido(s_sonido),
    s_posicion(s_posicion),
    s_dht(s_dht),
    button(button),
    mqtt(mqtt) {}

/** Publica cada vez llamada la información de uno de los sensores,
 * no haciéndolo todo de golpe para solaparse un poco con el resto de
 * funciones del sistema.
*/
void Cubiculo::update() {
    std::string topic = "cubiculo/"+ (std::to_string(ID));

    switch (state) {
        case 0:{
            leds.update();
            state++;
            break;
            }
        case 1:{
            int light_value = s_luz.read();
            mqtt.publish(topic+"/luz", String(light_value));
            state++;
            break;
            }
        case 2:{
            int sound_value = s_sonido.read();
            mqtt.publish(topic+"/ruido", String(sound_value));
            state++;
            break;
            }
        case 3:{
            bool ocupado = s_posicion.ocupado();
            leds.set_ocupado(ocupado);
            mqtt.publish(topic+"/ocupado", String(ocupado));
            state++;
            break;
            }
        case 4:{
            float temp = s_dht.readTemperature();
            float hum = s_dht.readHumidity();
            mqtt.publish(topic+"/temp", String(temp));
            mqtt.publish(topic+"/hum", String(hum));
            state++;
            break;
            }
        default:{
            state = 0;
            break;
            }
    }
}
