/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;


// Clase para la tabla Salas
public class Salas {
    private int idSala;
    private byte[] ocupada;

    public Salas() {}

    public Salas(int idSala, byte[] ocupada) {
        this.idSala = idSala;
        this.ocupada = ocupada;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public byte[] getOcupada() {
        return ocupada;
    }

    public void setOcupada(byte[] ocupada) {
        this.ocupada = ocupada;
    }
}