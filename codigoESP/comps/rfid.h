#include "../arduino.h"

#ifndef RFID_H
#define RFID_H

#ifdef ARDUINO
#include "SPI.h"
#include "../MFRC522v2/src/MFRC522v2.h"
#else
    class SPI{
        public:
            void begin();
            void transfer(int data);
            // int transfer(int data);
    };
    extern SPI SPI;
    class MFRC522{
        public:
            MFRC522(int SS_PIN, int RST_PIN);
            void PCD_Init();
            void PCD_DumpVersionToSerial();
            bool PICC_ReadCardSerial();
            void PICC_HaltA();
            Uid uid;
    };
#endif

class RFID { //Sensor de RFID
private:
    MFRC522 mfrc522;
    unsigned long user_id;
public:
    RFID(int RST_PIN, int SS_PIN);
    unsigned long read();
};


#endif // RFID_H
