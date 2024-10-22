package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetPercepcionEmpleado;

public class PercepcionEmpleadoDAO extends BaseDAO<DetPercepcionEmpleado, Integer> {
	
	private static Logger log = LogManager.getLogger(PercepcionEmpleadoDAO.class);

	public PercepcionEmpleadoDAO(Class<DetPercepcionEmpleado> modelClass) {
		super(modelClass);
	}
	
	public PercepcionEmpleadoDAO() {
		super(DetPercepcionEmpleado.class);
	}
	
	public List<DetPercepcionEmpleado> buscarPorEmpleado(Integer idEmpleado) {
		List<DetPercepcionEmpleado> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetEmpleadoPercepcion.buscarPorEmpleado", modelClass)
					.setParameter("idEmpleado", idEmpleado)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de percepciones del empleado " + idEmpleado, ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
