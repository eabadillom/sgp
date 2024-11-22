package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Pais;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class PaisDAO extends BaseDAO<Pais, String>
{
    private static Logger log = LogManager.getLogger(PaisDAO.class);

    public PaisDAO(Class modelClass) {
        super(modelClass);
    }
    
    public PaisDAO()
    {
        super(Pais.class);
    }
    
    public List<Pais> buscarTodos()
    {
        List<Pais> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("Pais.findAll", Pais.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el pais: {}", ex.getMessage());
        }finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public List<Pais> buscarPorId(Integer idPais)
    {
        List<Pais> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("Pais.findById", Pais.class)
                .setParameter("cd_pais", idPais)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el pais: {}", ex.getMessage());
        }finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public List<Pais> buscarPorClave(String clavePais)
    {
        List<Pais> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("Pais.findByClave", Pais.class)
                .setParameter("nb_clave", clavePais)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el pais: {}", ex.getMessage());
        }finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
}
