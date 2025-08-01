package mx.com.ferbo.dao.n.imss;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetRegistroIncapacidad;
import mx.com.ferbo.model.imss.DetIncapacidad;

/**
 *
 * @author alberto
 */
public class IncapacidadIMSSDAO extends BaseDAO<DetIncapacidad, Integer>
{
    private static Logger log = LogManager.getLogger(DetIncapacidad.class);

	public IncapacidadIMSSDAO(Class<DetIncapacidad> modelClass) {
		super(modelClass);
	}
    
	public IncapacidadIMSSDAO() {
		super(DetIncapacidad.class);
	}
    
	public List<DetIncapacidad> buscarTodos() {
		List<DetIncapacidad> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetIncapacidad.findAll", DetIncapacidad.class).getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener la lista de registro de incapacidades {}", ex.getMessage());
		} finally {
			this.close(em);
		}

		return modelList;
	}
    
	public List<DetIncapacidad> buscarPorEmpleado(Integer idEmpleado) {
		List<DetIncapacidad> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetIncapacidad.findByEmpleado", DetIncapacidad.class)
					.setParameter("idEmpleado", idEmpleado).getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener la lista de registro de incapacidades {}", ex.getMessage());
		} finally {
			this.close(em);
		}

		return modelList;
	}
    
	public List<DetIncapacidad> buscarPorIncapacidadClave(String clave) {
		List<DetIncapacidad> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetIncapacidad.findByIncapacidadClave", DetIncapacidad.class)
					.setParameter("clave", clave)
					.getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener la lista de registro de incapacidades {}", ex.getMessage());
		} finally {
			this.close(em);
		}

		return modelList;
	}
    
	public List<DetIncapacidad> buscarPorEstatus(String clave) {
		List<DetIncapacidad> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetIncapacidad.findByEstatus", DetIncapacidad.class)
					.setParameter("clave", clave)
					.getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener la lista de registro de incapacidades {}", ex.getMessage());
		} finally {
			this.close(em);
		}

		return modelList;
	}
    
    public List<DetIncapacidad> buscarPorPeriodo(Date fechaInicio, Date fechaFin) {
    	List<DetIncapacidad> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetIncapacidad.findByPeriodo1", this.modelClass)
    				.setParameter("fechaInicio", fechaInicio)
    				.setParameter("fechaFin", fechaFin)
    				.getResultList()
    				;
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de incapacidades...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
    
	public List<DetIncapacidad> buscarPorPeriodo(Integer idEmpleado, Date fechaInicio, Date fechaFin, String clave) {
		List<DetIncapacidad> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetIncapacidad.findByPeriodo", DetIncapacidad.class)
					.setParameter("idEmpleado", idEmpleado)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.setParameter("clave", clave).getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener la lista de registros de incapacidades {}", ex.getMessage());
		} finally {
			this.close(em);
		}

		return modelList;
	}
    
	public DetIncapacidad buscarPorEmpleadoUltimoPeriodo(Integer idEmpleado, Date fechaInicio) {
		DetIncapacidad model = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetIncapacidad.findByEmpleadoUltimoPeriodo", DetIncapacidad.class)
					.setParameter("idEmpleado", idEmpleado)
					.setParameter("fechaInicio", fechaInicio, TemporalType.TIMESTAMP).setMaxResults(1)
					.getSingleResult();
		} catch (Exception ex) {
			log.error("Error al obtener el registro de incapacidad {}", ex.getMessage());
		} finally {
			this.close(em);
		}

		return model;
	}
    
	public List<DetIncapacidad> buscarPorParametros(Integer idEmpleado, Date fechaInicio, Date fechaFinal) {
		List<DetIncapacidad> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetIncapacidad.findByParametros", DetIncapacidad.class)
					.setParameter("idEmpleado", idEmpleado)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFinal).getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener la lista de registros de incapacidades {}", ex.getMessage());
		} finally {
			this.close(em);
		}

		return modelList;
	}
	
	public Optional<DetIncapacidad> cargar(Integer id){
		Optional<DetIncapacidad> optional;
		DetIncapacidad model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.find(this.modelClass, id);
			
			for(DetRegistroIncapacidad item : model.getRegistrosIncapacidad()) {
				log.debug("Id registro incapacidad: {}", item.getId());
				log.debug("Id registro: {}",  item.getRegistro());
			}
//			if(model.getRegistrosIncapacidad() != null || model.getRegistrosIncapacidad().size() > 0) {
//				model.getRegistrosIncapacidad()
//				.forEach(item -> {
//					
//				});
//			}
			
			optional = Optional.of(model);
			
		} catch(Exception ex) {
			log.error("Problema para obtener la incapacidad con id: {}...\n{}", id, ex);
			optional = Optional.empty();
		} finally {
			this.close(em);
		}
		
		return optional;
	}
    
}
