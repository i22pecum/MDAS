package mgr;

import java.util.ArrayList;
import dto.Evento;
import dao.EventoDAO;

public class EventoMgr {
    
    private static EventoMgr instance;

    private EventoMgr() {}

    public static EventoMgr getInstance() {
        if (instance == null) {
            instance = new EventoMgr();
        }
        return instance;
    }

    public Boolean publicarEvento(Evento evento) {
        EventoDAO eventoDAO = new EventoDAO();
        return eventoDAO.insertarEvento(evento);
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
}
