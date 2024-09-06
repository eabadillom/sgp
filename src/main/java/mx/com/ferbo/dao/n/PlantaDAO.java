package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPlanta;

public class PlantaDAO extends BaseDAO<CatPlanta, Integer> {
	
	private static Logger log = LogManager.getLogger(PlantaDAO.class);

	public PlantaDAO(Class<CatPlanta> modelClass) {
		super(modelClass);
	}
	
	public List<CatPlanta> buscarActivo() {
		List<CatPlanta> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatPlanta.findActive", modelClass).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de plantas...", ex);
		} finally {
			this.close(em);
		}
		return modelList;
	}
	
	public List<CatPlanta> buscarTodos() {
		List<CatPlanta> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatPlanta.getAll", modelClass).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de plantas...", ex);
		} finally {
			this.close(em);
		}
		return modelList;
	}

}
