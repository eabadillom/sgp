package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTipoSolicitud;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoSolicitudDAO extends BaseDAO<CatTipoSolicitud, Integer>
{
    private static Logger log = LogManager.getLogger(TipoSolicitudDAO.class);

    public TipoSolicitudDAO(Class<CatTipoSolicitud> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoSolicitudDAO() 
    {
        super(CatTipoSolicitud.class);
    }
    
    public List<CatTipoSolicitud> buscarTodos()
    {
        List<CatTipoSolicitud> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatTipoSolicitud.findAll", CatTipoSolicitud.class)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener el listado de tipos de solicitud...", ex);
        }finally 
        {
           close(em);
        }
        
        return modelList;
    }
    
    public List<CatTipoSolicitud> buscarActivos()
    {
        List<CatTipoSolicitud> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatTipoSolicitud.findByActive", CatTipoSolicitud.class)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener el listado de tipos de solicitud...", ex);
        }finally 
        {
           close(em);
        }
        
        return modelList;
    }
    
    public List<CatTipoSolicitud> buscarPermisosyVacaciones(String permiso, String vacaciones)
    {
        List<CatTipoSolicitud> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatTipoSolicitud.findByPermisoYVacaciones", CatTipoSolicitud.class)
                .setParameter("clavePermiso", permiso)
                .setParameter("claveVacaciones", vacaciones)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener el listado de tipos de solicitud...", ex);
        }finally 
        {
           close(em);
        }
        
        return modelList;
    }
    
}
