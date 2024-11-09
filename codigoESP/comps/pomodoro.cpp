#include "pomodoro.h"

Leds::Leds(int red, int green, int pomodoro[6], Timer& timer) : timer(timer) {
    led_red = red;
    led_green = green;
    for (int i = 0; i < 6; i++) {
        leds_pomodoro[i] = pomodoro[i];
    }
    ocupado = false;
    init();
}

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

bool Button::isPressed(){
    int value = digitalRead(pin);
    return (bool)value;
}

int Button::read(){
    int value = digitalRead(pin);
    return value;
}
