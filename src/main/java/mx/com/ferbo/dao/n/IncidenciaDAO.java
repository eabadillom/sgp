package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetIncidencia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class IncidenciaDAO extends BaseDAO<DetIncidencia, Integer>
{
    private static Logger log = LogManager.getLogger(DetIncidencia.class);
    
    public IncidenciaDAO(Class<DetIncidencia> modelClass) {
        super(modelClass);
    }
    
    public IncidenciaDAO()
    {
        super(DetIncidencia.class);
    }
    
    public List<DetIncidencia> buscarTodos()
    {
        List<DetIncidencia> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetIncidencia.findAll", DetIncidencia.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de incidencias...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }
    
    public List<DetIncidencia> buscarPorIdEmpleado(Integer idEmpleado)
    {
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
    
    public List<DetIncidencia> buscarPorIdEmpleadoPrenda(Integer idEmpleado)
    {
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
    
    public List<DetIncidencia> buscarPorIdEmpleadoArticulo(Integer idEmpleado)
    {
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
    
    public List<DetIncidencia> buscarPorIdEmpleadoPermiso(Integer idEmpleado)
    {
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
    
}
