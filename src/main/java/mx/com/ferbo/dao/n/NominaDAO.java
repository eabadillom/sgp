package mx.com.ferbo.dao.n;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaConcepto;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

public class NominaDAO extends BaseDAO<DetNomina, Integer> {
	
	private static Logger log = LogManager.getLogger(NominaDAO.class);

	public NominaDAO(Class<DetNomina> modelClass) {
		super(modelClass);
	}
	
	public NominaDAO() {
		super(DetNomina.class);
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
	
	public DetNomina buscarPorPeriodoEmpleado(LocalDate periodoInicio, LocalDate periodoFin, String rfc) {
		DetNomina model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetNomina.findByPeriodoRfc", modelClass)
					.setParameter("periodoInicio", periodoInicio)
					.setParameter("periodoFin", periodoFin)
					.setParameter("rfc", rfc)
					.getSingleResult()
					;
			log.debug("periodo inicio {}, periodo fin {}, rfc {}");
			
			model.getConceptos().stream().forEach(item -> log.debug("Concepto: {}", item.getId()));
			
			model.getPercepciones().stream().forEach(item -> log.debug("Percepcion: {}", item.getId()));
			
			model.getDeducciones().stream().forEach(item -> log.debug("Deduccion: {}", item.getId()));
			
			model.getOtrosPagos().stream().forEach(item -> log.debug("Otro pago: {}", item.getId()));
			
		} catch(NoResultException ex) {
			log.warn("Problema para obtener la lista de nomina del periodo solicitado...", ex.getMessage());
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de nomina del periodo solicitado...", ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}
	
	public DetNomina buscar(String rfcEmisor, String tipoNomina, Integer anio, Integer periodo, String rfcReceptor) {
		DetNomina model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetNomina.findByEmisorTipoNominaAnioPeriodoReceptor", this.modelClass)
					.setParameter("rfcEmisor", rfcEmisor)
					.setParameter("tipoNomina", tipoNomina)
					.setParameter("anio", anio)
					.setParameter("periodo", periodo)
					.setParameter("rfcReceptor", rfcReceptor)
					.getSingleResult()
					;
			
		} catch(NoResultException ex) {
			log.warn("No hay resultados para el periodo de nomina solicitado...", ex.getMessage());
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de nomina del periodo solicitado...", ex);
		} finally {
			this.close(em);
		}
		
		
		return model;
	}
	
	public List<DetNomina> buscarNominasDelMesPorNumeroPeriodo(String tipoNomina, String periodicidadPago, Integer ejercicio, Integer periodoInicio, Integer periodoFin, String rfc) {
		List<DetNomina> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetNomina.findNominasDelMesPorNumeroPeriodo", modelClass)
					.setParameter("tipoNomina", tipoNomina)
					.setParameter("periodicidad", periodicidadPago)
					.setParameter("ejercicio", ejercicio)
					.setParameter("periodoInicio", periodoInicio)
					.setParameter("periodoFin", periodoFin)
					.setParameter("rfc", rfc)
					.getResultList()
					;
			log.debug("periodo inicio {}, periodo fin {}, rfc {}");
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de nomina del periodo solicitado...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
	
	public List<DetNomina> buscarNominasDelMesPorFechaPeriodo(String tipoNomina, String periodicidadPago, Integer ejercicio, LocalDate periodoInicio, LocalDate periodoFin, String rfc) {
		List<DetNomina> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetNomina.findNominasDelMesPorFechaPeriodo", modelClass)
					.setParameter("tipoNomina", tipoNomina)
					.setParameter("ejercicio", ejercicio)
					.setParameter("periodoInicio", periodoInicio)
					.setParameter("periodoFin", periodoFin)
					.setParameter("rfc", rfc)
					.getResultList()
					;
			log.debug("periodo inicio {}, periodo fin {}, rfc {}");
			
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
				log.info("Concepto: {}", concepto.getId());
			}
			
			for(DetNominaPercepcion percepcion : model.getPercepciones()) {
				log.info("Percepcion: {} - {}", percepcion.getId(), percepcion.getClave());
			}
			
			for(DetNominaOtroPago otroPago: model.getOtrosPagos()) {
				log.info("Otro pago: {} - {}", otroPago.getId(), otroPago.getClave());
			}
			
			for(DetNominaDeduccion deduccion : model.getDeducciones()) {
				log.info("Deduccion: {} - ", deduccion.getId(), deduccion.getClave());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la información de nomina...", ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}
	
	@Override
	public synchronized void eliminar(DetNomina model) throws SGPException {
		EntityManager em = null;
		try {
			log.info("Eliminando objeto: {} ........", model);
			em = getEntityManager();
			em.getTransaction().begin();
			
			model = em.contains(model) ? model : em.merge(model);
			
			model.getEmisor().setNomina(null);
			model.getReceptor().setNomina(null);
//			model.getConceptos().forEach(item -> item.setNomina(null));
//			model.getPercepciones().forEach(item -> item.getKey().setNomina(null));
//			model.getOtrosPagos().forEach(item -> item.getKey().setNomina(null));
//			model.getDeducciones().forEach(item -> item.getKey().setNomina(null));
			
			em.remove(model);
			em.getTransaction().commit();
			log.info("Objeto eliminado correctamente: {}", model);
		} catch(Exception ex) {
			this.rollback(em);
			log.error("Probleam para eliminar el objeto: " + model, ex);
                        throw new SGPException("Error al eliminar en la base de datos.");
		} finally {
			this.close(em);
		}
	}
}
