#include "cubiculo.h"

Cubiculo::Cubiculo(
    const Leds& leds,
    const Sensor& s_luz,
    const Sensor& s_sonido,
    const Sensor_US& s_posicion,
    const Sensor_DHT& s_dht,
    const Button& button,
    const MQTT& mqtt):

    leds(leds),
    s_luz(s_luz),
    s_sonido(s_sonido),
    s_posicion(s_posicion),
    s_dht(s_dht),
    button(button),
    mqtt(mqtt) {
    state = 0;
    occupied = false;
}

/** Recorre todos los componentes del cubículo.
 *  Una vez una condición se cumple, ejecuta su código
 *  y sale de la función, por optimización.
*/
void Cubiculo::update() {
    switch (state) {
        case 0:
            leds.update();
            state++;
            break;
        case 1:
            int light_value = s_luz.read();
            mqtt.tryPublish("light", String(light_value));
            state++;
            break;
        case 2:
            int sound_value = s_sonido.read();
            mqtt.tryPublish("sound", String(sound_value));
            state++;
            break;
        case 3:
            s_posicion.update();
            state++;
            break;
        case 4:
            s_dht.update();
            state++;
            break;
        default:
            state = 0;
            break;
    }
}
