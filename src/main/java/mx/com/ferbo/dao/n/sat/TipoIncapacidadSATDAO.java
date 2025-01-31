/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoIncapacidadSAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoIncapacidadSATDAO extends BaseDAO<CatTipoIncapacidadSAT, String>
{
    private static Logger log = LogManager.getLogger(CatTipoIncapacidadSAT.class);

    public TipoIncapacidadSATDAO(Class<CatTipoIncapacidadSAT> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoIncapacidadSATDAO()
    {
        super(CatTipoIncapacidadSAT.class);
    }
    
    public List<CatTipoIncapacidadSAT> buscarTodos()
    {
        List<CatTipoIncapacidadSAT> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoIncapacidadSAT.findByAll", CatTipoIncapacidadSAT.class)
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
