#include "../arduino.h"

#ifndef RFID_H
#define RFID_H

#include "../MFRC522v2/src/MFRC522v2.h"
#include <SPI.h>


class RFID { //Sensor de RFID
private:
    MFRC522 mfrc522;
public:
    RFID(int RST_PIN, int SS_PIN);
    int read();
};


#endif // RFID_H
