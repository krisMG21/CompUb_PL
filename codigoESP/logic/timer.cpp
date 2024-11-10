#include "timer.h"

Timer::Timer() : startTime(0), elapsedTime(0), currentState(IDLE), isRunning(false) {}

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

// void Timer::pause() {
//     if (isRunning) {
//         elapsedTime = millis() - startTime;
//         isRunning = false;
//     }
// }


// void Timer::toggle() {
//     if (isRunning) {
//         pause();
//     } else {
//         start();
//     }
// }

void Timer::update() {
    if (isRunning) {
        unsigned long currentTime = millis();
        elapsedTime = currentTime - startTime;

        if (currentState == WORK && elapsedTime >= WORK_TIME) {
            currentState = BREAK;
            startTime = currentTime;
            elapsedTime = 0;
        } else if (currentState == BREAK && elapsedTime >= BREAK_TIME) {
            currentState = WORK;
            startTime = currentTime;
            elapsedTime = 0;
        }
    }
}

bool Timer::isInWorkState() const {
    return currentState == WORK;
}

bool Timer::isInBreakState() const {
    return currentState == BREAK;
}

int Timer::getProgress() const {
    if (!isRunning) return 0;

    unsigned long totalTime = (currentState == WORK) ? WORK_TIME : BREAK_TIME;
    return elapsedTime / totalTime;
}

bool Timer::isTimerRunning() const {
    return isRunning;
}
