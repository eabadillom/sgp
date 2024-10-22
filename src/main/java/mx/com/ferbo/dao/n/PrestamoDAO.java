package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetPrestamo;

public class PrestamoDAO extends BaseDAO<DetPrestamo, Integer> {
	
	private static Logger log = LogManager.getLogger(PrestamoDAO.class);

	public PrestamoDAO(Class<DetPrestamo> modelClass) {
		super(modelClass);
	}
	
	public PrestamoDAO() {
		super(DetPrestamo.class);
	}
	
	public List<DetPrestamo> buscar(Integer idEmpleado) {
		List<DetPrestamo> list = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			list = em.createNamedQuery("DetPrestamo.findByEmpleado", modelClass)
					.setParameter("idEmpleado", idEmpleado)
					.getResultList()
					;
			
			for(DetPrestamo model : list) {
				log.debug("Tipo prestamo: {}", model.getTipoPrestamo().getTipoPrestamo());
				log.debug("Periodicidad: {}", model.getPeriodicidadPago().getPeriodicidad());
				log.debug("Tipo deduccion{}", model.getTipoPrestamo().getTipoDeduccion().getClave());
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de préstamos...", ex);
		} finally {
			this.close(em);
		}
		
		return list;
	}

}
