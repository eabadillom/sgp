package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoRegimen;

public class TipoRegimenDAO extends BaseDAO<CatTipoRegimen, String> {
	
	private static Logger log = LogManager.getLogger(TipoRegimenDAO.class);

	public TipoRegimenDAO(Class<CatTipoRegimen> modelClass) {
		super(modelClass);
	}
	
	public List<CatTipoRegimen> buscarTodos() {
		List<CatTipoRegimen> modelList = null;
		EntityManager em = null;
		try {
			em = getEntityManager();
			modelList = em.createNamedQuery("CatTipoRegimen.findAll", CatTipoRegimen.class)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de tipos de régimen...", ex);
		} finally {
			close(em);
		}
		return modelList;
	}

}
