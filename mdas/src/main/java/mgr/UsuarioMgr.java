package mgr;

import dto.Organizador;
import dto.Usuario;
import dao.OrganizadorDAO;
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

    public Boolean IniciarSesionUsuario(Usuario usuario) {
        Boolean permitirAcceso = false;
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        permitirAcceso = usuarioDAO.validarUsuario(usuario);

        return permitirAcceso;
    }

    public Boolean IniciarSesionOrganizador(Organizador organizador) {
        Boolean permitirAcceso = false;
        OrganizadorDAO OrganizadorDAO = new OrganizadorDAO();

        permitirAcceso = OrganizadorDAO.validarOrganizador(organizador);

        return permitirAcceso;
    }
    
    public float consultarMonedero(String correo) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.consultarMonedero(correo);
    }

    public boolean recargarMonedero(String correo, float cantidad){
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.recargarMonedero(correo, cantidad);
    }
}
