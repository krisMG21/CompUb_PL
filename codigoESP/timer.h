#ifndef TIMER_H
#define TIMER_H

class Timer {
private:
    unsigned long totalTimeSeconds;
    unsigned long startTime;
    bool timerRunning;

public:
    Timer();
    void setTime(unsigned int minutes);
    unsigned long readTime();
    void start();
    void stop();
    void reset();
    bool isRunning() const;
    bool isFinished() const;
    unsigned int getMinutes() const;
    unsigned int getSeconds() const;
    unsigned int getProgress() const;
    void addTime(int minutes);
};

#endif //TIMER_H
