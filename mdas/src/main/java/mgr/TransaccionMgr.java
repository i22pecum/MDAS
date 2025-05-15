package mgr;

import java.util.ArrayList;
import dto.Entrada;
import dao.EntradaDAO;

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
        EntradaDAO entradaDAO = new EntradaDAO();

        idEntradas = entradaDAO.getEntradasByCorreo(correo);

        entradas = entradaDAO.getEntradasById(idEntradas);

        return entradas;
    }
}
