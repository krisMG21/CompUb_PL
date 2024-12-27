/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

// Clase para la tabla LecturaSensores
import java.util.Date;

public class LecturaSensores {
    private int idLectura;
    private int idSensor;
    private int valor;
    private int idCubiculo;
    private int idSala;
    private Date fechaHora;

    public LecturaSensores() {}

    public LecturaSensores(int idLectura, int idSensor, int valor, int idCubiculo, int idSala, Date fechaHora) {
        this.idLectura = idLectura;
        this.idSensor = idSensor;
        this.valor = valor;
        this.idCubiculo = idCubiculo;
        this.idSala = idSala;
        this.fechaHora = fechaHora;
    }

    public int getIdLectura() {
        return idLectura;
    }

    public void setIdLectura(int idLectura) {
        this.idLectura = idLectura;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getIdCubiculo() {
        return idCubiculo;
    }

    public void setIdCubiculo(int idCubiculo) {
        this.idCubiculo = idCubiculo;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
}
