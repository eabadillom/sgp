package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author alberto
 */
public class IncidenciaDAO extends BaseDAO<DetIncidencia, Integer> {

    private static Logger log = LogManager.getLogger(DetIncidencia.class);

    public IncidenciaDAO(Class<DetIncidencia> modelClass) {
        super(modelClass);
    }

    public IncidenciaDAO() {
        super(DetIncidencia.class);
    }
    
    public List<DetIncidencia> buscarPorPerido(Date fechaInicio, Date fechaFin) {
        List<DetIncidencia> modelList = null;
        EntityManager emSGP = null;
        
        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetIncidencia.findByPeriodo", DetIncidencia.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList()
                    ;
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de incidencias...", ex);
        } finally {
            close(emSGP);
        }
        
        return modelList;
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

    public List<DetIncidencia> buscarPorIdEmpleado(Integer idEmpleado) {
        List<DetIncidencia> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetIncidencia.findByIdEmpleado", DetIncidencia.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de incidencias por id empleado...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }

    public List<DetIncidencia> buscarPorIdEmpleadoPrenda(Integer idEmpleado) {
        List<DetIncidencia> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetIncidencia.findByIdEmpleadoPrenda", DetIncidencia.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de incidencias por id empleado...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }
    
    public DetIncidencia buscarPorArticulo(Integer idEmpleado, Integer idSolicitudArticulo)
    {
        DetIncidencia model = null;
        EntityManager emSGP = null;
        
        try {
            emSGP = getEntityManager();
            model = emSGP.createNamedQuery("DetIncidencia.findByArticulo", DetIncidencia.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .setParameter("idSolicitud", idSolicitudArticulo)
                    .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema para obtener la incidencia por id empleado...", ex);
        } finally {
            close(emSGP);
        }
        
        return model;
    }
    
    public DetIncidencia buscarPorPermiso(Integer idEmpleado, Integer idSolicitudPrenda)
    {
        DetIncidencia model = null;
        EntityManager emSGP = null;
        
        try {
            emSGP = getEntityManager();
            model = emSGP.createNamedQuery("DetIncidencia.findByPermiso", DetIncidencia.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .setParameter("idSolicitud", idSolicitudPrenda)
                    .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema para obtener la incidencia por id empleado...", ex);
        } finally {
            close(emSGP);
        }
        
        return model;
    }
    
    public DetIncidencia buscarPorPrenda(Integer idEmpleado, Integer idSolicitudPrenda)
    {
        DetIncidencia model = null;
        EntityManager emSGP = null;
        
        try {
            emSGP = getEntityManager();
            model = emSGP.createNamedQuery("DetIncidencia.findByPrenda", DetIncidencia.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .setParameter("idSolicitud", idSolicitudPrenda)
                    .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema para obtener la incidencia por id empleado...", ex);
        } finally {
            close(emSGP);
        }
        
        return model;
    }

    public List<DetIncidencia> buscarPorIdEmpleadoArticulo(Integer idEmpleado) {
        List<DetIncidencia> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetIncidencia.findByIdEmpleadoArticulo", DetIncidencia.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de incidencias por id empleado...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }

    public List<DetIncidencia> buscarPorIdEmpleadoPermiso(Integer idEmpleado) {
        List<DetIncidencia> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetIncidencia.findByIdEmpleadoPermiso", DetIncidencia.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de incidencias por id empleado...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }
    
    public synchronized void eliminaIncidenciaPorIdEmpleado(Integer idIncidencia, Integer idEmpleado, Integer idPermiso) throws SGPException {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Query resultado = em.createQuery("DELETE FROM DetIncidencia di WHERE di.idIncidencia = :idIncidencia AND di.empleado.idEmpleado = :idEmpleado AND di.solPermiso.idSolicitud = :idPermiso");
            resultado.setParameter("idIncidencia", idIncidencia);
            resultado.setParameter("idEmpleado", idEmpleado);
            resultado.setParameter("idPermiso", idPermiso);
            resultado.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            rollback(em);
            throw new SGPException("Contacte con el administrador de sistemas");
        } finally {
            close(em);
        }
    }
    
    public List<DetIncidencia> buscarPorStatusTipo(CatEstatusIncidencia status, CatTipoIncidencia tipo) {
    	List<DetIncidencia> modelList = null;
		EntityManager em = null;
		TypedQuery<DetIncidencia> query = null;
		
		try {
			em = this.getEntityManager();
			query = em.createQuery("SELECT i FROM DetIncidencia i WHERE i.estatusIncidencia = :status AND i.tipoIncidencia = :tipo ", modelClass);
			query.setParameter("status", status);
			query.setParameter("tipo", tipo);
			
			modelList = query.getResultList();
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de incidencias...", ex);
			modelList = new ArrayList<DetIncidencia>();
		} finally {
			this.close(em);
		}
		
		return modelList;
    }

	public List<DetIncidencia> buscarPorStatusTipo(DetEmpleado empleado, CatEstatusIncidencia status, CatTipoIncidencia tipo) {
		List<DetIncidencia> modelList = null;
		EntityManager em = null;
		TypedQuery<DetIncidencia> query = null;
		
		try {
			em = this.getEntityManager();
			query = em.createQuery("SELECT i FROM DetIncidencia i "
					+ "WHERE i.empleado = :empleado AND i.estatusIncidencia = :status AND i.tipoIncidencia = :tipo "
					+ "ORDER BY i.solPermiso.fechaInicio ASC", modelClass);
			query.setParameter("empleado", empleado);
			query.setParameter("status", status);
			query.setParameter("tipo", tipo);
			
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