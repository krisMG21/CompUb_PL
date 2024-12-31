/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

// Clase para la tabla Cubiculos
public class Cubiculos {
    private int idCubiculo;
    private byte[] ocupada;

    public Cubiculos() {}

    public Cubiculos(int idCubiculo, byte[] ocupada) {
        this.idCubiculo = idCubiculo;
        this.ocupada = ocupada;
    }

    public int getIdCubiculo() {
        return idCubiculo;
    }

    public void setIdCubiculo(int idCubiculo) {
        this.idCubiculo = idCubiculo;
    }

    public byte[] getOcupada() {
        return ocupada;
    }

    public void setOcupada(byte[] ocupada) {
        this.ocupada = ocupada;
    }
}