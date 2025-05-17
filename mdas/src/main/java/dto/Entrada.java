package dto;

import dto.TipoEntrada;

public class Entrada {
    TipoEntrada tipo;
    float precio;
    String nombreEvento;
    String correoVendedor;
    int cantidad;

    public Entrada(TipoEntrada tipo, float precio, String nombreEvento) {
        this.tipo = tipo;
        this.precio = precio;
        this.nombreEvento = nombreEvento;
    }

    public Entrada(TipoEntrada tipo, float precio, String nombreEvento, int cantidad) {
        this.tipo = tipo;
        this.precio = precio;
        this.nombreEvento = nombreEvento;
        this.cantidad = cantidad;
    }

    public Entrada(TipoEntrada tipo, float precio, String nombreEvento, String correoVendedor) {
        this.tipo = tipo;
        this.precio = precio;
        this.nombreEvento = nombreEvento;
        this.correoVendedor = correoVendedor;
    }

    public Entrada() {
    }

    public TipoEntrada getTipo() {
        return tipo;
    }

    public void setTipo(TipoEntrada tipo) {
        this.tipo = tipo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getCorreoVendedor() {
        return correoVendedor;
    }

    public void setCorreoVendedor(String correoVendedor) {
        this.correoVendedor = correoVendedor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
