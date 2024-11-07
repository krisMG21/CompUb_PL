#ifdef ARDUINO
#include <Arduino.h>
#else
    #define HIGH 1
    #define LOW 0
    #define INPUT 0
    #define OUTPUT 1
    #define digitalWrite(pin, val) ((void)0)
    #define digitalRead(pin) (0)
    #define pinMode(pin, mode) ((void)0)
    #define delay(ms) ((void)0)
    typedef unsigned char uint8_t;
    typedef unsigned int uint16_t;
    typedef int int16_t;
#endif

#include "leds.h"
#include "timer.h"

struct leds leds;
Timer timer;

bool occupied = false;

void leds_init(int red, int green, int pomodoro[6]) {
    leds.red = red;
    leds.green = green;
    for (int i = 0; i < 6; i++) {
        leds.pomodoro[i] = pomodoro[i];
    }
}

void set_occupied(bool occupied) {
    occupied = occupied;
}

void change_occupied() {
    occupied = !occupied;
}

void update_leds() {
    digitalWrite(leds.red, occupied);
    digitalWrite(leds.green, !occupied);

    int leds = timer.getProgress() * 5;
    for (int i = 0; i < 5; i++) {
        digitalWrite(leds.pomodoro[i], i <= leds);
    }
}

