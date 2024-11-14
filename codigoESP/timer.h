#ifndef TIMER_H
#define TIMER_H

#include <Arduino.h>
#include "buzzer.h"

class Timer {
private:
    enum State { WORK, BREAK, IDLE };

    static const unsigned long WORK_TIME = 1 * 60 * 1000UL;  // 25 minutos en milisegundos
    static const unsigned long BREAK_TIME = 1 * 10 * 1000UL;  // 5 minutos en milisegundos

    unsigned long startTime;
    unsigned long elapsedTime;
    State currentState;
    bool isRunning;

    Buzzer buzzer;

public:
    Timer(Buzzer& buzzer);
    void start();
    void reset();
    void update();
    bool isInWorkState() const;
    bool isInBreakState() const;
    bool isTimerRunning() const;
    float getProgress() const;
};

#endif // TIMER_H
