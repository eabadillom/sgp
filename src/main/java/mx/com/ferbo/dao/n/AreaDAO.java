package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatArea;

public class AreaDAO extends BaseDAO<CatArea, Integer> {
	private static Logger log = LogManager.getLogger(AreaDAO.class);

	public AreaDAO(Class<CatArea> modelClass) {
		super(modelClass);
	}
	
	public List<CatArea> buscarActivo() {
		List<CatArea> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatArea.findActive", modelClass).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de areas...",  ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
	

}
