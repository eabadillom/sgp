package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetEmpleadoConfiguracion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class EmpleadoConfiguracionDAO extends BaseDAO<DetEmpleadoConfiguracion, Integer>
{
    private static Logger log = LogManager.getLogger(EmpleadoConfiguracionDAO.class);
    
    public EmpleadoConfiguracionDAO(Class<DetEmpleadoConfiguracion> modelClass) 
    {
        super(modelClass);
    }
    
    public EmpleadoConfiguracionDAO()
    {
        super(DetEmpleadoConfiguracion.class);
    }
    
    public DetEmpleadoConfiguracion buscarPorIdEmpleado(Integer idEmpleado)
    {
        DetEmpleadoConfiguracion model = null;
        EntityManager em = null;
        
        try {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetEmpleadoConfiguracion.findByEmpleado", DetEmpleadoConfiguracion.class)
                .setParameter("idEmpleado", idEmpleado)
                .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema para obtener las configuraciones del empleado...", ex);
        } finally {
            this.close(em);
        }
        
        return model;
    }
    
    public List<DetEmpleadoConfiguracion> buscarTodos()
    {
        List<DetEmpleadoConfiguracion> modelList = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetEmpleadoConfiguracion.findAll", DetEmpleadoConfiguracion.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener la lista de configuraciones de los empleados...", ex);
        } finally {
            this.close(em);
        }

        return modelList;
    }
    
    public List<DetEmpleadoConfiguracion> buscarPorRetardos(Boolean retardo)
    {
        List<DetEmpleadoConfiguracion> model = null;
        EntityManager em = null;
        
        try {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetEmpleadoConfiguracion.findByRetardos", DetEmpleadoConfiguracion.class)
                .setParameter("retardo", retardo)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener la lista de configuraciones de los empleados...", ex);
        } finally {
            this.close(em);
        }
        
        return model;
    }
    
}
