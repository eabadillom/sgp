package mx.com.ferbo.dao.n;

import java.util.Optional;

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
	
	public EmpleadoFotoDAO() {
		super(DetEmpleadoFoto.class);
	}
	
	public Optional<DetEmpleadoFoto> buscar(Integer idEmpleado) {
		Optional<DetEmpleadoFoto> optional = null;
		DetEmpleadoFoto model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetEmpleadoFoto.findByIdEmpleado", modelClass)
					.setParameter("idEmpleado", idEmpleado)
					.getSingleResult()
					;
			optional = Optional.of(model);
		} catch(NoResultException ex) {
			log.warn("Problema para obtener la fotografía del empleado con id {}", idEmpleado);
			optional = Optional.empty();
		} catch(Exception ex) {
			log.error("Problema para obtener la foto del empleado con id {}...\n{}", idEmpleado, ex);
			optional = Optional.empty();
		} finally {
			this.close(em);
		}
		
		return optional;
	}

}
