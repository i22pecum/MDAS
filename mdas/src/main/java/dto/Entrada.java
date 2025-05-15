package dto;

import dto.TipoEntrada;

public class Entrada {
    TipoEntrada tipo;
    float precio;
    String nombreEvento;


    public Entrada(TipoEntrada tipo, float precio, String nombreEvento) {
        this.tipo = tipo;
        this.precio = precio;
        this.nombreEvento = nombreEvento;
    }

    public Entrada() {}

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
}
