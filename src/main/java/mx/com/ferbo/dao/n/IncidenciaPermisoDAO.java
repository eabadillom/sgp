package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.util.SGPException;

/**Esta clase está dedicada a extraer información de la BBDD del conjunto de tablas
 * dedicadas a las incidencias (Permisos y Vacaciones).
 */
public class IncidenciaPermisoDAO extends BaseDAO<DetIncidencia, Integer> {
	
	private static Logger log = LogManager.getLogger(IncidenciaPermisoDAO.class);

	public IncidenciaPermisoDAO(Class<DetIncidencia> modelClass) {
		super(modelClass);
	}
	
	public IncidenciaPermisoDAO() {
		super(DetIncidencia.class);
	}
	
	public Optional<DetIncidencia> cargar(Integer id)
    throws SGPException {
    	
    	Optional<DetIncidencia> optional;
    	DetIncidencia model = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		
    		model = em.find(this.modelClass, id);
    		
    		model.getSolPermiso().getDiasPermiso().stream()
    		.forEach(item -> log.info("dia solicitado: {}", item.getId()));
    		
    		log.info("Id vacaciones: {}", model.getSolPermiso().getVacaciones().getIdVacaciones());
    		
    		optional = Optional.of(model);
    	} catch(Exception ex) {
    		log.warn("Problema para obtener la incidencia {}... {}", id, ex.getMessage());
    		optional = Optional.empty();
    	} finally {
    		this.close(em);
    	}
    	
    	return optional;
    }
	
	public List<DetIncidencia> buscarPermisos(Date periodoInicio, Date periodoFin) {
    	List<DetIncidencia> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetIncidencia.findPermisoByPeriodo", this.modelClass)
    				.setParameter("periodoInicio", periodoInicio)
    				.setParameter("periodoFin", periodoFin)
    				.getResultList()
    				;
    		
    		modelList.stream().forEach(
                    item -> {
                        log.debug("Permiso: {}", item.getSolPermiso());
                        if(item.getSolPermiso() != null)
                            log.debug("Dias permiso: {}", item.getSolPermiso().getDiasPermiso().size());
                    }
                );
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener los permisos de ausencia y/o permisos de vacaciones...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
	
	public List<DetIncidencia> buscarPermisos(Integer idEmpleado, Date periodoInicio, Date periodoFin) {
    	List<DetIncidencia> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetIncidencia.findPermisoByEmpleadoPeriodo", this.modelClass)
    				.setParameter("idEmpleado", idEmpleado)
    				.setParameter("periodoInicio", periodoInicio)
    				.setParameter("periodoFin", periodoFin)
    				.getResultList()
    				;
    		
    		modelList.stream().forEach(item -> log.debug("Permiso: {}", item.getSolPermiso().getIdSolicitud()));
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener los permisos de ausencia y/o permisos de vacaciones...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
	
	public List<DetIncidencia> buscarPor(DetEmpleado empleado, Date fechaInicio, Date fechaFin, CatEstatusIncidencia status, CatTipoIncidencia tipo) {
		List<DetIncidencia>       modelList = null;
		EntityManager             em        = null;
		TypedQuery<DetIncidencia> query     = null;
		
		try {
			em = this.getEntityManager();
			query = em.createQuery("SELECT i FROM DetIncidencia i INNER JOIN FETCH i.empleado e INNER JOIN FETCH i.solPermiso p "
					+ "WHERE i.empleado = :empleado AND i.estatusIncidencia = :status AND i.tipoIncidencia = :tipo "
					+ "AND p.fechaFin >= :fechaInicio AND p.fechaInicio <= :fechaFin "
					+ "ORDER BY i.solPermiso.fechaInicio ASC ", this.modelClass)
					.setParameter("empleado", empleado)
					.setParameter("status", status)
					.setParameter("tipo", tipo)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					;
			
			modelList = query.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de incidencias...", ex);
			modelList = new ArrayList<DetIncidencia>();
		} finally {
			this.close(em);
		}
		
		
		return modelList;
	}
	
}
