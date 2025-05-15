package mgr;

import dto.Organizador;
import dao.OrganizadorDAO;

public class OrganizadorMgr {

    private static OrganizadorMgr instance;

    private OrganizadorMgr() {}

    public static OrganizadorMgr getInstance() {
        if (instance == null) {
            instance = new OrganizadorMgr();
        }
        return instance;
    }

    
}
