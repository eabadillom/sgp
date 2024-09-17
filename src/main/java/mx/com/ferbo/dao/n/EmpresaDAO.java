package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEmpresa;

public class EmpresaDAO extends BaseDAO<CatEmpresa, Integer>{
	
	private static Logger log = LogManager.getLogger(EmpresaDAO.class);

	public EmpresaDAO(Class<CatEmpresa> modelClass) {
		super(modelClass);
	}
	
	public List<CatEmpresa> buscarActivo() {
		List<CatEmpresa> list = null;
		EntityManager emSGP = null;
    	
    	try {
    		emSGP = this.getEntityManager();
    		list = emSGP.createNamedQuery("CatEmpresa.findActive", CatEmpresa.class).getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener el listado de empresas...", ex);
    	}
    	
        return list;
	}

}
