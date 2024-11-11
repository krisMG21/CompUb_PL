#ifndef RFID_H
#define RFID_H

#include <Arduino.h>
#include "SPI.h"
#include <MFRC522DriverPinSimple.h>
#include <MFRC522DriverSPI.h>
#include <MFRC522v2.h>
#include <require_cpp11.h>


class RFID { //Sensor de RFID
private:
    MFRC522DriverPinSimple ss_pin;
    MFRC522DriverSPI driver;
    MFRC522 mfrc522;

public:
    RFID(int RST_PIN, int SS_PIN);
    unsigned long read();
};


#endif // RFID_H
