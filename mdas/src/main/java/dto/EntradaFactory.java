package dto;

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

    public static Entrada createEntradaConCorreoVendedor(TipoEntrada tipoEntrada, float precio, String nombreEvento,
            String correoVendedor) {
        return new Entrada(tipoEntrada, precio, nombreEvento, correoVendedor);
    }
}
