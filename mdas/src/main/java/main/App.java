package main;

import java.util.ArrayList;

import dto.*;
import mgr.*;

public class App {
    public static void main(String[] args) {
        int opc;
        UsuarioMgr usuarioMgr = UsuarioMgr.getInstance();
        Usuario usuario = new Usuario();

        App.clearConsole();
        do {
            System.out.println("\nSelecciona la acción que desees realizar:" +
                    "\n1.- Registrarse" +
                    "\n2.- Iniciar Sesión" +
                    "\n0.- Salir");

            opc = aux.Scanf.scanInt();

            // Para hacer un clear de la terminal
            App.clearConsole();

            switch (opc) {
                case 0:
                    System.out.println("\nSaliendo de la aplicación");
                    break;
                case 1: // Funcion de registrarse
                    System.out.println("\nIntroduce tu correo:");
                    usuario.setCorreo(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu contraseña:");
                    usuario.setContrasena(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu nombre y apellidos:");
                    usuario.setNombreCompleto(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu numero de teléfono:");
                    usuario.setTelefono(aux.Scanf.scanInt());
                    System.out.println("\nIntroduce tu DNI:");
                    usuario.setDni(aux.Scanf.scanDni());

                    App.clearConsole();
                    if (usuarioMgr.Registrarse(usuario) == true) {
                        System.out.println("\nUsuario registrado correctamente");
                    } else {
                        System.out.println("\nError al registrar el usuario");
                    }
                    break;

                case 2:
                    App.interfazIniciarSesion();
                    break;
                default:
                    System.out.println("\nLa opción seleccionada no existe");
                    break;
            }
        } while (opc != 0);

    }

    public static void interfazIniciarSesion() {
        int opcIS;
        UsuarioMgr usuarioMgr = UsuarioMgr.getInstance();
        Usuario usuario = new Usuario();
        Organizador organizador = new Organizador();

        App.clearConsole();
        do {
            System.out.println("\nSelecciona como quieres Iniciar Sesión:" +
                    "\n1.- Iniciar Sesión como Organizador" +
                    "\n2.- Iniciar Sesión como Usuario" +
                    "\n0.- Volver");

            opcIS = aux.Scanf.scanInt();

            switch (opcIS) {
                case 0:
                    System.out.println("\nVolviendo al Menú Principal");
                    break;
                case 1:
                    // Inicio como organizador
                    System.out.println("\nIntroduce tu correo:");
                    organizador.setCorreo(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu contraseña:");
                    organizador.setContrasena(aux.Scanf.scanString());

                    App.clearConsole();
                    if (usuarioMgr.IniciarSesionOrganizador(organizador) == true) {
                        System.out.println("\nIniciando sesion como Organizador");
                        interfazOrganizador(organizador.getCorreo());
                    } else {
                        System.out.println("Credenciales incorrectas.");
                    }
                    break;
                case 2:
                    // Inicio como usuario
                    System.out.println("\nIntroduce tu correo:");
                    usuario.setCorreo(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu contraseña:");
                    usuario.setContrasena(aux.Scanf.scanString());

                    App.clearConsole();
                    if (usuarioMgr.IniciarSesionUsuario(usuario) == true) {
                        System.out.println("\nIniciando sesion como Usuario");
                        interfazUsuario(usuario.getCorreo());
                    } else {
                        System.out.println("\nError al iniciar sesion, el correo o la contraseña son incorrectos");
                    }
                    break;
                default:
                    System.out.println("\nLa opción seleccionada no existe");
                    break;
            }
        } while (opcIS != 0);
    }

    /*
     * Lo mismo hay que pasar como argumento el correo
     */
    public static void interfazUsuario(String correoUsuario) {
        int opc, contador = 0;

        UsuarioMgr usuarioMgr = UsuarioMgr.getInstance();
        Usuario usuario = new Usuario();

        TransaccionMgr transaccionMgr = TransaccionMgr.getInstance();
        EventoMgr eventoMgr = EventoMgr.getInstance();

        float saldoActual = 0, cantidad = 0, saldoNuevo = 0;
        Boolean recargaMonedero = false;
        ArrayList<Entrada> entradas = new ArrayList<Entrada>();
        ArrayList<Evento> eventosDisponibles = new ArrayList<Evento>();

        do {
            System.out.println("\nSelecciona la acción que desees realizar:" +
                    "\n1.- Comprar Entrada" +
                    "\n2.- Publicar Reventa" +
                    "\n3.- Recargar Monedero" +
                    "\n4.- Ver mis entradas" +
                    "\n5.- Valorar Usuario" +
                    "\n6.- Reclamar/Consultar" +
                    "\n0.- Cerrar Sesión");

            opc = aux.Scanf.scanInt();

            // Para hacer un clear de la terminal
            App.clearConsole();

            switch (opc) {
                case 0:
                    System.out.println("\nCerrando sesión");
                    break;
                case 1:
                    // Funcion de comprar entradas

                    // 1. Mostrar eventos disponibles
                    System.out.println("\nEventos disponibles:");
                    ArrayList<Evento> listarEventosDisponibles = eventoMgr.listarEventosComprar();

                    if (listarEventosDisponibles.isEmpty()) {
                        System.out.println("No hay eventos disponibles.");
                    } else {
                        for (Evento evento : eventosDisponibles) {
                            System.out.println("Nombre: " + evento.getNombre());
                            System.out.println("Descripción: " + evento.getDescripcion());
                            System.out.println("Lugar: " + evento.getLugar());
                            System.out.println("Fecha: " + evento.getFecha());
                            System.out.println("--------------------------");
                        }
                    }

                    // 2. Pedir evento
                    System.out.println("\nIntroduce el nombre del evento para comprar entrada:");
                    String eventoCompra = aux.Scanf.scanString();

                    // 3. Mostrar tipos de entrada disponibles para ese evento
                    System.out.println("\nEntradas disponibles para el evento: " + eventoCompra);
                    ArrayList<Entrada> entradasDisponibles = transaccionMgr.verEntradasPorEvento(eventoCompra);

                    if (entradasDisponibles.isEmpty()) {
                        System.out.println("No hay entradas disponibles para este evento.");
                    } else {
                        for (Entrada entrada : entradasDisponibles) {
                            System.out.println("Tipo: " + entrada.getTipo());
                            System.out.println("Precio: " + entrada.getPrecio() + "€");
                            System.out.println("--------------------------");
                        }
                    }

                    // 4. Pedir tipo de entrada
                    System.out.println("\nIntroduce el tipo de entrada (GENERAL, VIP, NUMERADA):");
                    String tipoEntradaStr = aux.Scanf.scanString();

                    TipoEntrada tipoEntradaCompra = null;
                    try {
                        tipoEntradaCompra = TipoEntrada.valueOf(tipoEntradaStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Tipo de entrada no válido.");
                    }

                    // 5. Ejecutar compra
                    boolean exitoCompra = transaccionMgr.comprarEntrada(correoUsuario, eventoCompra, tipoEntradaCompra);

                    if (exitoCompra) {
                        System.out.println("\nCompra realizada con éxito.");
                    } else {
                        System.out.println("\nNo se pudo realizar la compra. Revisa saldo o disponibilidad.");
                    }

                    break;
                case 2:
                    // Función de publicar reventa
                    entradas = transaccionMgr.verEntradasUsuario(correoUsuario);
                    contador = 1;
                    App.clearConsole();

                    if (entradas.isEmpty()) {
                        System.out.println("\nNo tienes entradas disponibles para reventa.");
                        break;
                    }

                    System.out.println("\nTienes las siguientes entradas disponibles para reventa:");
                    for (Entrada entrada : entradas) {

                        System.out.println("\n" + contador + ".- La entrada pertenece al evento: "
                                + entrada.getNombreEvento() +
                                "\n\t Entrada de tipo: " + entrada.getTipo() +
                                "\n\t Precio original: " + entrada.getPrecio() + "\n");
                        contador++;
                    }

                    System.out.println("\nSelecciona el número de la entrada que deseas revender:");
                    int entradaSeleccionada = aux.Scanf.scanInt() - 1;

                    if (entradaSeleccionada < 0 || entradaSeleccionada >= entradas.size()) {
                        System.out.println("\nSelección inválida.");
                        break;
                    }

                    Entrada entradaReventa = entradas.get(entradaSeleccionada);

                    float limiteReventaEvento = 0;// eventoMgr.getLimiteReventaEvento(entradaReventa.getNombreEvento());

                    System.out.println("\nIntroduce el precio de reventa (máximo permitido: " +
                            (entradaReventa.getPrecio() * limiteReventaEvento) + "):");
                    float precioReventa = aux.Scanf.scanFloat();

                    if (precioReventa <= 0
                            || precioReventa > (entradaReventa.getPrecio() * limiteReventaEvento)) {
                        System.out.println("\nPrecio inválido. Debe estar dentro del límite de reventa.");
                        break;
                    }

                    // Publicar la entrada como reventa
                    Entrada nuevaReventa = EntradaFactory.createEntradaConCorreoVendedor(entradaReventa.getTipo(),
                            precioReventa, entradaReventa.getNombreEvento(), correoUsuario);

                    boolean publicada = transaccionMgr.publicarReventa(entradaReventa);

                    if (publicada) {
                        System.out.println("\nEntrada publicada para reventa con éxito.");
                    } else {
                        System.out.println("\nError al publicar la entrada para reventa.");
                    }

                    break;
                case 3: // Terminado
                    // Funcion de recargar monedero
                    saldoActual = usuarioMgr.consultarMonedero(correoUsuario);
                    System.out.println("\nSaldo actual: " + saldoActual);
                    System.out.println("Introduce la cantidad a recargar:");
                    cantidad = aux.Scanf.scanFloat();

                    if (cantidad > 0) {
                        recargaMonedero = usuarioMgr.recargarMonedero(correoUsuario, cantidad);
                        if (recargaMonedero) {
                            saldoNuevo = usuarioMgr.consultarMonedero(correoUsuario);
                            System.out.println("\nRecarga realizada con éxito. Nuevo saldo: " + saldoNuevo);
                        } else {
                            System.out.println("\nError al recargar el monedero.");
                        }
                    } else {
                        System.out.println("\nCantidad inválida.");
                    }
                    break;
                case 4: // Terminado
                    // Funcion de ver mis entradas
                    entradas = transaccionMgr.verEntradasUsuario(correoUsuario);
                    contador = 1;
                    App.clearConsole();
                    if (entradas.isEmpty()) {
                        System.out.println("\nNo tienes entradas compradas");
                        break;
                    }
                    System.out.println("\nTienes las siguientes entradas:");
                    for (Entrada entrada : entradas) {
                        System.out.println(
                                "\n" + contador + ".- La entrada pertenece al evento:" + entrada.getNombreEvento() +
                                        "\n\t Entrada de tipo:" + entrada.getTipo() +
                                        "\n\t Precio de la entrada: " + entrada.getPrecio() + "\n");
                        contador++;
                    }

                    break;
                case 5: // Terminado
                    // Funcion de Valorar Usuario
                    System.out.println("\nLo sentimos, esta función no se ha implementado todavia");
                    break;
                case 6: // Terminado
                    // Funcion de Reclamar/Consultar
                    System.out.println("\nLo sentimos, esta función no se ha implementado todavia");
                    break;
                default:
                    System.out.println("\nLa opción seleccionada no existe");
                    break;
            }
        } while (opc != 0);
    }

    public static void interfazOrganizador(String correoOrganizador) {
        int opc;
        int contador = 0;
        int eventoSeleccionado = 0;
        float precio = 0;
        int cantidad = 0;
        EventoMgr eventoMgr = EventoMgr.getInstance();
        TransaccionMgr transaccionMgr = TransaccionMgr.getInstance();
        Evento evento = new Evento();
        ArrayList<Entrada> entradas = new ArrayList<Entrada>();
        ArrayList<Evento> eventos = new ArrayList<Evento>();
        do {
            System.out.println("\nSelecciona la acción que desees realizar:" +
                    "\n1.- Publicar Evento" +
                    "\n2.- Modificar Evento" +
                    "\n3.- Cancelar Evento" +
                    "\n4.- Ver mis eventos" +
                    "\n0.- Cerrar Sesión");

            opc = aux.Scanf.scanInt();

            // Para hacer un clear de la terminal
            App.clearConsole();

            switch (opc) {
                case 0:
                    System.out.println("\nCerrando sesión");
                    break;
                case 1:
                    // Funcion de publicar evento
                    Evento Evento = new Evento();

                    evento.setCorreoOrganizador(correoOrganizador);

                    System.out.println("\nIntroduce el nombre del evento:");
                    evento.setNombre(aux.Scanf.scanString());

                    System.out.println("\nIntroduce la descripción del evento:");
                    evento.setDescripcion(aux.Scanf.scanString());

                    System.out.println("\nIntroduce el lugar del evento:");
                    evento.setLugar(aux.Scanf.scanString());

                    System.out.println("\nIntroduce la fecha (formato yyyy-MM-dd):");
                    evento.setFecha(aux.Scanf.scanFecha());

                    System.out.println("\nIntroduce el limite de reventa (0,%):");
                    evento.setLimiteReventa(aux.Scanf.scanFloat());

                    System.out.println("\nIntroduce la cantidad de entradas Generales:");
                    cantidad = aux.Scanf.scanInt();

                    System.out.println("\nIntroduce el precio de las entradas Generales:");
                    precio = aux.Scanf.scanFloat();

                    entradas.add(EntradaFactory.createEntradaGeneralConCantidad(precio, evento.getNombre(), cantidad));

                    System.out.println("\nIntroduce la cantidad de entradas VIP:");
                    cantidad = aux.Scanf.scanInt();

                    System.out.println("\nIntroduce el precio de las entradas VIP:");
                    precio = aux.Scanf.scanFloat();

                    entradas.add(EntradaFactory.createEntradaVIPConCantidad(precio, evento.getNombre(), cantidad));

                    System.out.println("\nIntroduce la cantidad de entradas Numeradas:");
                    cantidad = aux.Scanf.scanInt();

                    System.out.println("\nIntroduce el precio de las entradas Numeradas:");
                    precio = aux.Scanf.scanFloat();

                    entradas.add(EntradaFactory.createEntradaNumeradaConCantidad(precio, evento.getNombre(), cantidad));

                    boolean publicado = eventoMgr.publicarEvento(evento, entradas);

                    if (publicado) {
                        System.out.println("\nEvento publicado correctamente.");
                    } else {
                        System.out.println("\nError al publicar el evento.");
                    }
                    break;
                case 2:
                    // Funcion de modificar evento

                    /*
                     * SE TIENE QUE HACER UNA INTERFAZ APARTE PARA ESTO
                     */
                    System.out.println("Selecciona el evento que deseas modificar:");
                    eventos = eventoMgr.verEventosOrganizador(correoOrganizador);

                    contador = 1;

                    if (eventos.isEmpty()) {
                        System.out.println("\nNo tienes eventos publicados");
                        break;
                    }

                    for (Evento eventoDisponible : eventos) {
                        System.out.println("\n" + contador + ".- " + eventoDisponible.getNombre());
                        contador++;
                    }

                    eventoSeleccionado = aux.Scanf.scanInt() - 1;

                    evento = eventos.get(eventoSeleccionado);

                    /*
                     * Mostrar información del evento seleccionado
                     * 
                     * ES POSIBLE Q SEA RECOMENDABLE HACERLO EN UNA FUNCIÓN APARTE !!!!!!!
                     */
                    System.out.println("\nInformación del evento seleccionado:");
                    System.out.println("Nombre: " + evento.getNombre());
                    System.out.println("Descripción: " + evento.getDescripcion());
                    System.out.println("Lugar: " + evento.getLugar());
                    System.out.println("Fecha: " + evento.getFecha());
                    System.out.println("LimiteReventa: " + evento.getLimiteReventa());

                    System.out.println("Introduce la nueva descripción del evento:");
                    evento.setDescripcion(aux.Scanf.scanString());

                    System.out.println("Introduce el nuevo lugar del evento:");
                    evento.setLugar(aux.Scanf.scanString());

                    System.out.println("Introduce la nueva fecha (formato yyyy-MM-dd):");
                    evento.setFecha(aux.Scanf.scanFecha());

                    System.out.println("Introduce el limite de reventa (0,%):");
                    evento.setLimiteReventa(aux.Scanf.scanFloat());

                    boolean modificado = eventoMgr.modificarEvento(evento);

                    if (modificado) {
                        System.out.println("\nEvento modificado correctamente.");
                    } else {
                        System.out.println(
                                "\nError al modificar el evento. Verifica que los datos introducidos sean correctos.");
                    }

                    break;
                case 3: // Terminado
                    // Funcion de cancelar evento

                    /*
                     * SE TIENE QUE HACER UNA INTERFAZ APARTE PARA ESTO
                     */
                    System.out.println("Selecciona el evento que deseas cancelar:");
                    eventos = eventoMgr.verEventosOrganizador(correoOrganizador);

                    contador = 1;

                    if (eventos.isEmpty()) {
                        System.out.println("\nNo tienes eventos publicados");
                        break;
                    }

                    for (Evento eventoDisponible : eventos) {
                        System.out.println("\n" + contador + ".- " + eventoDisponible.getNombre());
                        contador++;
                    }
                    eventoSeleccionado = aux.Scanf.scanInt() - 1;
                    evento = eventos.get(eventoSeleccionado);

                    transaccionMgr.devolverDinero(evento.getNombre());

                    transaccionMgr.eliminarTransacciones(evento.getNombre());

                    transaccionMgr.eliminarEntradas(evento.getNombre());

                    boolean cancelado = eventoMgr.cancelarEvento(evento.getNombre());

                    if (cancelado) {
                        System.out.println("\nEvento cancelado correctamente.");
                    } else {
                        System.out.println("\nNo se pudo cancelar el evento. ¿Existe con ese nombre?");
                    }

                    break;
                case 4:
                    // Funcion de ver mis eventos

                    /*
                     * SE TIENE QUE HACER UNA INTERFAZ APARTE PARA ESTO
                     */
                    System.out.println("\nEventos publicados por el organizador: " + correoOrganizador);
                    eventos = eventoMgr.verEventosOrganizador(correoOrganizador);

                    contador = 1;

                    if (eventos.isEmpty()) {
                        System.out.println("\nNo tienes eventos publicados");
                        break;
                    }

                    for (Evento eventoDisponible : eventos) {
                        System.out.println("\n" + contador + ".- " + eventoDisponible.getNombre());
                        contador++;
                    }

                    eventoSeleccionado = aux.Scanf.scanInt() - 1;

                    evento = eventos.get(eventoSeleccionado);

                    /*
                     * Mostrar información del evento seleccionado
                     * 
                     * ES POSIBLE Q SEA RECOMENDABLE HACERLO EN UNA FUNCIÓN APARTE !!!!!!!
                     */
                    System.out.println("\nInformación del evento seleccionado:");
                    System.out.println("Nombre: " + evento.getNombre());
                    System.out.println("Descripción: " + evento.getDescripcion());
                    System.out.println("Lugar: " + evento.getLugar());
                    System.out.println("Fecha: " + evento.getFecha());
                    System.out.println("LimiteReventa: " + evento.getLimiteReventa());

                    break;
                default:
                    System.out.println("\nLa opción seleccionada no existe");
                    break;
            }
        } while (opc != 0);
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
