package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoContrato;

public class TipoContratoDAO extends BaseDAO<CatTipoContrato, String> {
	private static Logger log = LogManager.getLogger(TipoContratoDAO.class);

	public TipoContratoDAO(Class<CatTipoContrato> modelClass) {
		super(modelClass);
	}
	
	public List<CatTipoContrato> buscarTodos() {
		List<CatTipoContrato> modelList = null;
		EntityManager em = null;
		
		try {
			em = getEntityManager();
			modelList = em.createNamedQuery("CatTipoContrato.findAll", CatTipoContrato.class)
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
