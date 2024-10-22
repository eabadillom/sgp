package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class TipoPercepcionDAO extends BaseDAO<CatTipoPercepcion, String> {
	
	private Logger log = LogManager.getLogger(TipoPercepcionDAO.class);

	public TipoPercepcionDAO(Class<CatTipoPercepcion> modelClass) {
		super(modelClass);
	}
	
	public TipoPercepcionDAO() {
		super(CatTipoPercepcion.class);
	}
	
	public List<CatTipoPercepcion> buscarTodos() {
		List<CatTipoPercepcion> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatTipoPercepcion.findByAll", modelClass)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de tipos de percepción...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
}
