package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatParametro;

public class ParametroDAO extends BaseDAO<CatParametro, Integer> {
	
	private static Logger log = LogManager.getLogger(ParametroDAO.class);

	public ParametroDAO(Class<CatParametro> modelClass) {
		super(modelClass);
	}
	
	public CatParametro buscarPorClave(String clave) {
		CatParametro model = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("CatParametro.buscarPorClave", modelClass)
					.setParameter("clave", clave)
					.getSingleResult()
					;
		} catch(Exception ex) {
			log.warn("Problema para obtener el parametro: {}", clave);
		} finally {
			close(em);
		}
		return model;
	}

}
