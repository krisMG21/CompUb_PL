 int led = 7; //PIN usado para lED
 int PIR = 8; //PIN usado para Sensor
 int valor; //Variable para el valor del Sensor.

void setup() {
 pinMode(led,OUTPUT); //Declaramos el LED de tipo salida
 pinMode(RIP,INPUT); //Declaramos al sensor de tipo entrada
 }

void loop() {
 valor = digitalRead(PIR); //Obtenemos el valor del sensor
 if(valor == HIGH){ // Comparamos el valor si es HIGH esta detectando un movimiento de lo  contrario enviaria un LOW (Tambien pueden comprarse con 0,1)
     digitalWrite(led,HIGH); // SI el valor es igual a HIGH, encendemos el LED
 }
else{
     digitalWrite(led,LOW); //Si el valor es diferente de HIGH apagamos el LED.
 }
 }