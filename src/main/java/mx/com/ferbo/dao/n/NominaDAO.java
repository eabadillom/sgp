package mx.com.ferbo.dao.n;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaConcepto;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;

public class NominaDAO extends BaseDAO<DetNomina, Integer> {
	
	private static Logger log = LogManager.getLogger(NominaDAO.class);

	public NominaDAO(Class<DetNomina> modelClass) {
		super(modelClass);
	}
	
	public List<DetNomina> buscarPorPeriodo(LocalDate periodoInicio, LocalDate periodoFin) {
		List<DetNomina> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetNomina.findByPeriodo", modelClass)
					.setParameter("periodoInicio", periodoInicio)
					.setParameter("periodoFin", periodoFin)
					.getResultList()
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de nomina del periodo solicitado...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
	
	@Override
	public DetNomina buscarPorId(Integer id) {
		DetNomina model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.find(modelClass, id);
			
			for(DetNominaConcepto concepto : model.getConceptos()) {
				log.info("Concepto: {}", concepto.getKey().getId());
			}
			
			for(DetNominaPercepcion percepcion : model.getPercepciones()) {
				log.info("Percepcion: {} - {}", percepcion.getKey().getId(), percepcion.getClave());
			}
			
			for(DetNominaOtroPago otroPago: model.getOtrosPagos()) {
				log.info("Otro pago: {} - {}", otroPago.getKey().getId(), otroPago.getClave());
			}
			
			for(DetNominaDeduccion deduccion : model.getDeducciones()) {
				log.info("Deduccion: {} - ", deduccion.getKey().getId(), deduccion.getClave());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la información de nomina...", ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}
}
