package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstatusSolicitud;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class EstatusSolicitudDAO extends BaseDAO<CatEstatusSolicitud, Integer>
{
    private static Logger log = LogManager.getLogger(EstatusSolicitudDAO.class);
    
    public EstatusSolicitudDAO(Class<CatEstatusSolicitud> modelClass) 
    {
        super(modelClass);
    }
    
    public EstatusSolicitudDAO()
    {
        super(CatEstatusSolicitud.class);
    }
    
    public CatEstatusSolicitud buscarPorClave(String clave)
    {
        CatEstatusSolicitud model = null;
        EntityManager em = null;
        
        try 
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatEstatusSolicitud.findByClave", CatEstatusSolicitud.class)
                .setParameter("clave", clave)
                .getSingleResult();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de estatus", ex);
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }
    
    public List<CatEstatusSolicitud> buscarTodos()
    {
        List<CatEstatusSolicitud> listModel = null;
        EntityManager em = null;
        
        try 
        {
            em = this.getEntityManager();
            listModel = em.createNamedQuery("CatEstatusSolicitud.findAll", CatEstatusSolicitud.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de estatus", ex);
        } finally 
        {
            this.close(em);
        }
        
        return listModel;
    }
    
}
