#ifndef RFID_H
#define RFID_H

#include <Arduino.h>
#include "SPI.h"
#include <MFRC522v2.h>
#include <MFRC522DriverPinSimple.h>
#include <MFRC522DriverSPI.h>
#include <MFRC522Debug.h>


class RFID { //Sensor de RFID
private:
    MFRC522DriverPinSimple ss_pin;
    MFRC522DriverSPI driver;
    MFRC522 rfid;

public:
    RFID(int RST_PIN, int SDA_PIN);
    void read();
    void test();
};


#endif // RFID_H
