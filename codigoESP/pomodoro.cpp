#include "pomodoro.h"

Leds::Leds(int red, int green, int pomodoro[6], Timer& timer, Button& button) : timer(timer), button(button) {
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

void Leds::start_pomodoro() {
    timer.start();
}

void Leds::stop_pomodoro() {
    timer.reset();
    for (int i = 0; i < 6; i++) {
        digitalWrite(leds_pomodoro[i], false);
    }
}


void Leds::update() {
    timer.update();
    if (timer.isTimerRunning()) {
        // Los leds se encienden progresivamente en función del tiempo transcurrido
        if (timer.isInWorkState()) {
            int leds = timer.getProgress() * 5;
            for (int i = 0; i < 5; i++) {
                digitalWrite(leds_pomodoro[i], i <= leds);
            }

            // El led amarillo se enciende si se está trabajando, se apaga en el descanso
            digitalWrite(leds_pomodoro[5], timer.isInWorkState());

        } else {
            digitalWrite(led_red, false);
            digitalWrite(led_green, true);
        }
    }

    if (button.read()) {
        if (timer.isTimerRunning()) {
            stop_pomodoro();
        } else {
            start_pomodoro();
        }
    }
}

int Button::read(){
    int value = digitalRead(pin);
    return value;
}
