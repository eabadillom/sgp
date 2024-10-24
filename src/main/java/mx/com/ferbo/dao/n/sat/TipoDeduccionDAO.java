/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoDeduccionDAO extends BaseDAO<CatTipoDeduccion, String>
{
    private static Logger log = LogManager.getLogger(TipoDeduccionDAO.class);

    public TipoDeduccionDAO(Class<CatTipoDeduccion> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoDeduccionDAO()
    {
        super(CatTipoDeduccion.class);
    }
    
    public List<CatTipoDeduccion> buscarTodos()
    {
        List<CatTipoDeduccion> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoDeduccion.findByAll", CatTipoDeduccion.class)
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
