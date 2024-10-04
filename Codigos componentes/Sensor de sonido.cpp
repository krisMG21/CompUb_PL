
#include <KY_37_SoundSensor.h>

KY_37_SoundSensor soundSensor(A0);

void setup() {
  Serial.begin(9600);
}

void loop() {
  int soundValue = soundSensor.read();
  Serial.println("Sound: " + String(soundValue));
  delay(1000);
}
