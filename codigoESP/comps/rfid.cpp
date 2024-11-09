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
