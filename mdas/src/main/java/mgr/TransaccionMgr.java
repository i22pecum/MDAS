package mgr;

import java.util.ArrayList;
import dto.Entrada;
import dao.EntradaDAO;
import dao.EventoDAO;
import dao.TransaccionDAO;
import dao.UsuarioDAO;
import dto.Evento;
import dto.Organizador;
import dto.TipoTransaccion;
import dto.Transaccion;
import dao.OrganizadorDAO;

public class TransaccionMgr {
    
    private static TransaccionMgr instance;


    private TransaccionMgr() {}

	public static TransaccionMgr getInstance() {
		if(instance == null) {
			instance = new TransaccionMgr();
		}
		return instance;
	}

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
            if(nombreEventos.contains(entrada.getNombreEvento()) == false){
                entradas.remove(entrada);
            }
        }

        return entradas;
    }

    public void devolverDinero(String nombreEvento){
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        OrganizadorDAO organizadorDAO = new OrganizadorDAO();
        ArrayList<Transaccion> transacciones = new ArrayList<>();

        transacciones = transaccionDAO.getTransaccionesByNombreEvento(nombreEvento);

        for (Transaccion transaccion : transacciones){
            usuarioDAO.recargarMonedero(transaccion.getCorreoComprador(), transaccion.getPrecio());
            if(transaccion.getTipo().equals(TipoTransaccion.VENTASECUNDARIA) == true){
                usuarioDAO.restarMonedero(transaccion.getCorreoVendedor(), transaccion.getPrecio());
            }
            else{
                organizadorDAO.restarMonedero(transaccion.getCorreoVendedor(), transaccion.getPrecio());
            }
        }
    }

    public Boolean eliminarTransacciones(String nombreEvento){
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        return transaccionDAO.eliminarTransaccionesByNombreEvento(nombreEvento);
    }

    public Boolean eliminarEntradas(String nombreEvento){
        EntradaDAO entradaDAO = new EntradaDAO();
        ArrayList<Integer> idEntradas = new ArrayList<>();

        idEntradas = entradaDAO.getIdEntradasByEvento(nombreEvento);

        entradaDAO.eliminarEntradasVendidasByIdEntrada(idEntradas);

        entradaDAO.eliminarEntradasByNombreEvento(nombreEvento);

        return true;
    }
}
