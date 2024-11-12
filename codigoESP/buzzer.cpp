#include "buzzer.h"

Buzzer::Buzzer(int buzzerPin) : buzzerPin(buzzerPin) {}

void Buzzer::beep(int duration) {
    digitalWrite(buzzerPin, HIGH);
    delay(duration);
    digitalWrite(buzzerPin, LOW);
}
