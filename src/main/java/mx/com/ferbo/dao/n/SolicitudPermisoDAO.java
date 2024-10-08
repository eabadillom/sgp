
package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetSolicitudPermiso;


/**
 *
 * @author alberto
 */
public class SolicitudPermisoDAO extends BaseDAO<DetSolicitudPermiso, Integer>
{
    private static Logger log = LogManager.getLogger(SolicitudPermisoDAO.class);
    
    public SolicitudPermisoDAO(Class<DetSolicitudPermiso> modelClass) 
    {
        super(modelClass);
    }
    
    public SolicitudPermisoDAO() 
    {
        super(DetSolicitudPermiso.class);
    }
    
    public List<DetSolicitudPermiso> buscarPorIdEmpleado(Integer idEmpleado)
    {
        List<DetSolicitudPermiso> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetSolicitudPermiso.findByIdEmp", DetSolicitudPermiso.class)
                .setParameter("idEmp", idEmpleado)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener la lista de solicitud de permisos...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public List<DetSolicitudPermiso> buscarTodos()
    {
        List<DetSolicitudPermiso> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetSolicitudPermiso.findAll", DetSolicitudPermiso.class)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener la lista de solicitud de permisos...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
}
