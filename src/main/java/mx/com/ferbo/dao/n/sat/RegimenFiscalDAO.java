/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n.sat;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class RegimenFiscalDAO extends BaseDAO<CatRegimenFiscal, String>
{
    private static Logger log = LogManager.getLogger(RegimenFiscalDAO.class);
    
    public RegimenFiscalDAO(Class<CatRegimenFiscal> modelClass) 
    {
        super(modelClass);
    }
    
    public RegimenFiscalDAO()
    {
        super(CatRegimenFiscal.class);
    }
    
    public List<CatRegimenFiscal> buscarTodos() 
    {
        List<CatRegimenFiscal> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = EntityManagerUtil.getEntityManager();
            modelList = em.createNamedQuery("CatRegimenFiscal.findAll", CatRegimenFiscal.class)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Probleam para obtener la lista de regímenes fiscales", ex);
        } finally 
        {
            EntityManagerUtil.close(em);
        }
        
        return modelList;
    }
    
    public List<CatRegimenFiscal> buscarActivos(Date fecha) 
    {
        List<CatRegimenFiscal> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = EntityManagerUtil.getEntityManager();
            modelList = em.createNamedQuery("CatRegimenFiscal.findByActivos", CatRegimenFiscal.class)
                .setParameter("fecha", fecha)
                .getResultList();
        }catch(Exception ex) 
        {
            log.error("Probleam para obtener la lista de regímenes fiscales", ex);
        } finally 
        {
            EntityManagerUtil.close(em);
        }
        
        return modelList;
    }
    
}
