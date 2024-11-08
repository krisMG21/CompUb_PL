#include "sala.h"

Sala::Sala(struct leds leds) {
    occupied = false;
    timer = Timer();
    leds_i = leds;
}
