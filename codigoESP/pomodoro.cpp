#include "pomodoro.h"

Leds::Leds(int red, int green, int pomodoro[6], Timer &timer, Button &button, Buzzer& buzzer)
    : timer(timer), button(button), buzzer(buzzer) {
  led_red = red;
  led_green = green;
  for (int i = 0; i < 6; i++) {
    leds_pomodoro[i] = pomodoro[i];
  }
  ocupado = false;
  init();
}

void Leds::init() {
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
    buzzer.beep(50);
    delay(10);
    buzzer.beep(50);
    timer.start();

    for (int i = 0; i < 6; i++) {
        digitalWrite(leds_pomodoro[i], true);
    }
    Serial.println("POMODORO INICIADO");
}

void Leds::stop_pomodoro() {
    buzzer.beep(100);
    timer.reset();
    for (int i = 0; i < 6; i++) {
        digitalWrite(leds_pomodoro[i], false);
    }
    Serial.println("POMODORO FINALIZADO");
}

void Leds::update_pomodoro() {
    timer.update();
    if (timer.isTimerRunning()) {
        // Los leds se apagan progresivamente en función del tiempo transcurrido
        int leds = (1 - timer.getProgress()) * 5;
        Serial.println(timer.getProgress());
        for (int i = 0; i < 5; i++) {
        digitalWrite(leds_pomodoro[i], i <= leds);
        }

        // El led amarillo se enciende si se está trabajando, se apaga en el
        // descanso
        digitalWrite(leds_pomodoro[5], timer.isInWorkState());
    }

    if (button.read()) {
        Serial.println("BOTÓN PRESIONADO");
        if (timer.isTimerRunning()) {
        stop_pomodoro();
        } else {
        start_pomodoro();
        }
        delay(1000);
    }
}

Button::Button(int pin) : pin(pin) { pinMode(pin, INPUT_PULLDOWN); };

int Button::read() {
  int value = digitalRead(pin);
  return value;
}
