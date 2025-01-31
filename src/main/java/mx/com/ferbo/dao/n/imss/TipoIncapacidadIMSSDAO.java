package mx.com.ferbo.dao.n.imss;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.imss.CatTipoIncapacidadIMSS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoIncapacidadIMSSDAO extends BaseDAO<CatTipoIncapacidadIMSS, Integer>
{
    private static Logger log = LogManager.getLogger(CatTipoIncapacidadIMSS.class);
    
    public TipoIncapacidadIMSSDAO(Class<CatTipoIncapacidadIMSS> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoIncapacidadIMSSDAO()
    {
        super(CatTipoIncapacidadIMSS.class);
    }
    
    public List<CatTipoIncapacidadIMSS> buscarTodos()
    {
        List<CatTipoIncapacidadIMSS> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoIncapacidadIMSS.findAll", CatTipoIncapacidadIMSS.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener la lista de incapacidades...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public CatTipoIncapacidadIMSS buscarPorClave(String clave)
    {
        CatTipoIncapacidadIMSS model = null;
        
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatTipoIncapacidadIMSS.findByClave", CatTipoIncapacidadIMSS.class)
                .setParameter("clave", clave)
                .getSingleResult();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el registro de incapacidad...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }
    
}
