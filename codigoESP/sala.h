#ifndef SALA_H
#define SALA_H

typedef struct {
    int id;
} Usuario;

#include "comps/sensor.h"

class Sala {
private:
    Usuario usuario;
    bool ocupada;
    bool reservada;

    //Servo cerradura;
    //RFID escaner;

    Sensor_DHT s_dht;

public:
    Sala(const Sensor_DHT& s_dht);

    void reservar(Usuario& usuario);

    void update();


};
#endif //SALA_H
