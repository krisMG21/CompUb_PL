#include <WiFi.h>

// Configura tu red WiFi aquí
#define WIFI_SSID "iPhone de Martin "
#define WIFI_PASSWORD "contrasea"

void setup() {
  // Inicia la comunicación serie para ver el estado de la conexión
  Serial.begin(9600);
  
  // Intenta conectar a WiFi
  Serial.print("Conectando a WiFi...");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  // Espera hasta que se conecte o falle
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  // Confirma la conexión en el monitor serie
  Serial.println("\nConexión establecida.");
  Serial.print("Dirección IP: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  // Aquí puedes poner algún código adicional si quieres,
  // pero para probar WiFi solo necesitamos `setup()`
}
