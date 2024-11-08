#ifndef TIMER_H
#define TIMER_H

#include "arduino.h"

class Timer {
private:
    enum State { WORK, BREAK, IDLE };

    static const unsigned long WORK_TIME = 25 * 60 * 1000UL;  // 25 minutos en milisegundos
    static const unsigned long BREAK_TIME = 5 * 60 * 1000UL;  // 5 minutos en milisegundos

    unsigned long startTime;
    unsigned long elapsedTime;
    State currentState;
    bool isRunning;

public:
    Timer();
    void start();
    void pause();
    void reset();
    void toggle();
    void update();
    bool isInWorkState() const;
    bool isInBreakState() const;
    int getProgress() const;
    bool isTimerRunning() const;
};

#endif // TIMER_H
