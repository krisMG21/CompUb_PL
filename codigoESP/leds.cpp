#include "leds.h"

void Leds::init(){
    pinMode(led_red, OUTPUT);
    pinMode(led_green, OUTPUT);
    for (int i = 0; i < 6; i++) {
        pinMode(leds_pomodoro[i], OUTPUT);
    }
}

void Leds::set_occupied(bool occupied) {
    digitalWrite(led_red, occupied);
    digitalWrite(led_green, !occupied);
}

void Leds::change_occupied() {
    occupied = !occupied;
    set_occupied(occupied);
}

void Leds::start_pomodoro() {
    timer.start();
}

void Leds::update() {
    int leds = timer.getProgress() * 5;
    for (int i = 0; i < 5; i++) {
        digitalWrite(leds_pomodoro[i], i <= leds);
    }
}
