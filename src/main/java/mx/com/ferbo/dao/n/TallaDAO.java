/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTalla;
import mx.com.ferbo.util.SGPException;
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

    @Override
    public synchronized void eliminar(CatTalla talla) throws SGPException {
        
        EntityManager em = null;
        
        try{
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.remove(em.contains(talla) ? talla : em.merge(talla));
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.debug("Hubo algun problema al eliminar la talla: " + talla.toString()  + " de la tabla CatTalla", ex);
            throw new SGPException("No se pudo eliminar la talla");
        }
        finally{
            super.close(em);
        }

    }

    @Override
    public synchronized void actualizar(CatTalla talla) throws SGPException {
        
        EntityManager em = null;
        
        try{
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.merge(talla);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.debug("Hubo algun problema al actualizar el registro: " + talla.toString()  + " de la tabla CatTalla", ex);
            throw new SGPException("No se pudo actualizar la talla");
        }
        finally{
            super.close(em);
        }
    }

    @Override
    public synchronized void guardar(CatTalla talla) throws SGPException {

        EntityManager em = null;
        
        try{
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.persist(talla);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.debug("Hubo algun problema al guardar la talla: " + talla.toString()  + " de la tabla CatTalla", ex);
            throw new SGPException("No se pudo guardar la talla");
        }
        finally{
            super.close(em);
        }
    }
    
    
}
