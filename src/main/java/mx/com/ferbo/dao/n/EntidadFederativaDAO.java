package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatEntidadFederativa;

public class EntidadFederativaDAO extends BaseDAO<CatEntidadFederativa, String> {
	
	private static Logger log = LogManager.getLogger(EntidadFederativaDAO.class);

	public EntidadFederativaDAO(Class<CatEntidadFederativa> modelClass) {
		super(modelClass);
	}
	
	public List<CatEntidadFederativa> buscarTodos() {
		List<CatEntidadFederativa> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatEntidadFederativa.getAll", modelClass)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Probmea para obtener el listado de entidades federativas...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
