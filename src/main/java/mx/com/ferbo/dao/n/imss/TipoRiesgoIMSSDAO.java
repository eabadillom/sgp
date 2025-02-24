package mx.com.ferbo.dao.n.imss;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.imss.CatTipoRiesgoIMSS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoRiesgoIMSSDAO extends BaseDAO<CatTipoRiesgoIMSS, Integer>
{
    private static Logger log = LogManager.getLogger(CatTipoRiesgoIMSS.class);
    
    public TipoRiesgoIMSSDAO(Class<CatTipoRiesgoIMSS> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoRiesgoIMSSDAO()
    {
        super(CatTipoRiesgoIMSS.class);
    }
    
    public List<CatTipoRiesgoIMSS> buscarTodos()
    {
        List<CatTipoRiesgoIMSS> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoRiesgoIMSS.findAll", CatTipoRiesgoIMSS.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener la lista de riesgo de trabajo...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public CatTipoRiesgoIMSS buscarPorClave(String clave)
    {
        CatTipoRiesgoIMSS model = null;
        
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatTipoRiesgoIMSS.findByClave", CatTipoRiesgoIMSS.class)
                .setParameter("clave", clave)
                .getSingleResult();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el registro de trabajo...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }
    
}
