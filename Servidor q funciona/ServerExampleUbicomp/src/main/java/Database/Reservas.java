package Database;

import java.util.Date;

public class Reservas {
    private int idReservas;
    private String emailUsuario;
    private int idSala;
    private Date horaReserva;

    // Constructor vacío
    public Reservas() {}

    // Constructor con parámetros
    public Reservas(int idReservas, String emailUsuario, int idSala, Date horaReserva) {
        this.idReservas = idReservas;
        this.emailUsuario = emailUsuario;
        this.idSala = idSala;
        this.horaReserva = horaReserva;
    }

    // Getters y setters
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

    // Método para representar la horaReserva como String
    public String getHoraReservaString() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(horaReserva);
    }

    // Método toString para la depuración
    @Override
    public String toString() {
        return "Reservas{" +
                "idReservas=" + idReservas +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", idSala=" + idSala +
                ", horaReserva=" + getHoraReservaString() +
                '}';
    }
}
