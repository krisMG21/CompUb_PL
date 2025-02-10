#include <cstdio>
#include <DHT.h>
#include <WiFi.h>
#include <WiFiClient.h>
#include <PubSubClient.h>
#include <ESP32Servo.h>

#include <MFRC522v2.h>
#include <MFRC522DriverPinSimple.h>
#include <MFRC522DriverSPI.h>
#include <MFRC522Debug.h>

#include "buzzer.h"
#include "timer.h"

#include "config.h"  //Credenciales de WiFi y MQTT

// INFO: MODO DE EJECUCIÓN
// ==================================================
typedef enum { SALA,
               CUBICULO } tipo;
#define MODE SALA
//#define MODE CUBICULO


// INFO: IDENTIFICADOR DE CIRCUITO
// ==================================================
#define ID 2


// INFO: PINES
// ==================================================
#define RED_LED 17    // LED de ocupación del cubículo: ROJO
#define GREEN_LED 21  // LED de ocupación del cubículo: VERDE

// INFO: LEDs del pomodoro
#define P_LED1 22
#define P_LED2 16
#define P_LED3 27
#define P_LED4 14
#define P_LED5 2
#define P_LED_AMARILLO 15

#define BUZZER 32  // Buzzer
#define SERVO 13
#define SSONIDO 35  // Sensor de sonido
#define SLUZ 34     // Sensor de luz
#define BUTTON 33   // Botón inicio pomodoro

#define DHTPIN 3       // Pin del sensor DHT
#define DHTTYPE DHT11  // Tipo de sensor DHT
DHT dht(DHTPIN, DHTTYPE);

#define TRIG_ULTRASONIC 25  // Pin TRIG para sensor ultrasónico
#define ECHO_ULTRASONIC 26  // Pin ECHO para sensor ultrasónico
#define SERVO_PIN 13

// INFO: PINES RFID
#define RFID_RST 4
#define RFID_SDA 5


// Constructor RFID
MFRC522DriverPinSimple ss_pin(RFID_SDA);
MFRC522DriverSPI driver(ss_pin);
MFRC522 mfrc522(driver);

// Objetos WiFi, MQTT y DHT
WiFiClient espClient;
PubSubClient client(espClient);

// Componentes
Buzzer buzzer(BUZZER);
Timer timer(buzzer);
Servo servo;

// Variables cubiculo
long startTime = 0;
long elapsedTime = 0;

int leds_pomodoro[] = { P_LED1, P_LED2, P_LED3, P_LED4, P_LED5, P_LED_AMARILLO };

bool ocupado = false;

// Variables sala
bool sala_reservada = false;
bool sala_abierta = false;
unsigned long UID_Valida = 1;  //Not set yet

// Contador de pulsaciones del botón
int pulsaciones = 0;


void setup() {
  Serial.begin(115200);
  delay(1000);
  Serial.println("Serial connection initialized");  // Iniciar conexión serial

  // Iniciar pines duh
  initPines();

  // Iniciar MFRC522
  mfrc522.PCD_Init();
  dht.begin();

  // Iniciar conexiones
  initWifi();
  initMQTTServer();

  std::string topic;
  if (MODE == SALA) {
    topic = "sala/";
  } else if (MODE == CUBICULO) {
    topic = "cubiculo/";
  }
  topic += std::to_string(ID) + "/#";
  client.subscribe(topic.c_str());  // [espacio]/[ID]/#
  client.setCallback(callback);

  // Iniciar servo
  ESP32PWM::allocateTimer(0);  // Asigna un temporizador para el PWM del servo
  servo.setPeriodHertz(50);    // Configura la frecuencia del servo a 50 Hz
  servo.attach(SERVO_PIN, 1000, 2000);
}

void loop() {
  elapsedTime = millis() - startTime;
  if (MODE == SALA) {
    std::string topic = "sala/";
    topic += std::to_string(ID);
    topic += "/";

    if (elapsedTime >= 6000) {  // Los datos se publican cada 60 segundos
      elapsedTime = 0;
      startTime = millis();

      // Sensor DHT
      int temp = dht.readTemperature();
      publish(topic + "temp", std::to_string(temp));
      int hum = dht.readHumidity();
      publish(topic + "hum", std::to_string(hum));
    }

    unsigned long UID = leerTarjeta();
    Serial.print("UID Leída:");
    Serial.println(UID, HEX);

    if (UID == UID_Valida) {
      Serial.println("RFID VÁLIDA");
      if (sala_abierta) {
        servo.write(0);
        sala_abierta = false;
        publish(topic + "ocupado", std::to_string(false));
      } else {
        servo.write(180);
        sala_abierta = true;
        publish(topic + "ocupado", std::to_string(true));
      }
    }

  } else if (MODE == CUBICULO) {
    std::string topic = "cubiculo/";
    topic += std::to_string(ID);
    topic += "/";
    if (elapsedTime >= 6000) {  // Los datos se publican cada 60 segundos
      elapsedTime = 0;
      startTime = millis();

      // Sensor de luz
      int luz = analogRead(SLUZ);
      publish(topic + "luz", std::to_string(luz));

      // Sensor de sonido
      int sonido = analogRead(SSONIDO);
      publish(topic + "sonido", std::to_string(sonido));

      delay(100);

      // Sensor DHT
      int temp = dht.readTemperature();
      publish(topic + "temp", std::to_string(temp));
      int hum = dht.readHumidity();
      publish(topic + "hum", std::to_string(hum));
    }

    // Sensor ultrasonido
    float distancia = get_distance();
    //publish(topic+"distancia", std::to_string(distancia));
    bool ocupado_temp = distancia < 15;
    if (ocupado_temp != ocupado) {
      ocupado = ocupado_temp;
      publish(topic + "ocupado", std::to_string(ocupado));
      if (ocupado) {
        digitalWrite(RED_LED, HIGH);
        digitalWrite(GREEN_LED, LOW);
      } else {
        digitalWrite(RED_LED, LOW);
        digitalWrite(GREEN_LED, HIGH);
      }
    }
    Serial.print("Button digital read:");
    Serial.println(digitalRead(BUTTON));
    if (digitalRead(BUTTON) == HIGH) {
      Serial.println("BOTÓN PRESIONADO");
      pulsaciones++;
      publish(topic + "pulsaciones", std::to_string(pulsaciones));
      if (timer.isTimerRunning()) {
        timer.reset();
        stop_pomodoro();
      } else {
        timer.start();
        start_pomodoro();
      }
      delay(1000);
    }
    update_pomodoro();
    delay(500);
  }
}
// Conexión a WiFi
void initWifi() {
  WiFi.begin(WIFI_SSID, WIFI_PASS);  // Conectar a la red WiFi
  Serial.print("Conectando a WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConectado a WiFi");
}

