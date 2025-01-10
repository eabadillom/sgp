package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstatusRegistro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EstatusRegistroDAO extends BaseDAO<CatEstatusRegistro, Integer> {
    private static Logger log = LogManager.getLogger(EstatusRegistroDAO.class);
    
	public EstatusRegistroDAO(Class<CatEstatusRegistro> modelClass) {
		super(modelClass);
	}
        
    public CatEstatusRegistro buscarPorCodigo(String codigo)
    {
        CatEstatusRegistro model = null;
        EntityManager em = null;
        
        try 
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatEstatusRegistro.findByCodigo", CatEstatusRegistro.class)
                .setParameter("codigo", codigo)
                .getSingleResult();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el estatus del registro", ex);
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }

}
