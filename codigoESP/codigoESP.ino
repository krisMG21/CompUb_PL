#include <DHT.h>
#define RED_LED 2  // Led de ocupación del cubículo: RED
#define GREEN_LED 0      // Led de ocupación del cubículo: GREEN
#define BUZZER 15
#define SSONIDO 35
#define LIGHT_SENSOR 12
#define BUTTON 21
#define DHTPIN 3
#define DHTTYPE DHT11 
DHT dht(DHTPIN, DHTTYPE);
#include <cstdio>

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  dht.begin();
  pinMode(RED_LED,OUTPUT);
  pinMode(GREEN_LED,OUTPUT);
  pinMode(BUZZER,OUTPUT);
  pinMode(LIGHT_SENSOR, INPUT);  
  pinMode(BUTTON,INPUT);
  pinMode(SSONIDO, INPUT);
}

void alternating_leds(){
  digitalWrite(RED_LED, HIGH);  // Turn on the LED
  digitalWrite(GREEN_LED, LOW);
  delay(1000);                  // Wait for 1 second
  digitalWrite(RED_LED, LOW);   // Turn off the LED
  digitalWrite(GREEN_LED, HIGH);
  delay(1000);                  // Wait for 1 second
}

void read_light_sensor() {
  int light_value = analogRead(LIGHT_SENSOR);   
  Serial.print("Valor del sensor de luz: ");      
  Serial.println(light_value);                 
  delay(5000); 
}

void read_sound_sensor() {
  int sound_value = analogRead(SSONIDO);  
  Serial.print("Valor del sensor de sonido: ");       
  Serial.println(sound_value);                  
  delay(5000); 
}

void readDHT(){
  // Esperamos 5 segundos entre medidas
  delay(5000);
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  if (isnan(h) || isnan(t)) {
  Serial.println("Failed to read from DHT sensor!");
  return;
  }
  Serial.print("Humedad: ");
  Serial.println(h);
  Serial.print("Temperatura: ");
  Serial.print(t);
  Serial.println("°C ");
}

void loop() {
    read_sound_sensor();            
    read_light_sensor();   
    readDHT();
    
}