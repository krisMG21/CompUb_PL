const byte pinBuzzer = 5;
byte salida = 10;

void setup() {
}

void loop() {
  analogWrite(pinBuzzer, salida);
}