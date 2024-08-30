package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetBiometrico;

public class BiometricoDAO extends BaseDAO<DetBiometrico, Integer> {
	
	private static Logger log = LogManager.getLogger(BiometricoDAO.class);

	public BiometricoDAO(Class<DetBiometrico> modelClass) {
		super(modelClass);
	}
	
	public DetBiometrico consultaBiometricoByNumEmpleado(String numeroEmpleado) {
		DetBiometrico model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetBiometrico.findByNumEmpl", modelClass)
					.setParameter("numEmpl", numeroEmpleado)
					.getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener el biometrico del empleado " + numeroEmpleado, ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}

}
