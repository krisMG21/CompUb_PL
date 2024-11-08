#include <ESP32Servo.h>

Servo myservo;
int servoPin = 13;

void setup() {
  Serial.begin(9600);                   // Inicia la comunicación serial
  ESP32PWM::allocateTimer(0);           // Asigna un temporizador para el PWM del servo
  myservo.setPeriodHertz(50);           // Configura la frecuencia del servo a 50 Hz
  myservo.attach(servoPin, 1000, 2000); // Asocia el servo al pin 13 con un rango de pulso
}

void loop() {
  // Abrir la puerta (mover el servo a 90 grados)
  myservo.write(180);                    // Mueve el servo a 90 grados
  delay(3000);                          // Espera 3 segundos con la puerta abierta
  Serial.println("PUERTA ABIERTA!!!");  // Imprime el mensaje en la consola serial
  
  // Cerrar la puerta (mover el servo a 0 grados)
  myservo.write(0);                     // Mueve el servo a 0 grados (cerrar la puerta)
  delay(3000);                          // Espera 3 segundos con la puerta cerrada
  Serial.println("PUERTA CERRADA!!!");  // Imprime el mensaje en la consola serial
}
