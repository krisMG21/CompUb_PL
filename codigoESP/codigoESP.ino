#define RED_LED 2  // Led de ocupación del cubículo: RED
#define GREEN_LED 0      // Led de ocupación del cubículo: GREEN
#define BUZZER 15

#define BUTTON 21

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(RED_LED,OUTPUT);
  pinMode(GREEN_LED,OUTPUT);
  pinMode(BUZZER,OUTPUT);
  
  pinMode(BUTTON,INPUT);
}


void loop() {
  if(digitalRead(BUTTON)){
    digitalWrite(RED_LED, HIGH);
    digitalWrite(GREEN_LED, LOW);
  } else {
    digitalWrite(RED_LED, LOW);
    digitalWrite(GREEN_LED, HIGH);
  }
  delay(50);

}