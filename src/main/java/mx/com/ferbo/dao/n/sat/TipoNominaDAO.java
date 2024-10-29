package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoNomina;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoNominaDAO extends BaseDAO<CatTipoNomina, String> 
{
    private static Logger log = LogManager.getLogger(CatTipoNomina.class);

    public TipoNominaDAO(Class<CatTipoNomina> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoNominaDAO()
    {
        super(CatTipoNomina.class);
    }
    
    public List<CatTipoNomina> buscarTodos()
    {
        List<CatTipoNomina> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoNomina.findByAll", CatTipoNomina.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener la lista de tipos de contratos...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
}
