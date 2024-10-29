package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTipoPrestamo;

public class TipoPrestamoDAO extends BaseDAO<CatTipoPrestamo, Integer> {
	
	private static Logger log = LogManager.getLogger(TipoPrestamoDAO.class);

	public TipoPrestamoDAO(Class<CatTipoPrestamo> modelClass) {
		super(modelClass);
	}
	
	public TipoPrestamoDAO() {
		super(CatTipoPrestamo.class);
	}
	
	public List<CatTipoPrestamo> buscarTodos() {
		List<CatTipoPrestamo> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList  = em.createNamedQuery("CatTipoPrestamo.findAll", this.modelClass)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener los tipos de préstamos...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
}
