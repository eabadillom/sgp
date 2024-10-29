package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatBanco;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class BancoDAO extends BaseDAO<CatBanco, String>
{
    private static Logger log = LogManager.getLogger(BancoDAO.class);
    
    public BancoDAO(Class<CatBanco> modelClass) 
    {
        super(modelClass);
    }
    
    public BancoDAO()
    {
        super(CatBanco.class);
    }
    
    public List<CatBanco> buscarTodos()
    {
        List<CatBanco> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatBanco.findByAll", CatBanco.class)
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
    
    public List<CatBanco> buscarPorIdBanco(String id)
    {
        List<CatBanco> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatBanco.findById", CatBanco.class)
                .setParameter("idBanco", id)
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
    
    public List<CatBanco> buscarPorNombre(String nombre)
    {
        List<CatBanco> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatBanco.findByNombre", CatBanco.class)
                .setParameter("nombre", nombre)
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
