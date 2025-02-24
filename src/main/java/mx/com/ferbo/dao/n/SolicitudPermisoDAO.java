
package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

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
            
            for(DetSolicitudPermiso aux: modelList)
            {
                log.trace("Info Solicitud: {}", aux.toString());
                log.trace("Info Cat Tipo Solicitud: {}", aux.getIdTipoSolicitud().toString());
            } 
            
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
    
    public List<DetSolicitudPermiso> buscarPorTipoSolicitud(Integer idEmpleado, String permiso, String vacaciones)
    {
        List<DetSolicitudPermiso> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetSolicitudPermiso.findByTipoSolicitud", DetSolicitudPermiso.class)
                .setParameter("idEmp", idEmpleado)
                .setParameter("clavePermiso", permiso)
                .setParameter("claveVacaciones", vacaciones)
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
    
    public List<DetSolicitudPermiso> buscarPorIdEmpleadoClaveSolicitud(Integer idEmpleado, String clave)
    {
        List<DetSolicitudPermiso> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetSolicitudPermiso.findByClave", DetSolicitudPermiso.class)
                .setParameter("idEmp", idEmpleado)
                .setParameter("clave", clave)
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
    
    public List<DetSolicitudPermiso> buscarPorIdEmpleadoFechasClave(Integer idEmpleado, Date fechaIni, Date fechaFin, String clave1, String clave2)
    {
        List<DetSolicitudPermiso> modellist = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modellist = em.createNamedQuery("DetSolicitudPermiso.findByCriterios", DetSolicitudPermiso.class)
                .setParameter("idEmp", idEmpleado)
                .setParameter("fechaInicio", fechaIni, TemporalType.TIMESTAMP)
                .setParameter("fechaFin", fechaFin, TemporalType.TIMESTAMP)
                .setParameter("clave1", clave1)
                .setParameter("clave2", clave2)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener la solicitud de permiso: {}", ex.getMessage());
        } finally 
        {
            this.close(em);
        }
        
        return modellist;
    }
    
    public List<DetSolicitudPermiso> buscarPorPeriodo (Integer idEmpleado, Date fechaIni, Date fechaFin)
    {
        List<DetSolicitudPermiso> modellist = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modellist = em.createNamedQuery("DetSolicitudPermiso.findByPeriodo", DetSolicitudPermiso.class)
                .setParameter("idEmp", idEmpleado)
                .setParameter("fechaInicio", fechaIni, TemporalType.TIMESTAMP)
                .setParameter("fechaFin", fechaFin, TemporalType.TIMESTAMP)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener la solicitud de permiso: {}", ex.getMessage());
        } finally 
        {
            this.close(em);
        }
        
        return modellist;
    }
    
}
