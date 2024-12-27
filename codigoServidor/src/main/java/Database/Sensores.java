/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;


// Clase para la tabla Sensores
public class Sensores {
    private int idSensor;
    private String nombre;

    public Sensores() {}

    public Sensores(int idSensor, String nombre) {
        this.idSensor = idSensor;
        this.nombre = nombre;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}