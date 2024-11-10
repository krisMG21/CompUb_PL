#ifndef RFID_H
#define RFID_H

#include "SPI.h"
#include "../MFRC522v2/src/MFRC522v2.h"


class RFID { //Sensor de RFID
private:
    MFRC522 mfrc522;
    unsigned long user_id;
public:
    RFID(int RST_PIN, int SS_PIN);
    unsigned long read();
};


#endif // RFID_H
