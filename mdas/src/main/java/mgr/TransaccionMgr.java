package mgr;

import java.util.ArrayList;
import dao.*;
import dto.*;

//Clase que gestiona la lógica de negocio relacionada con transacciones

public class TransaccionMgr {

    private static TransaccionMgr instance;

    private TransaccionMgr() {
    }

    public static TransaccionMgr getInstance() {
        if (instance == null) {
            instance = new TransaccionMgr();
        }
        return instance;
    }

    /**
     * Devuelve una lista de entradas del usuario que aún se pueden revender (es decir, entradas asociadas a eventos futuros).
     * @param correo Correo del usuario
     * @return Lista de entradas válidas para reventa
     */
    public ArrayList<Entrada> verEntradasUsuario(String correo) {
        ArrayList<Entrada> entradas = new ArrayList<>();
        ArrayList<Integer> idEntradas = new ArrayList<>();
        ArrayList<Evento> eventos = new ArrayList<>();
        ArrayList<String> nombreEventos = new ArrayList<>();
        EntradaDAO entradaDAO = new EntradaDAO();
        EventoDAO eventoDAO = new EventoDAO();

        idEntradas = entradaDAO.getIdEntradasByCorreo(correo);

        entradas = entradaDAO.getEntradasById(idEntradas);

        eventos = eventoDAO.listarEventosDisponibles();

        for (Evento evento : eventos) {
            nombreEventos.add(evento.getNombre());
        }

        for (Entrada entrada : entradas) {
            if (nombreEventos.contains(entrada.getNombreEvento()) == false) {
                entradas.remove(entrada);
            }
        }

        return entradas;
    }

        /**
     * Devuelve una lista de entradas asociadas a un usuario que **aún no han sido revendidas** y
     * que pertenecen a eventos que todavía están disponibles (no han pasado).
     * @param correo Correo del usuario del cual se quieren consultar las entradas.
     * @return Lista de entradas válidas para publicar en reventa.
     */
    public ArrayList<Entrada> verEntradasUsuarioSinReventa(String correo) {
        ArrayList<Entrada> entradas = new ArrayList<>();
        ArrayList<Integer> idEntradas = new ArrayList<>();
        ArrayList<Evento> eventos = new ArrayList<>();
        ArrayList<String> nombreEventos = new ArrayList<>();
        EntradaDAO entradaDAO = new EntradaDAO();
        EventoDAO eventoDAO = new EventoDAO();

        idEntradas = entradaDAO.getIdEntradasByCorreo(correo);

        entradas = entradaDAO.getEntradasByIdSinReventa(idEntradas);

        eventos = eventoDAO.listarEventosDisponibles();

        for (Evento evento : eventos) {
            nombreEventos.add(evento.getNombre());
        }

        for (Entrada entrada : entradas) {
            if (nombreEventos.contains(entrada.getNombreEvento()) == false) {
                entradas.remove(entrada);
            }
        }

        return entradas;
    }


    /**
     * Devuelve el dinero a los compradores y resta el saldo al vendedor en caso de cancelación de evento.
     * @param nombreEvento Nombre del evento cancelado
     */
    public void devolverDinero(String nombreEvento) {
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        OrganizadorDAO organizadorDAO = new OrganizadorDAO();
        ArrayList<Transaccion> transacciones = new ArrayList<>();

        transacciones = transaccionDAO.getTransaccionesByNombreEvento(nombreEvento);

        for (Transaccion transaccion : transacciones) {
            usuarioDAO.recargarMonedero(transaccion.getCorreoComprador(), transaccion.getPrecio());
            if (transaccion.getTipo().equals(TipoTransaccion.VENTASECUNDARIA) == true) {
                usuarioDAO.restarMonedero(transaccion.getCorreoVendedor(), transaccion.getPrecio());
            } else {
                organizadorDAO.restarMonedero(transaccion.getCorreoVendedor(), transaccion.getPrecio());
            }
        }
    }

