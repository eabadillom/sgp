package mx.com.ferbo.dao.n;

import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPercepciones;

public class PercepcionesDAO extends BaseDAO<CatPercepciones, Integer> {
	
	private static Logger log = LogManager.getLogger(PercepcionesDAO.class);

	public PercepcionesDAO(Class<CatPercepciones> modelClass) {
		super(modelClass);
	}
	
	public CatPercepciones buscarActual(Date fechaCorte) {
		CatPercepciones model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("CatPercepciones.findVigente", modelClass)
					.setParameter("fechaCorte", fechaCorte)
					.getSingleResult()
					;
		} catch(Exception ex) {
			
		} finally {
			this.close(em);
		}
		
		return model;
	}

}
