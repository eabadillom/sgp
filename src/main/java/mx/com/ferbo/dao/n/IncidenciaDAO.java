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
                if (aux.getIdSolPermiso() != null) {
                    log.trace("DetSolicitudPermiso: {}", aux.getIdSolPermiso().toString());
                }
                if (aux.getIdSolPrenda() != null) {
                    log.trace("DetSolicitudPrenda: {}", aux.getIdSolPrenda().toString());
                }
                if (aux.getIdSolArticulo() != null) {
                    log.trace("DetSolicitudArticulo: {}", aux.getIdSolArticulo().toString());
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

    public synchronized void eliminaIncidenciaPorIdEmpleado(Integer idEmpleado) throws SGPException {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Query resultado = em.createQuery("DELETE FROM DetIncidencia di WHERE di.idEmpleado.idEmpleado = :idEmpleado");
            resultado.setParameter("idEmpleado", idEmpleado);
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
