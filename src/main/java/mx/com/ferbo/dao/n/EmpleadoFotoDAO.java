package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
			
		} catch(NoResultException ex) {
			log.warn("No hay registro de foto para el empleado {}", numeroEmpleado);
		} catch(Exception ex) {
			log.error("Problema para obtener la foto del empleado " + numeroEmpleado, ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}

}
