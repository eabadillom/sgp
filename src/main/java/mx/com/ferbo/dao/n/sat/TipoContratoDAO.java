/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n.sat;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoContrato;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoContratoDAO extends BaseDAO<CatTipoContrato, String>
{
    private static Logger log = LogManager.getLogger(TipoContratoDAO.class);

    public TipoContratoDAO(Class<CatTipoContrato> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoContratoDAO()
    {
        super(CatTipoContrato.class);
    }
    
    public List<CatTipoContrato> buscarTodos()
    {
        List<CatTipoContrato> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoContrato.findAll", CatTipoContrato.class)
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
