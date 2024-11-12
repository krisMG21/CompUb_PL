#include "rfid.h"


RFID::RFID(int RST_PIN, int SS_PIN) :
    ss_pin(SS_PIN),
    driver(ss_pin),
    mfrc522(driver) {

    pinMode(RST_PIN, OUTPUT);
    digitalWrite(RST_PIN, LOW);
    delay(100);
    digitalWrite(RST_PIN, HIGH);
    delay(100);

    SPI.begin();
    mfrc522.PCD_Init();
}

unsigned long RFID::read() {
    if (! mfrc522.PICC_IsNewCardPresent()) {
        return 0;
    }

    if ( ! mfrc522.PICC_ReadCardSerial()) { //Since a PICC placed get Serial and continue
        return 0;
    }
    unsigned long hex_num;
    hex_num =  mfrc522.uid.uidByte[0] << 24;
    hex_num += mfrc522.uid.uidByte[1] << 16;
    hex_num += mfrc522.uid.uidByte[2] <<  8;
    hex_num += mfrc522.uid.uidByte[3];
    mfrc522.PICC_HaltA(); // Stop reading
    Serial.println("Card read succesfully!");
    Serial.print("Card UID: ");
    Serial.println(hex_num, HEX);
    return hex_num;
}
