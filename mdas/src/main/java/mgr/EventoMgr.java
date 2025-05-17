package mgr;

import java.util.ArrayList;

import dto.Entrada;
import dto.Evento;
import dao.EventoDAO;
import dao.EntradaDAO;

public class EventoMgr {

    private static EventoMgr instance;

    private EventoMgr() {
    }

    public static EventoMgr getInstance() {
        if (instance == null) {
            instance = new EventoMgr();
        }
        return instance;
    }

    public Boolean publicarEvento(Evento evento, ArrayList<Entrada> entradas) {
        EventoDAO eventoDAO = new EventoDAO();
        EntradaDAO entradaDAO = new EntradaDAO();
        Boolean publicado = false;

        publicado = eventoDAO.insertarEvento(evento);
        for (Entrada entrada : entradas) {
            publicado = entradaDAO.insertarEntrada(entrada);
        }

        return publicado;
    }

    public Boolean cancelarEvento(String nombreEvento) {
        EventoDAO eventoDAO = new EventoDAO();
        return eventoDAO.cancelarEvento(nombreEvento);
    }

    public Boolean modificarEvento(Evento evento) {
        EventoDAO eventoDAO = new EventoDAO();
        return eventoDAO.modificarEvento(evento);
    }

    public ArrayList<Evento> listarEventosDisponibles() {
        EventoDAO eventoDAO = new EventoDAO();
        return eventoDAO.listarEventosDisponibles();
    }

    public ArrayList<Evento> verEventosOrganizador(String correo) {
        EventoDAO eventoDAO = new EventoDAO();
        return eventoDAO.verEventosOrganizador(correo);
    }

    public ArrayList<Evento> listarEventosComprar() {
        EventoDAO eventoDAO = new EventoDAO();
        return eventoDAO.listarEventosComprar();
    }

}
