#include "rfid.h"


RFID::RFID(int RST_PIN, int SDA_PIN) :
    ss_pin(SDA_PIN),
    driver(ss_pin),
    rfid(driver) {

    // pinMode(RST_PIN, OUTPUT);
    // digitalWrite(RST_PIN, LOW);
    // delay(100);
    // digitalWrite(RST_PIN, HIGH);
    // delay(100);

    SPI.begin();
    rfid.PCD_Init();
}


void RFID::read() {
    Serial.println("Entered read()");

    if (!rfid.PICC_IsNewCardPresent() || !rfid.PICC_ReadCardSerial()){
        Serial.print("UID Leída: ");
            for (byte i = 0; i < 4; i++) {
                Serial.print(rfid.uid.uidByte[i]);
            }
            Serial.println("");
            return;
    }
    // unsigned long startTime = millis();
    // while (millis() - startTime < 100) { // Intenta durante 100ms máximo
    //     Serial.println("Trying to read card...");
    //     if (rfid.PICC_IsNewCardPresent()){
    //         Serial.println("Card detected");
    //
    //         if(rfid.PICC_ReadCardSerial()){
    //             Serial.print("UID Leída: ");
    //             for (byte i = 0; i < 4; i++) {
    //                 Serial.print(rfid.uid.uidByte[i]);
    //             }
    //             Serial.println("");
    //             return;
    //         }
    //         yield();
            // Serial.print("UID Leída: ");
            // for (byte i = 0; i < 4; i++) {
            //     Serial.print(rfid.uid.uidByte[i]);
            // }
            // Serial.println("");

            // unsigned long hex_num;
            // hex_num =  rfid.uid.uidByte[0] << 24;
            // hex_num += rfid.uid.uidByte[1] << 16;
            // hex_num += rfid.uid.uidByte[2] <<  8;
            // hex_num += rfid.uid.uidByte[3];
            // rfid.PICC_HaltA(); // Stop reading
            // Serial.println("Card read succesfully!");
            // Serial.print("Card UID: ");
            // Serial.println(hex_num, HEX);
            // return hex_num;        return;
        // }
        // yield(); // Permite que otras tareas se ejecuten
    // }
    //
    // Serial.println("No card detected within timeout");

}
