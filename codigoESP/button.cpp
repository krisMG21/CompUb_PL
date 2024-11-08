#include "button.h"

bool Button::isPressed(){
    int value = digitalRead(pin);
    return (bool)value;
}

int Button::read(){
    int value = digitalRead(pin);
    return value;
}
