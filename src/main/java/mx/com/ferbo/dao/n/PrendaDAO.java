/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPrenda;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author alberto
 */
public class PrendaDAO extends BaseDAO<CatPrenda, Integer>
{
    private static Logger log = LogManager.getLogger(CatPrenda.class);

    public PrendaDAO(Class<CatPrenda> modelClass) 
    {
        super(modelClass);
    }
    
    public PrendaDAO()
    {
        super(CatPrenda.class);
    }
    
    public List<CatPrenda> buscarTodos()
    {
        List<CatPrenda> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatPrenda.findAll", CatPrenda.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de prendas...", ex);
        } finally 
        {
            close(em);
        }
        
        return modelList;
    }
    
    public List<CatPrenda> buscarTodosActivos()
    {
        List<CatPrenda> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatPrenda.findAllActive", CatPrenda.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de prendas...", ex);
        } finally 
        {
            close(em);
        }
        
        return modelList;
    }
    
}
