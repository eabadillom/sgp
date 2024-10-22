/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPrenda;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class PrendaDAO extends BaseDAO<CatPrenda, Integer> {

    private static Logger log = LogManager.getLogger(CatPrenda.class);

    public PrendaDAO(Class<CatPrenda> modelClass) {
        super(modelClass);
    }

    public PrendaDAO() {
        super(CatPrenda.class);
    }

    public CatPrenda buscarPrendaPorId(Integer id) throws SGPException {
        CatPrenda prenda = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            em.getTransaction().begin();
            prenda = em.find(CatPrenda.class, id);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.debug("Hubo algun problema al buscar el id: " + id + " en la tabla CatPrenda", ex);
            throw new SGPException("No se encontro el elemento buscado");
        } finally {
            super.close(em);
        }

        return prenda;
    }

    public List<CatPrenda> buscarTodos() {
        List<CatPrenda> modelList = null;
        EntityManager em = null;

        try {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatPrenda.findAll", CatPrenda.class)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de prendas...", ex);
        } finally {
            close(em);
        }

        return modelList;
    }

    public List<CatPrenda> buscarTodosActivos() {
        List<CatPrenda> modelList = null;
        EntityManager em = null;

        try {
            em = getEntityManager();
            modelList = em.createNamedQuery("CatPrenda.findAllActive", CatPrenda.class)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de prendas...", ex);
        } finally {
            close(em);
        }

        return modelList;
    }

    @Override
    public synchronized void eliminar(CatPrenda prenda) throws SGPException {
        EntityManager em = null;
        
        try{
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.remove(em.contains(prenda) ? prenda : em.merge(prenda));
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.debug("Hubo algun problema al eliminar el registro: " + prenda.toString() + " de la tabla CatPrenda", ex);
            throw new SGPException("Hubo algun inconveniente al eliminar la prenda");
        }
        finally{
            super.close(em);
        }
    }

    @Override
    public synchronized void actualizar(CatPrenda prenda) throws SGPException {
        EntityManager em = null;
        
        try{
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.merge(prenda);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.debug("Hubo algun problema al actualizar el registro: " + prenda.toString() + " en la tabla CatPrenda", ex);
            throw new SGPException("Hubo algun inconveniente al actualizar la prenda");
        }
        finally{
            super.close(em);
        }
    }

    @Override
    public synchronized void guardar(CatPrenda prenda) throws SGPException {
        EntityManager em = null;

        try {
            em = super.getEntityManager();

            em.getTransaction().begin();
            em.persist(prenda);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.debug("Hubo algun problema al guardar la prenda: " + prenda.toString() + " en la tabla CatPrenda", ex);
            throw new SGPException("Hubo algun inconveniente al guardar la prenda");
        } finally {
            super.close(em);
        }
    }

}
