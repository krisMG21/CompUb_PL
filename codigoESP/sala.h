#ifndef SALA_H
#define SALA_H

typedef struct {
    int id;
} Usuario;

#include "comps/sensor.h"
#include "comps/servo.h"
#include "comps/rfid.h"
#include "logic/mqtt.h"

class Sala {
private:
    unsigned ID;
    int state = 0;

    Usuario usuario;
    bool ocupada;
    bool reservada;

    unsigned long startTime;
    unsigned long elapsedTime; //Tiempo transcurrido desde la reserva
    unsigned long reservedTime;

    Servo cerradura;
    RFID escaner;

    Sensor_DHT s_dht;

    MQTT mqtt;

public:
    Sala(const unsigned ID, const Sensor_DHT& s_dht, const Servo& cerradura, const int RFID_RST, const int RFID_SS, const MQTT& mqtt);

    void reservar(Usuario& usuario, int time);

    void abrir(); //Abre la sala con el servo
    void cerrar(); //Cierra la sala con el servo

    bool is_reservada(); //Compara tiempo transcurrido con el tiempo reservado
    bool is_ocupada(); //Cuando se abre, se marca como ocupada, cuando se cierra se interpreta como libre

    void update();


};
#endif //SALA_H
