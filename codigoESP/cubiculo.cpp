#include "cubiculo.h"

Cubiculo::Cubiculo(
    const Leds& leds,
    const Sensor& s_luz,
    const Sensor& s_sonido,
    const Sensor_US& s_posicion,
    const Sensor_DHT& s_dht,
    const Button& button):

    leds(leds),
    s_luz(s_luz),
    s_sonido(s_sonido),
    s_posicion(s_posicion),
    s_dht(s_dht),
    button(button) {

    occupied = false;
}

/** Recorre todos los componentes del cubículo.
 *  Una vez una condición se cumple, ejecuta su código
 *  y sale de la función, por optimización.
*/
void Cubiculo::update() {
}
