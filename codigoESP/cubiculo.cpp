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
    std::string topic = "cubiculo/";
    topic += std::to_string(ID);

    switch (state) {
        case 0:{ //Actualizar los leds del pomodoro
            leds.update();
            state++;
            break;
            }
        case 1:{ //Leer sensor de luz
            int light_value = s_luz.read();
            mqtt.publish(topic+"luz", std::to_string(light_value));
            state++;
            break;
            }
        case 2:{ //Leer sensor de sonido
            int sound_value = s_sonido.read();
            mqtt.publish(topic+"ruido", std::to_string(sound_value));
            state++;
            break;
            }
        case 3:{ //Leer sensor de ocupación
            bool ocupado = s_posicion.ocupado();
            leds.set_ocupado(ocupado);
            mqtt.publish(topic+"ocupado", std::to_string(ocupado));
            state++;
            break;
            }
        case 4:{ //Leer temperatura del sensor DHT
            float temp = s_dht.readTemperature();
            mqtt.publish(topic+"temp", std::to_string(temp));
            state++;
            break;
            }
        case 5:{ //Leer humedad del sensor DHT
            float hum = s_dht.readHumidity();
            mqtt.publish(topic+"hum", std::to_string(hum));
            state = 0;
            break;
            }
        default:{
            state = 0;
            break;
            }
    }
}
