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

    unsigned long startTime;
    unsigned long elapsedTime; //Tiempo transcurrido desde la reserva
    unsigned long reservedTime;

    Servo cerradura;
    RFID escaner;

    Sensor_DHT s_dht;

    MQTT mqtt;

public:
    Sala(const Sensor_DHT& s_dht, const Servo& cerradura, const MQTT& mqtt);

    void reservar(Usuario& usuario, int time);

    void abrir();
    void cerrar();

    bool is_reservada();
    bool is_ocupada();

    void update();


};
#endif //SALA_H
