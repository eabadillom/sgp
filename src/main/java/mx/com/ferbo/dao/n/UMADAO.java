/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatUMA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class UMADAO extends BaseDAO<CatUMA, Integer>
{
    private static Logger log = LogManager.getLogger(UMADAO.class);
    
    public UMADAO(Class<CatUMA> modelClass) 
    {
        super(modelClass);
    }
    
    public UMADAO() 
    {
        super(CatUMA.class);
    }
    
    @Override
    public CatUMA buscarPorId(Integer codigoAnio)
    {
        CatUMA uma = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            uma = em.createNamedQuery("CatUMA.findById", CatUMA.class)
                    .setParameter("cd_anio", codigoAnio)
                    .getSingleResult();
        }catch(Exception ex) 
        {
            log.error("Problema para obtener la UMA...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return uma;
    }
    
    public List<CatUMA> obtenerLista()
    {
        List<CatUMA> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatUMA.findAll", CatUMA.class)
                    .getResultList();
        }catch(Exception ex)
        {
            log.error("Problema al obtener la lista de las UMA'S...", ex);
        }finally
        {
            this.close(em);
        }
        
        return modelList;
    }
    
}
