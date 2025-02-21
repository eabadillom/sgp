package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstatusIncapacidad;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class EstatusIncapacidadDAO extends BaseDAO<CatEstatusIncapacidad, Integer>
{
    private static Logger log = LogManager.getLogger(EstatusIncapacidadDAO.class);
    
    public EstatusIncapacidadDAO(Class<CatEstatusIncapacidad> modelClass) 
    {
        super(modelClass);
    }
    
    public EstatusIncapacidadDAO()
    {
        super(CatEstatusIncapacidad.class);
    }
    
    public CatEstatusIncapacidad buscarPorClave(String clave)
    {
        CatEstatusIncapacidad model = null;
        EntityManager em = null;
        
        try 
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatEstatusIncapacidad.findByClave", CatEstatusIncapacidad.class)
                .setParameter("clave", clave)
                .getSingleResult();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de estatus", ex);
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }
    
    public List<CatEstatusIncapacidad> buscarTodos()
    {
        List<CatEstatusIncapacidad> listModel = null;
        EntityManager em = null;
        
        try 
        {
            em = this.getEntityManager();
            listModel = em.createNamedQuery("CatEstatusIncapacidad.findAll", CatEstatusIncapacidad.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de estatus", ex);
        } finally 
        {
            this.close(em);
        }
        
        return listModel;
    }
    
}
