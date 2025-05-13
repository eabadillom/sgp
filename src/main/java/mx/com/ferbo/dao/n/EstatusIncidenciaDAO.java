package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class EstatusIncidenciaDAO extends BaseDAO<CatEstatusIncidencia, Integer>
{
    private static Logger log = LogManager.getLogger(EstatusIncidenciaDAO.class);
    
    public EstatusIncidenciaDAO(Class<CatEstatusIncidencia> modelClass) 
    {
        super(modelClass);
    }
    
    public EstatusIncidenciaDAO()
    {
        super(CatEstatusIncidencia.class);
    }
    
    public List<CatEstatusIncidencia> buscarTodos()
    {
        List<CatEstatusIncidencia> modelList = null;
        EntityManager em = null;
        
        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatEstatusIncidencia.findAll", CatEstatusIncidencia.class)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener el listado de estatus incidencia...", ex);
        } finally {
            this.close(em);
        }
        
        return modelList;
    }
    
    public CatEstatusIncidencia buscarPorClave(String clave)
    {
        CatEstatusIncidencia model = null;
        EntityManager em = null;
        
        try 
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatEstatusIncidencia.findByClave", CatEstatusIncidencia.class)
                .setParameter("clave", clave)
                .getSingleResult();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener el listado de estatus incidencia...", ex);
        } finally {
            this.close(em);
        }
        
        return model;
    }
    
}
