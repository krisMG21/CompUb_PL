#include "leds.h"

void Leds::init(){
    pinMode(led_red, OUTPUT);
    pinMode(led_green, OUTPUT);
    for (int i = 0; i < 6; i++) {
        pinMode(leds_pomodoro[i], OUTPUT);
    }
}

void Leds::set_ocupado(bool is_ocupado) {
    digitalWrite(led_red, is_ocupado);
    digitalWrite(led_green, !is_ocupado);
    ocupado = is_ocupado;
}

void Leds::change_ocupado() {
    ocupado = !ocupado;
    set_ocupado(ocupado);
}

void Leds::update() {
    int leds = timer.getProgress() * 5;
    for (int i = 0; i < 5; i++) {
        digitalWrite(leds_pomodoro[i], i <= leds);
    }
}
