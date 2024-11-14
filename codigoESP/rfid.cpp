#include "rfid.h"


RFID::RFID(int RST_PIN, int SDA_PIN) :
    ss_pin(SDA_PIN),
    driver(ss_pin),
    rfid(driver) {

    pinMode(RST_PIN, OUTPUT);
    digitalWrite(RST_PIN, LOW);
    delay(100);
    digitalWrite(RST_PIN, HIGH);
    delay(100);

    SPI.begin();
    rfid.PCD_Init();
}


void RFID::read() {
    Serial.println("Entered read()");

    if (rfid.PICC_IsNewCardPresent() && rfid.PICC_ReadCardSerial()){
        Serial.print("UID Leída: ");
        for (byte i = 0; i < rfid.uid.size; i++) {
            Serial.print(rfid.uid.uidByte[i]);
        }
        Serial.println("");
    }
}
