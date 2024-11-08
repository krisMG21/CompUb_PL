#include "cubiculo.h"

/** Recorre todos los componentes del cubículo.
 *  Una vez una condición se cumple, ejecuta su código
 *  y sale de la función, por optimización.
*/
void Cubiculo::update() {
    // Boton presionado, empieza pomodoro
    if (button.isPressed()) {
        if(timer.isTimerRunning()) {
            timer.reset();
        }
        else {
            timer.start();
        }
    }
    // Sensor de posicion, cubiculo ocupado
    else if(s_posicion.ocupado()){
        leds.set_ocupado(true);
    }
    leds.update();
}
