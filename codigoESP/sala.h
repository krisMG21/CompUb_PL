#ifndef SALA_H
#define SALA_H

#include "leds.h"
#include "timer.h"

class Sala {
private:
    bool occupied;
    Timer timer;
    struct leds leds_i;
    

public:
    Sala(struct leds leds);
};

#endif //SALA_H
