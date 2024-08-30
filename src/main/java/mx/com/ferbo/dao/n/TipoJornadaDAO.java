package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoJornada;

public class TipoJornadaDAO extends BaseDAO<CatTipoJornada, String> {
	
	private static Logger log = LogManager.getLogger(TipoJornadaDAO.class);

	public TipoJornadaDAO(Class<CatTipoJornada> modelClass) {
		super(modelClass);
	}
	
	public List<CatTipoJornada> buscarTodos() {
		List<CatTipoJornada> modelList = null;
		EntityManager em = null;
		
		try {
			em = getEntityManager();
			modelList = em.createNamedQuery("CatTipoJornada.findAll", CatTipoJornada.class)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de tipos de contrato...", ex);
		} finally {
			close(em);
		}
		
		return modelList;
	}
}