    /**
     * Elimina las transacciones asociadas a un evento.
     * @param nombreEvento Nombre del evento
     * @return true si se eliminaron correctamente
     */
    public Boolean eliminarTransacciones(String nombreEvento) {
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        return transaccionDAO.eliminarTransaccionesByNombreEvento(nombreEvento);
    }


    /**
     * Elimina las entradas asociadas a un evento (tanto en entradas vendidas como en disponibles).
     * @param nombreEvento Nombre del evento
     * @return true si se eliminaron correctamente
     */
    public Boolean eliminarEntradas(String nombreEvento) {
        EntradaDAO entradaDAO = new EntradaDAO();
        ArrayList<Integer> idEntradas = new ArrayList<>();

        idEntradas = entradaDAO.getIdEntradasByEvento(nombreEvento);

        entradaDAO.eliminarEntradasVendidasByIdEntrada(idEntradas);

        entradaDAO.eliminarEntradasByNombreEvento(nombreEvento);

        return true;
    }


    /**
     * Publica una entrada como reventa y actualiza su ID.
     * @param entradaReventa Objeto Entrada con los datos de reventa
     * @param idEntrada ID de la entrada original
     * @return true si la reventa se publica y actualiza correctamente
     */
    public boolean publicarReventa(Entrada entradaReventa, int idEntrada) {
        EntradaDAO entradaDAO = new EntradaDAO();
        int idReventa = 0;
        boolean actualizado = false;

        idReventa = entradaDAO.insertarReventa(entradaReventa);

        if(idReventa != 0){
            actualizado = entradaDAO.actualizarId(idEntrada, idReventa); 
        }

        return actualizado;
    }


    /**
     * Lista las entradas disponibles para un evento concreto y tipo de transacción (primaria/secundaria).
     * @param nombreEvento Nombre del evento
     * @param tipoTransaccion Tipo de transacción (primaria o secundaria)
     * @return Lista de entradas disponibles
     */
    public ArrayList<Entrada> verEntradasPorEvento(String nombreEvento, TipoTransaccion tipoTransaccion) {
        EntradaDAO entradaDAO = new EntradaDAO();
        return entradaDAO.getEntradasDisponiblesByNombreEvento(nombreEvento, tipoTransaccion);
    }


    /**
     * Procesa la compra de una entrada, actualiza stock, transacciones y monederos.
     * @param correoUsuario Correo del comprador
     * @param entrada Objeto Entrada comprada
     * @param tipoTransaccion Tipo de transacción (primaria o secundaria)
     * @return true si la compra fue exitosa
     */
    public boolean comprarEntrada(String correoUsuario, Entrada entrada, TipoTransaccion tipoTransaccion) {
        EntradaDAO entradaDAO = new EntradaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Transaccion transaccion = new Transaccion(correoUsuario, entrada.getCorreoVendedor(), entrada.getPrecio(),
                tipoTransaccion);
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        OrganizadorDAO organizadorDAO = new OrganizadorDAO();
        ArrayList<Integer> idEntradas = new ArrayList<>();
        idEntradas.add(entrada.getId());

        
        entradaDAO.disminuirCantidadEntrada(entrada.getId());

        usuarioDAO.restarMonedero(correoUsuario, entrada.getPrecio());
        
        if (tipoTransaccion.equals(TipoTransaccion.VENTASECUNDARIA) == true) {
            usuarioDAO.recargarMonedero(entrada.getCorreoVendedor(), entrada.getPrecio());
            entradaDAO.eliminarEntradasVendidasByIdEntrada(idEntradas);
        } else {
            
            organizadorDAO.recargarMonedero(entrada.getCorreoVendedor(), entrada.getPrecio());
        }

        entradaDAO.insertarEntradaVendida(entrada.getId(), correoUsuario);
        
        transaccionDAO.insertarTransaccion(transaccion, entrada.getNombreEvento());
        
        return true;
    }

}
