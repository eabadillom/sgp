package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetEmpleadoFoto;

public class EmpleadoFotoDAO extends BaseDAO<DetEmpleadoFoto, Integer> {
	
	private static Logger log = LogManager.getLogger(EmpleadoFotoDAO.class);

	public EmpleadoFotoDAO(Class<DetEmpleadoFoto> modelClass) {
		super(modelClass);
	}
	
	public DetEmpleadoFoto buscar(String numeroEmpleado) {
		DetEmpleadoFoto model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetEmpleadoFoto.findByNumeroEmpleado", modelClass)
					.setParameter("numeroEmpleado", numeroEmpleado)
					.getSingleResult()
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener la foto del empleado " + numeroEmpleado, ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}

}
