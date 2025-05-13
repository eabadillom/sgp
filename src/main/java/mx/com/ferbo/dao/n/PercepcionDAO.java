package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPercepcion;

public class PercepcionDAO extends BaseDAO<CatPercepcion, String> {
	
	private static Logger log = LogManager.getLogger(PercepcionDAO.class);

	public PercepcionDAO(Class<CatPercepcion> modelClass) {
		super(modelClass);
	}
	
	public PercepcionDAO() {
		super(CatPercepcion.class);
	}
	
	public List<CatPercepcion> buscarTodos() {
		List<CatPercepcion> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatPercepcion.buscarTodos", this.modelClass)
					.getResultList();
			
		} catch(Exception ex) {
			log.error("Problema para obtener el catálogo de percepciones...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
}
