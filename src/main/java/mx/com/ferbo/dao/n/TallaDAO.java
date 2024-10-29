/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTalla;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TallaDAO extends BaseDAO<CatTalla, Integer>
{
    private static Logger log = LogManager.getLogger(CatTalla.class);

    public TallaDAO(Class<CatTalla> modelClass) {
        super(modelClass);
    }
    
    public TallaDAO()
    {
        super(CatTalla.class);
    }
    
    public List<CatTalla> buscarPorIdTalla(Integer idTalla)
    {
        List<CatTalla> model = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            model = em.createNamedQuery("CatTalla.findForId", CatTalla.class)
                .setParameter("idTalla", idTalla)
                .getResultList();
        }
        catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de tallas...", ex);
        } finally 
        {
            close(em);
        }
        
        return model;
    }
    
    public List<CatTalla> buscarTodos()
    {
        List<CatTalla> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatTalla.findAll", CatTalla.class)
                .getResultList();
        }
        catch (Exception ex) 
        {
            log.error("Problema para obtener la prenda...", ex);
        } finally 
        {
            close(em);
        }
        
        return modelList;
    }
    
    public List<CatTalla> buscarTodosActivos()
    {
        List<CatTalla> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatTalla.findAllActive", CatTalla.class)
                .getResultList();
        }
        catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de tallas...", ex);
        } finally 
        {
            close(em);
        }
        
        return modelList;
    }
    
}
