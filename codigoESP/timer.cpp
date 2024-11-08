#ifdef ARDUINO
#include <Arduino.h>
#else
    #define millis() (0)
#endif

#include "timer.h"

Timer::Timer() : totalTimeSeconds(0), startTime(0), timerRunning(false) {}

void Timer::setTime(unsigned int minutes) {
    if (!timerRunning) {
        totalTimeSeconds = minutes * 60UL;
    }
}

unsigned long Timer::readTime() {
    if (timerRunning) {
        unsigned long elapsedTime = (millis() - startTime) / 1000UL;
        return (elapsedTime < totalTimeSeconds) ? (totalTimeSeconds - elapsedTime) : 0;
    }
    return totalTimeSeconds;
}

void Timer::start() {
    if (!timerRunning && totalTimeSeconds > 0) {
        timerRunning = true;
        startTime = millis();
    }
}

void Timer::stop() {
    if (timerRunning) {
        timerRunning = false;
        unsigned long elapsedTime = (millis() - startTime) / 1000UL;
        totalTimeSeconds = (elapsedTime < totalTimeSeconds) ? (totalTimeSeconds - elapsedTime) : 0;
    }
}

void Timer::reset() {
    stop();
    totalTimeSeconds = 0;
}

bool Timer::isRunning() const {
    return timerRunning;
}

bool Timer::isFinished() const {
    return timerRunning && (millis() - startTime) / 1000UL >= totalTimeSeconds;
}

unsigned int Timer::getMinutes() const {
    return totalTimeSeconds / 60;
}

unsigned int Timer::getSeconds() const {
    return totalTimeSeconds % 60;
}

unsigned int Timer::getProgress() const {
    return (totalTimeSeconds > 0) ? (totalTimeSeconds/ startTime) : 0;
}

void Timer::addTime(int minutes) {
    if (minutes > 0) {
        totalTimeSeconds += minutes * 60UL;
    } else if (minutes < 0 && static_cast<unsigned long>(-minutes * 60) < totalTimeSeconds) {
        totalTimeSeconds -= static_cast<unsigned long>(-minutes * 60);
    }
}

