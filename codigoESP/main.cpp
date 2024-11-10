#include "timer.h"
#include <unistd.h>
#include <cstdio>

#define DHTPIN 3              // Pin del sensor DHT
#define DHTTYPE DHT11         // Tipo de sensor DHT

int main() {
    Timer timer;
    timer.start();
    for(int i = 0; i < 10; i++) {
        printf("timer.isInWorkState(): %d\n", timer.isInWorkState());
        printf("timer.isInBreakState(): %d\n", timer.isInBreakState());
        printf("timer.getProgress(): %d\n", timer.getProgress());
        printf("timer.isTimerRunning(): %d\n", timer.isTimerRunning());
        sleep(1);
    }
    timer.reset();
    printf("timer.getProgress(): %d\n", timer.getProgress());
    return 0;
}
