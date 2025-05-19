package dto;

/**
 * Clase factoría para crear objetos Entrada de forma simplificada.
 * Permite instanciar entradas de distintos tipos con distintos parámetros
 */

public class EntradaFactory {

  
    public static Entrada createEntradaGeneral(float precio, String nombreEvento) {
        return new Entrada(TipoEntrada.GENERAL, precio, nombreEvento);
    }

   
    public static Entrada createEntradaGeneralConCantidad(float precio, String nombreEvento, int cantidad) {
        return new Entrada(TipoEntrada.GENERAL, precio, nombreEvento, cantidad);
    }

 
    public static Entrada createEntradaVIP(float precio, String nombreEvento) {
        return new Entrada(TipoEntrada.VIP, precio, nombreEvento);
    }

    
    public static Entrada createEntradaVIPConCantidad(float precio, String nombreEvento, int cantidad) {
        return new Entrada(TipoEntrada.VIP, precio, nombreEvento, cantidad);
    }

    
    public static Entrada createEntradaNumerada(float precio, String nombreEvento) {
        return new Entrada(TipoEntrada.NUMERADA, precio, nombreEvento);
    }

    
    public static Entrada createEntradaNumeradaConCantidad(float precio, String nombreEvento, int cantidad) {
        return new Entrada(TipoEntrada.NUMERADA, precio, nombreEvento, cantidad);
    }

    
    public static Entrada createEntrada(TipoEntrada tipoEntrada, float precio, String nombreEvento) {
        return new Entrada(tipoEntrada, precio, nombreEvento);
    }

    /**
     * Crea una entrada con tipo de entrada, precio, nombre del evento y correo del vendedor.
     * Esta función se utiliza normalmente para revender entradas.
     */
    public static Entrada createEntradaConCorreoVendedor(TipoEntrada tipoEntrada, float precio, String nombreEvento,
            String correoVendedor) {
        return new Entrada(tipoEntrada, precio, nombreEvento, correoVendedor);
    }

    
    public static Entrada createEntradaGeneralCompleto(float precio, String nombreEvento, int cantidad, String correoVendedor) {
        return new Entrada(TipoEntrada.GENERAL, precio, nombreEvento, cantidad, correoVendedor);
    }

    
    public static Entrada createEntradaVIPCompleto(float precio, String nombreEvento, int cantidad, String correoVendedor) {
        return new Entrada(TipoEntrada.VIP, precio, nombreEvento, cantidad, correoVendedor);
    }

    
    public static Entrada createEntradaNumeradaCompleto(float precio, String nombreEvento, int cantidad, String correoVendedor) {
        return new Entrada(TipoEntrada.NUMERADA, precio, nombreEvento, cantidad, correoVendedor);
    }
}
