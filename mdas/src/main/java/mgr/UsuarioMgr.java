package mgr;

import dto.Usuario;
import dao.UsuarioDAO;

public class UsuarioMgr {
    
    private static UsuarioMgr instance;


    private UsuarioMgr() {}

	public static UsuarioMgr getInstance() {
		if(instance == null) {
			instance = new UsuarioMgr();
		}
		return instance;
	}

    public Boolean Registrarse(Usuario usuario) {
        Boolean registrado = false;
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        registrado = usuarioDAO.insertarUsuario(usuario);

        return registrado;
    }
}
