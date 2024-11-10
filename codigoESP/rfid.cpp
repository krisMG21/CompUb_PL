#include "rfid.h"

RFID::RFID(int RST_PIN, int SS_PIN):
    mfrc522(SS_PIN, RST_PIN) {
    if (Serial){
        SPI.begin();
        mfrc522.PCD_Init();
        delay(4);
        mfrc522.PCD_DumpVersionToSerial();
        Serial.println("RFID module initialized");
        Serial.println("Scan PICC to see UID, SAK, type, and data blocks...");
    }
}

unsigned long RFID::read() {
    if ( ! mfrc522.PICC_ReadCardSerial()) { //Since a PICC placed get Serial and continue
        return 0;
    }
    unsigned long hex_num;
    hex_num =  mfrc522.uid.uidByte[0] << 24;
    hex_num += mfrc522.uid.uidByte[1] << 16;
    hex_num += mfrc522.uid.uidByte[2] <<  8;
    hex_num += mfrc522.uid.uidByte[3];
    mfrc522.PICC_HaltA(); // Stop reading
    return hex_num;
}

