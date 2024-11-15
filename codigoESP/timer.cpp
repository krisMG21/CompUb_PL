#include "timer.h"

Timer::Timer(Buzzer& buzzer) :buzzer(buzzer), startTime(0), elapsedTime(0), currentState(IDLE), isRunning(false) {}

void Timer::start() {
    if (!isRunning) {
        startTime = millis() - elapsedTime;
        currentState = WORK;
        isRunning = true;
    }
}

void Timer::reset() {
    elapsedTime = 0;
    currentState = IDLE;
    isRunning = false;
}

void Timer::update() {
    if (isRunning) {
        unsigned long currentTime = millis();
        elapsedTime = currentTime - startTime;
        int elapsedSeconds = elapsedTime / 1000;

        if (currentState == WORK) {
            Serial.println("Pomodoro: TRABAJANDO");
            Serial.print("Tiempo transcurrido: ");
            Serial.println(elapsedSeconds);
            Serial.println("Tiempo total: ");
            Serial.println(WORK_TIME);
            Serial.println("Progreso: ");
            Serial.println(getProgress());
        } else if (currentState == BREAK) {
            Serial.println("Pomodoro: DESCANSANDO");
            Serial.print("Tiempo transcurrido: ");
            Serial.println(elapsedSeconds);
            Serial.println("Tiempo total: ");
            Serial.println(BREAK_TIME);
            Serial.println("Progreso: ");
            Serial.println(getProgress());
        }


        if (currentState == WORK && elapsedTime >= WORK_TIME) {
            currentState = BREAK;
            startTime = currentTime;
            elapsedTime = 0;
            buzzer.beep(50);
        } else if (currentState == BREAK && elapsedTime >= BREAK_TIME) {
            currentState = WORK;
            startTime = currentTime;
            elapsedTime = 0;
            buzzer.beep(50);
        }
    }
}

bool Timer::isInWorkState() const {
    return currentState == WORK;
}

bool Timer::isInBreakState() const {
    return currentState == BREAK;
}

float Timer::getProgress() const {
    if (!isRunning) return 0;
    float elapsedTime = this->elapsedTime;

    unsigned long totalTime = (currentState == WORK) ? WORK_TIME : BREAK_TIME;
    return elapsedTime / totalTime;
}

bool Timer::isTimerRunning() const {
    return isRunning;
}
