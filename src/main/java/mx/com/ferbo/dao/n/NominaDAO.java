package mx.com.ferbo.dao.n;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetVacaciones;
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
			model.setVacaciones(new ArrayList<DetVacaciones>());
			model.getDeducciones().stream().forEach(item -> log.debug("Deduccion: {}", item.getId()));
			model.getOtrosPagos().stream().forEach(item -> log.debug("Otro pago: {}", item.getId()));
			model.getIncidencias().stream().forEach(item -> log.debug("Incidencia: {}", item.getId()));
			model.getNominaVacaciones().stream().forEach(item -> log.debug("Nomina vacaciones: {}", item.getId()));
			
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
			
			modelList.stream().forEach(item -> {
				item.getPercepciones().forEach(p -> log.debug("Percepcion: {}", p.getId()));
				item.getDeducciones().forEach(d -> log.debug("Deduccion: {}", d.getId()));
				item.getOtrosPagos().forEach(o -> log.debug("Otro pago: {}", o.getId()));
			});
			
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
			
			model.getConceptos().stream().forEach(item -> log.info("Concepto: {}", item.getId()));
			model.getPercepciones().stream().forEach(item -> log.info("Percepcion: {}", item.getId()));
			model.getOtrosPagos().stream().forEach(item -> log.info("Otro pago: {}", item.getId()));
			model.getDeducciones().stream().forEach(item -> log.info("Deduccion: {}", item.getId()));
			model.getIncidencias().stream().forEach(item -> log.info("Incidencia: {}", item.getId()));
			
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
