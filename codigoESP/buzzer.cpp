#include "buzzer.h"

Buzzer::Buzzer(int pin) : pin(pin) { pinMode(pin, OUTPUT); };

void Buzzer::beep(int duration) {
  digitalWrite(pin, HIGH);
  delay(duration);
  digitalWrite(pin, LOW);
}
