package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPuesto;

public class PuestoDAO extends BaseDAO<CatPuesto, Integer> {
	
	private static Logger log = LogManager.getLogger(PuestoDAO.class);

	public PuestoDAO(Class<CatPuesto> modelClass) {
		super(modelClass);
	}
	
	public List<CatPuesto> buscarActivo() {
		List<CatPuesto> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatPuesto.findActive", modelClass).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de puestos...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
