package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatSalarioMinimo;

public class SalarioMinimoDAO extends BaseDAO<CatSalarioMinimo, Integer> {
	
	private static Logger log = LogManager.getLogger(SalarioMinimoDAO.class);

	public SalarioMinimoDAO(Class<CatSalarioMinimo> modelClass) {
		super(modelClass);
	}
	
	public SalarioMinimoDAO() {
		super(CatSalarioMinimo.class);
	}
	
	public List<CatSalarioMinimo> buscarTodos() {
		List<CatSalarioMinimo> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatSalarioMinimo.findAll", CatSalarioMinimo.class)
					.getResultList()
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de Salarios minimos...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
