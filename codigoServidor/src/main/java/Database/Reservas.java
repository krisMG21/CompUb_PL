  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;


// Clase para la tabla Reservas
import java.util.Date;

public class Reservas {
    private int idReservas;
    private String emailUsuario;
    private int idSala;
    private Date horaReserva;

    public Reservas() {}

    public Reservas(int idReservas, String emailUsuario, int idSala, Date horaReserva) {
        this.idReservas = idReservas;
        this.emailUsuario = emailUsuario;
        this.idSala = idSala;
        this.horaReserva = horaReserva;
    }

    public int getIdReservas() {
        return idReservas;
    }

    public void setIdReservas(int idReservas) {
        this.idReservas = idReservas;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public Date getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(Date horaReserva) {
        this.horaReserva = horaReserva;
    }
}
