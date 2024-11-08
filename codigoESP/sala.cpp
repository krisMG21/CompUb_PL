#include "sala.h"

void Sala::update() {
    if (button.isPressed()) {
        leds.change_occupied();
        leds.start_pomodoro(timer.getProgress());
    }
    else {
        leds.set_occupied(occupied);
    }
    leds.update();
}
