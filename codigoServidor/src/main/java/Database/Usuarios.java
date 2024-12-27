/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

// Clase para la tabla Usuarios
public class Usuarios {
    private int idUsuario;
    private String passwd;
    private String emailUsuario;
    private byte[] tipo;
    private int idTarjeta;

    public Usuarios() {}

    public Usuarios(int idUsuario, String passwd, String emailUsuario, byte[] tipo, int idTarjeta) {
        this.idUsuario = idUsuario;
        this.passwd = passwd;
        this.emailUsuario = emailUsuario;
        this.tipo = tipo;
        this.idTarjeta = idTarjeta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public byte[] getTipo() {
        return tipo;
    }

    public void setTipo(byte[] tipo) {
        this.tipo = tipo;
    }

    public int getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(int idTarjeta) {
        this.idTarjeta = idTarjeta;
    }
}