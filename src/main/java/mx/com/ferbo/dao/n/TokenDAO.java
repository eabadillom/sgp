package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetToken;

public class TokenDAO extends BaseDAO<DetToken, Integer> {
	
	private static Logger log = LogManager.getLogger(TokenDAO.class);

	public TokenDAO(Class<DetToken> modelClass) {
		super(modelClass);
	}
	
	public DetToken buscarPorToken(String token) {
		DetToken model = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetToken.findByToken",DetToken.class)
					.setParameter("token", token)
					.getSingleResult()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el token...", ex);
		} finally {
			this.close(em);
		}
		return model;
	}

}
