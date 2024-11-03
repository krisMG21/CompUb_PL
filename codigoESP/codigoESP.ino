#include <DHT.h>
#define RED_LED 2  // Led de ocupación del cubículo: RED
#define GREEN_LED 0      // Led de ocupación del cubículo: GREEN
#define BUZZER 15
#define SSONIDO 35
#define LIGHT_SENSOR 12
#define BUTTON 21
#define DHTPIN 34
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
  // put your main code here, to run repeatedly:
  digitalWrite(RED_LED, HIGH);  // Turn on the LED
  digitalWrite(GREEN_LED, LOW);
  delay(1000);                  // Wait for 1 second
  digitalWrite(RED_LED, LOW);   // Turn off the LED
  digitalWrite(GREEN_LED, HIGH);
  delay(1000);                  // Wait for 1 second
}

void change_led_when_button(){
  if(digitalRead(BUTTON)){
    digitalWrite(RED_LED, HIGH);
    digitalWrite(GREEN_LED, LOW);
  } else {
    digitalWrite(RED_LED, LOW);
    digitalWrite(GREEN_LED, HIGH);
  }
}

void buzz_when_button() {
  if (digitalRead(BUTTON)) {
    tone(BUZZER, 100); // Inicia el sonido a 100 Hz
  } else {
    noTone(BUZZER);    // Detiene el sonido cuando el botón no está presionado
  }
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
 
  // Leemos la humedad relativa
  float h = dht.readHumidity();
  // Leemos la temperatura en grados centígrados (por defecto)
  float t = dht.readTemperature();
  // Leemos la temperatura en grados Fahrenheit
  float f = dht.readTemperature(true);
 
  // Comprobamos si ha habido algún error en la lectura
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println("Error obteniendo los datos del sensor DHT11");
    /*Serial.println("Sensor h",isnan(h));
    Serial.println("Sensor h",isnan(t));
    Serial.println("Sensor h",isnan(f));*/
    return;
  }
 
  // Calcular el índice de calor en Fahrenheit
  float hif = dht.computeHeatIndex(f, h);
  // Calcular el índice de calor en grados centígrados
  float hic = dht.computeHeatIndex(t, h, false);
 
  Serial.print("Humedad: ");
  Serial.print(h);
  Serial.print(" %\t");
  Serial.print("Temperatura: ");
  Serial.print(t);
  Serial.print(" *C ");
  Serial.print(f);
  Serial.print(" *F\t");
  Serial.print("Índice de calor: ");
  Serial.print(hic);
  Serial.print(" *C ");
  Serial.print(hif);
  Serial.println(" *F");
}

void loop() {
    read_sound_sensor();            
    read_light_sensor();   
    readDHT();
    digitalWrite(GREEN_LED, HIGH); 
    buzz_when_button();
}