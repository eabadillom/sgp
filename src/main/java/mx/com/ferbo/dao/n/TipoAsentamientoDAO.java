
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTipoAsentamiento;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TipoAsentamientoDAO extends BaseDAO{
    
    private static Logger log = LogManager.getLogger(FpClientDAO.class);
    
    public TipoAsentamientoDAO(Class<CatTipoAsentamiento> modelClass) {
        super(modelClass);
    }
    
    public TipoAsentamientoDAO(){
        super(CatTipoAsentamiento.class);
    }
    
    public synchronized List<CatTipoAsentamiento> obtenerTodos() throws SGPException{
        EntityManager em = null;
        List<CatTipoAsentamiento> tiposasentamiento = null;
        try{
            log.info("Inicia proceso de obtener todos los tiposasentamiento.");
            em = super.getEntityManager();
            TypedQuery<CatTipoAsentamiento> resultado = em.createQuery("select e from  CatTipoAsentamiento e", CatTipoAsentamiento.class);
            tiposasentamiento = resultado.getResultList();
            
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatTipoAsentamiento");
            throw new SGPException("Problema al obtener los registros" + ex);
        }
        finally{
            log.info("Finaliza proceso de obtener todos los tipos de asentamiento");
            super.close(em);
        }
        return tiposasentamiento;
    }
    
    public synchronized CatTipoAsentamiento buscarPorId(Integer id) throws SGPException{
        
        EntityManager em = null;
        CatTipoAsentamiento tipoasentamiento = null;
        
        try{
          log.info("Inicia proceso de obtener elemento con id: {}", id);
          em = super.getEntityManager();
          em.getTransaction().begin();
          tipoasentamiento = em.find(CatTipoAsentamiento.class, id);
          em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al buscar el elemento con id: " + id);
            throw new SGPException("Problema al obtener el elemento por id " + ex);
        }
        finally{
            super.close(em);
           log.info("Finaliza proceso de obtener elemento con id: {}", id);           
        }
        return tipoasentamiento;
    }
    
    public synchronized void guardar(CatTipoAsentamiento tipoasentamiento) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de guardar el registro en la tabla CatTipoAsentamiento");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoasentamiento);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun proble al guardar el registro {} en la tabla CatTipoAsentamiento", tipoasentamiento);
            throw new SGPException("Problema al guardar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de guardar el registro en la tabla CatTipoAsentamiento");
        }
    }
    
    public synchronized void actualizar(CatTipoAsentamiento tipoasentamiento) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de actualizar el registro de la tabla CatTipoAsentamiento");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.merge(tipoasentamiento);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al actualizar el registro {} en la tabla CatTipoAsentamiento", tipoasentamiento);
            throw new SGPException("Problema al actualizar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatTipoAsentamiento");
        }
    }
    
    public synchronized void eliminar(CatTipoAsentamiento tipoasentamiento) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de eliminar el registro de la tabla CatTipoAsentamiento");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(tipoasentamiento) ? tipoasentamiento : em.merge(tipoasentamiento));
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al eliminar el registro {} de la tabla CatTipoAsentamiento", tipoasentamiento);
            throw new SGPException("Problema al eliminar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatTipoAsentamiento");
        }
    }
}
