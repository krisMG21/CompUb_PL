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
    #define millis() (0)
    typedef unsigned char uint8_t;
    typedef unsigned int uint16_t;
    typedef int int16_t;
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

void Timer::addTime(int minutes) {
    if (minutes > 0) {
        totalTimeSeconds += minutes * 60UL;
    } else if (minutes < 0 && static_cast<unsigned long>(-minutes * 60) < totalTimeSeconds) {
        totalTimeSeconds -= static_cast<unsigned long>(-minutes * 60);
    }
}

