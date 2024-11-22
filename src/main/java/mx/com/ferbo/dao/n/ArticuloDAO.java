/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatArticulo;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ArticuloDAO extends BaseDAO<CatArticulo, Integer> {

    private static Logger log = LogManager.getLogger(CatArticulo.class);

    public ArticuloDAO(Class<CatArticulo> modelClass) {
        super(modelClass);
    }

    public ArticuloDAO() {
        super(CatArticulo.class);
    }

    public List<CatArticulo> buscarTodos() {
        List<CatArticulo> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("CatArticulo.findAll", CatArticulo.class)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de articulos...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }

    public List<CatArticulo> buscarTodosActivos() {
        List<CatArticulo> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("CatArticulo.findAllActive", CatArticulo.class)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de articulos activos...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }

    @Override
    public synchronized void eliminar(CatArticulo articulo) throws SGPException {
        EntityManager em = null;
        
        try{
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.remove(em.contains(articulo) ? articulo : em.merge(articulo));
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.debug("Hubo algun problema al eliminar el registro:" + articulo.toString() + " de la tabla CatArticulo", ex);
            throw new SGPException("Hubo algun inconveniente al eliminar el articulo");
        }
        finally{
            super.close(em);
        }

    }

    @Override
    public synchronized void actualizar(CatArticulo articulo) throws SGPException {
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.merge(articulo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.debug("Hubo algun problema al actualizar el registro:" + articulo.toString() + " en la tabla CatArticulo", ex);
            throw new SGPException("Hubo algun inconveniente al actualizar el articulo");
        } finally {
            super.close(em);
        }
    }

    @Override
    public synchronized void guardar(CatArticulo articulo) throws SGPException {
        EntityManager em = null;

        try {
            em = super.getEntityManager();

            em.getTransaction().begin();
            em.persist(articulo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.debug("Hubo algun problema en guardar el articulo:" + articulo.toString() + " en la tabla CatArticulo", ex);
            throw new SGPException("Hubo algun inconveniente en guardar el articulo");
        } finally {
            super.close(em);
        }
    }

}
