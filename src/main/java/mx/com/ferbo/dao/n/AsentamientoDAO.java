package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.CatAsentamientoPK;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class AsentamientoDAO extends BaseDAO<CatAsentamiento, CatAsentamientoPK>
{
    private static Logger log = LogManager.getLogger(AsentamientoDAO.class);
    
    public AsentamientoDAO(Class<CatAsentamiento> modelClass) 
    {
        super(modelClass);
    }
    
    public AsentamientoDAO()
    {
        super(CatAsentamiento.class);
    }
    
    public List<CatAsentamiento> buscarTodos() 
    {
        List<CatAsentamiento> modelList = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatAsentamiento.findAll", CatAsentamiento.class).getResultList();
        } catch(Exception ex) 
        {
            log.error("Problema para obtener la lista de asentamientos...",  ex);
        } finally 
        {
            this.close(em);
        }

        return modelList;
    }
    
    public List<CatAsentamiento> buscarPorCodigoPostal(String codigoPostal) 
    {
        List<CatAsentamiento> modelList = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatAsentamiento.findByCodigoPostal", CatAsentamiento.class)
                .setParameter("codigoPostal", codigoPostal)
                .getResultList();
        } catch(Exception ex) 
        {
            log.error("Problema para obtener la lista de asentamientos...",  ex);
        } finally 
        {
            this.close(em);
        }

        return modelList;
    }
    
}
