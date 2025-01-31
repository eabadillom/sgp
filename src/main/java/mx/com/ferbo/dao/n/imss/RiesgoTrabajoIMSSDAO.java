package mx.com.ferbo.dao.n.imss;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.imss.CatRiesgoTrabajoIMSS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class RiesgoTrabajoIMSSDAO extends BaseDAO<CatRiesgoTrabajoIMSS, Integer>
{
    private static Logger log = LogManager.getLogger(CatRiesgoTrabajoIMSS.class);

    public RiesgoTrabajoIMSSDAO(Class<CatRiesgoTrabajoIMSS> modelClass) 
    {
        super(modelClass);
    }
    
    public RiesgoTrabajoIMSSDAO()
    {
        super(CatRiesgoTrabajoIMSS.class);
    }
    
    public List<CatRiesgoTrabajoIMSS> buscarTodos()
    {
        List<CatRiesgoTrabajoIMSS> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatRiesgoTrabajoIMSS.findAll", CatRiesgoTrabajoIMSS.class)
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
    
    public CatRiesgoTrabajoIMSS buscarPorClave(String clave)
    {
        CatRiesgoTrabajoIMSS model = null;
        
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatRiesgoTrabajoIMSS.findByClave", CatRiesgoTrabajoIMSS.class)
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
