package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoDeduccion;

public class TipoDeduccionDAO extends BaseDAO<CatTipoDeduccion, String> {
	private static Logger log = LogManager.getLogger(TipoDeduccionDAO.class);

	public TipoDeduccionDAO(Class<CatTipoDeduccion> modelClass) {
		super(modelClass);
	}
	
	public TipoDeduccionDAO() {
		super(CatTipoDeduccion.class);
	}
	
	public List<CatTipoDeduccion> buscarTodos() {
		List<CatTipoDeduccion> modelList = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatTipoDeduccion.findByAll", modelClass)
					.getResultList()
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener los tipos de deducción...", ex);
		} finally {
			this.close(em);
		}
			
		return modelList;
	}

}
