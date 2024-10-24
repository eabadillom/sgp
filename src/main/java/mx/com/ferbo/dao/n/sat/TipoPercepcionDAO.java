/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoPercepcionDAO extends BaseDAO<CatTipoPercepcion, String>
{
    private static Logger log = LogManager.getLogger(TipoPercepcionDAO.class);

    public TipoPercepcionDAO(Class<CatTipoPercepcion> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoPercepcionDAO()
    {
        super(CatTipoPercepcion.class);
    }
    
    public List<CatTipoPercepcion> buscarTodos()
    {
        List<CatTipoPercepcion> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoPercepcion.findByAll", CatTipoPercepcion.class)
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
