package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatOrigRecurso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class OrigenRecursosDAO extends BaseDAO<CatOrigRecurso, String>
{
    private static Logger log = LogManager.getLogger(OrigenRecursosDAO.class);

    public OrigenRecursosDAO(Class<CatOrigRecurso> modelClass) 
    {
        super(modelClass);
    }
    
    public OrigenRecursosDAO()
    {
        super(CatOrigRecurso.class);
    }
    
    public List<CatOrigRecurso> buscarTodos()
    {
        List<CatOrigRecurso> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatOrigRecurso.findAll", CatOrigRecurso.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener la lista de bancos...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
}
