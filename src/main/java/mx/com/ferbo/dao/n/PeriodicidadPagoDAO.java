package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPeriodicidadPago;

public class PeriodicidadPagoDAO extends BaseDAO<CatPeriodicidadPago, String> {
	
	private static Logger log = LogManager.getLogger(PeriodicidadPagoDAO.class);

	public PeriodicidadPagoDAO(Class<CatPeriodicidadPago> modelClass) {
		super(modelClass);
	}
	
	public List<CatPeriodicidadPago> buscarTodos() {
		List<CatPeriodicidadPago> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatPeriodicidadPago.buscarTodos", modelClass)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de periodicidades de pago...", ex);
		} finally {
			this.close(em);
		}
		return modelList;
	}
	
	public List<CatPeriodicidadPago> buscarActivos(Date fechaVigencia) {
		List<CatPeriodicidadPago> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatPeriodicidadPago.buscarTodosActivos", modelClass)
					.setParameter("vigenciaFin", fechaVigencia)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de periodicidades de pago...", ex);
		} finally {
			this.close(em);
		}
		return modelList;
	}
}