void initMQTTServer() {
  client.setServer(MQTT_SERVER, MQTT_PORT);
  reconnectMQTT();
}

void reconnectMQTT() {
  while (!client.connected()) {
    Serial.print("Intentando conexión MQTT...");
    // Intentar conexión con credenciales
    if (client.connect("ESP32Client", MQTT_USER, MQTT_PASS)) {
      Serial.println("Conectado a MQTT");
    } else {
      Serial.print("Fallo, rc=");
      Serial.print(client.state());
      Serial.println(" Reintentando en 2 segundos...");
      delay(2000);
    }
  }
}

void publish(const std::string topic, const std::string message) {
  Serial.print(topic.c_str());
  Serial.print("/");
  Serial.println(message.c_str());
  client.publish(topic.c_str(), message.c_str());
}

/**
void subscribe(const std::string topic) {
    Serial.print("Subscribiendo a ");
    Serial.println(topic.c_str());
    client.subscribe(topic.c_str(), callback);
}*/

void callback(char* topic, byte* payload, unsigned int length) {
  std::string payloadStr(reinterpret_cast<char*>(payload), length);
  String mytopic(topic);

  if (mytopic.endsWith("uid")) {
    UID_Valida = std::stoul(payloadStr);
  } else if (mytopic.endsWith("reservada")) {
    sala_reservada = std::stoi(payloadStr);
  }
}

void initPines() {
  Serial.println("Iniciando pines...");
  Serial.println("Leds ocupación");
  pinMode(RED_LED, OUTPUT);
  pinMode(GREEN_LED, OUTPUT);

  Serial.println("Leds de pomodoro");
  pinMode(P_LED1, OUTPUT);
  pinMode(P_LED2, OUTPUT);
  pinMode(P_LED3, OUTPUT);
  pinMode(P_LED4, OUTPUT);
  pinMode(P_LED5, OUTPUT);
  pinMode(P_LED_AMARILLO, OUTPUT);

  Serial.println("Demás componentes");
  pinMode(SERVO, OUTPUT);
  pinMode(SSONIDO, INPUT);
  pinMode(SLUZ, INPUT);
  pinMode(BUTTON, INPUT_PULLDOWN);

  pinMode(DHTPIN, INPUT);
  pinMode(TRIG_ULTRASONIC, OUTPUT);

  pinMode(ECHO_ULTRASONIC, INPUT);
}

float get_distance() {
  // Clears the trigPin
  digitalWrite(TRIG_ULTRASONIC, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(TRIG_ULTRASONIC, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_ULTRASONIC, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  long duration = pulseIn(ECHO_ULTRASONIC, HIGH);
  // Calculating the distance
  float distance = (duration * 0.0343) / 2;
  return distance;
}

void start_pomodoro() {
  Serial.println("START_POMODORO");
  buzzer.beep(50);
  delay(10);
  buzzer.beep(50);
  timer.start();

  for (int i = 0; i < 6; i++) {
    digitalWrite(leds_pomodoro[i], HIGH);
  }
  Serial.println("POMODORO INICIADO");
}

void stop_pomodoro() {
  Serial.println("STOP_POMODORO");
  buzzer.beep(100);
  timer.reset();
  for (int i = 0; i < 6; i++) {
    digitalWrite(leds_pomodoro[i], LOW);
  }
  Serial.println("POMODORO FINALIZADO");
}

void update_pomodoro() {
  Serial.println("UPDATE_POMODORO");
  timer.update();
  Serial.println("timer");
  if (timer.isTimerRunning()) {
    // Los leds se apagan progresivamente en función del tiempo transcurrido
    int leds = (1 - timer.getProgress()) * 5;
    Serial.println(timer.getProgress());
    for (int i = 0; i < 5; i++) {
      digitalWrite(leds_pomodoro[i], i <= leds);
    }

    // El led amarillo se enciende si se está trabajando, se apaga en el
    // descanso
    digitalWrite(leds_pomodoro[5], timer.isInWorkState());
  }
}

long leerTarjeta() {
  unsigned long hex_num;
  if (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()) {
    Serial.print("UID detectado: ");
    for (byte i = 0; i < mfrc522.uid.size; i++) {
      Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
      Serial.print(mfrc522.uid.uidByte[i], HEX);
    }
    Serial.println();

    hex_num = mfrc522.uid.uidByte[0] << 24;
    hex_num += mfrc522.uid.uidByte[1] << 16;
    hex_num += mfrc522.uid.uidByte[2] << 8;
    hex_num += mfrc522.uid.uidByte[3];
    mfrc522.PICC_HaltA();
  } else {
    Serial.println("No hay tarjeta");
    hex_num = 0;
  }
  return hex_num;
}
