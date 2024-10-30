/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n.sat;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoRegimen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoRegimenDAO extends BaseDAO<CatTipoRegimen, String>
{
    private static Logger log = LogManager.getLogger(TipoRegimenDAO.class);

    public TipoRegimenDAO(Class<CatTipoRegimen> modelClass) 
    {
        super(modelClass);
    }
    
    public TipoRegimenDAO()
    {
        super(CatTipoRegimen.class);
    }
    
    public List<CatTipoRegimen> buscarTodos()
    {
        List<CatTipoRegimen> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoRegimen.findAll", CatTipoRegimen.class)
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
    
    public List<CatTipoRegimen> buscarActivo(Date fecha)
    {
        List<CatTipoRegimen> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatTipoRegimen.findActive", CatTipoRegimen.class)
                .setParameter("fecha", fecha)
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
