/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoIncapacidad;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoIncapacidadDAO extends BaseDAO<CatTipoIncapacidad, String>
{
    private static Logger log = LogManager.getLogger(CatTipoIncapacidad.class);

    public TipoIncapacidadDAO(Class<CatTipoIncapacidad> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoIncapacidadDAO()
    {
        super(CatTipoIncapacidad.class);
    }
    
    public List<CatTipoIncapacidad> buscarTodos()
    {
        List<CatTipoIncapacidad> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoIncapacidad.findByAll", CatTipoIncapacidad.class)
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
