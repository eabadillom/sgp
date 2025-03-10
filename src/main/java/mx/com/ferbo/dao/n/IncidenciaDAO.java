package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public List<DetIncidencia> buscarTodos() {
        List<DetIncidencia> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetIncidencia.findAll", DetIncidencia.class)
                    .getResultList();

            for (DetIncidencia aux : modelList) {
                if (aux.getSolPermiso() != null) {
                    log.trace("DetSolicitudPermiso: {}", aux.getSolPermiso().toString());
                }
                if (aux.getSolPrenda() != null) {
                    log.trace("DetSolicitudPrenda: {}", aux.getSolPrenda().toString());
                }
                if (aux.getSolArticulo() != null) {
                    log.trace("DetSolicitudArticulo: {}", aux.getSolArticulo().toString());
                }
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el listado de incidencias...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
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
}
