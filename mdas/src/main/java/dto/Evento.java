package dto;

import java.sql.Date;

public class Evento {
    private String nombre;
    private String descripcion;
    private String lugar;
    private Date fecha;
    private float precio;
    private int aforo;
    private String correoOrganizador;

    public Evento() {}

    public Evento(String nombre, String descripcion, String lugar, Date fecha, float precio, int aforo, String correoOrganizador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.fecha = fecha;
        this.precio = precio;
        this.aforo = aforo;
        this.correoOrganizador = correoOrganizador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    public String getCorreoOrganizador() {
        return correoOrganizador;
    }

    public void setCorreoOrganizador(String correoOrganizador) {
        this.correoOrganizador = correoOrganizador;
    }
}

