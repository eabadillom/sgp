package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class RiesgoPuestoDAO extends BaseDAO<CatRiesgoPuesto, Integer>
{
    private static Logger log = LogManager.getLogger(CatRiesgoPuesto.class);

    public RiesgoPuestoDAO(Class<CatRiesgoPuesto> modelClass) {
        super(modelClass);
    }
    
    public RiesgoPuestoDAO()
    {
        super(CatRiesgoPuesto.class);
    }
    
    public List<CatRiesgoPuesto> buscarTodos()
    {
        List<CatRiesgoPuesto> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatRiesgoPuesto.findByAll", CatRiesgoPuesto.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener la lista de riesgos de trabajo...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
}
