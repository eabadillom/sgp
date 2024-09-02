package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPerfil;

public class PerfilDAO extends BaseDAO<CatPerfil, Integer> {
	
	private static Logger log = LogManager.getLogger(PerfilDAO.class);

	public PerfilDAO(Class<CatPerfil> modelClass) {
		super(modelClass);
	}
	
	public List<CatPerfil> buscarActivo() {
		List<CatPerfil> modelList = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatPerfil.findActive", modelClass).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de perfiles...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
