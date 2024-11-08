#include <DHT.h>
#define RED_LED 2        // Led de ocupación del cubículo: RED
#define GREEN_LED 0      // Led de ocupación del cubículo: GREEN
#define BUZZER 39        // Buzzer
#define SSONIDO 35       // Sensor de sonido
#define LIGHT_SENSOR 12  // Sensor de luz
#define BUTTON 36        // Boton inicio pomodoro
#define DHTPIN 3         // Sensor DHT
#define DHTTYPE DHT11
#define TRIG_ULTRASONIC 25
#define ECHO_ULTRASONIC 26
long duration;
int distance;
DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  dht.begin();

  pinMode(RED_LED, OUTPUT);
  pinMode(GREEN_LED, OUTPUT);
  digitalWrite(GREEN_LED, LOW);

  pinMode(BUZZER, OUTPUT);
  pinMode(LIGHT_SENSOR, INPUT);
  pinMode(BUTTON, INPUT);
  pinMode(SSONIDO, INPUT);
  pinMode(TRIG_ULTRASONIC, OUTPUT); // Sets the trigPin as an Output
  pinMode(ECHO_ULTRASONIC, INPUT); // Sets the echoPin as an Input
}

void read_light_sensor() {
  int light_value = analogRead(LIGHT_SENSOR);
  Serial.print("Valor del sensor de luz: ");
  Serial.println(light_value);
}

void read_sound_sensor() {
  int sound_value = analogRead(SSONIDO);
  Serial.print("Valor del sensor de sonido: ");
  Serial.println(sound_value);
}

void readDHT(){
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  Serial.print("Humedad: ");
  Serial.print(h);
  Serial.println("%");
  Serial.print("Temperatura: ");
  Serial.print(t);
  Serial.println("°C ");
}

void readDistance(){
  // Clears the trigPin
  digitalWrite(TRIG_ULTRASONIC, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(TRIG_ULTRASONIC, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_ULTRASONIC, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(ECHO_ULTRASONIC, HIGH);
  // Calculating the distance
  distance = duration * 0.034 / 2;
  // Prints the distance on the Serial Monitor
  Serial.print("Distance: ");
  Serial.println(distance);
}
void loop() {
  /*read_sound_sensor();
  read_light_sensor();
  readDHT();
  readDistance();
  delay(3000);*/
  valorPulsador = digitalRead(BUTTON);  // Lectura digital de pulsadorPin
  if (valorPulsador == HIGH) {
      digitalWrite(BUZZER, HIGH); // Si detecta que pulsamos el pulsador imprime por el monitor serie "pulsado".
  }
  else {
     digitalWrite(BUZZER, LOW); 
  }
  delay(1000); 
}
