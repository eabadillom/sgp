package mx.com.ferbo.dao.n.imss;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.imss.CatControlIncapacidadIMSS;
import mx.com.ferbo.model.imss.CatTipoIncapacidadIMSS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ControlIncapacidadIMSSDAO extends BaseDAO<CatControlIncapacidadIMSS, Integer>
{
    private static Logger log = LogManager.getLogger(CatTipoIncapacidadIMSS.class);

    public ControlIncapacidadIMSSDAO(Class<CatControlIncapacidadIMSS> modelClass) 
    {
        super(modelClass);
    }
    
    public ControlIncapacidadIMSSDAO()
    {
        super(CatControlIncapacidadIMSS.class);
    }
    
    public List<CatControlIncapacidadIMSS> buscarTodos()
    {
        List<CatControlIncapacidadIMSS> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatControlIncapacidadIMSS.findAll", CatControlIncapacidadIMSS.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener la lista de control de incapacidades...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public CatControlIncapacidadIMSS buscarPorClave(String clave)
    {
        CatControlIncapacidadIMSS model = null;
        
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatControlIncapacidadIMSS.findByClave", CatControlIncapacidadIMSS.class)
                .setParameter("clave", clave)
                .getSingleResult();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el registro de control de incapacidad...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }
    
}
