
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
    
    private static Logger log = LogManager.getLogger(CatTipoAsentamiento.class);
    
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
            log.info("Finaliza proceso de obtener todos los tipos de asentamiento");
            
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatTipoAsentamiento " + ex);
            throw new SGPException("Problema al obtener los registros");
        }
        finally{
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
          log.info("Finaliza proceso de obtener elemento con id: {}", id);  
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al buscar el elemento con id: " + id + ". " + ex);
            throw new SGPException("Problema al obtener el elemento por id ");
        }
        finally{
            super.close(em);         
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
            log.info("Finaliza prodceo de guardar el registro en la tabla CatTipoAsentamiento");
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun proble al guardar el registro en la tabla CatTipoAsentamiento. " + ex);
            throw new SGPException("Problema al guardar el registro");
        }
        finally{
            super.close(em);
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
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatTipoAsentamiento");
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al actualizar el registro en la tabla CatTipoAsentamiento. " + ex);
            throw new SGPException("Problema al actualizar el registro ");
        }
        finally{
            super.close(em);
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
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatTipoAsentamiento");
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al eliminar el registro de la tabla CatTipoAsentamiento. " + ex);
            throw new SGPException("Problema al eliminar el registro ");
        }
        finally{
            super.close(em);
        }
    }
    
    public synchronized List<CatTipoAsentamiento> obtenerPorAsentamiento(short idAsent)throws SGPException{
        List<CatTipoAsentamiento> resultado = null;
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de obtener tododos los tipos de asentamiento en base al asentamiento.");
            em = super.getEntityManager();
            TypedQuery <CatTipoAsentamiento> consulta = em.createQuery("select e from CatTipoAsentamiento e where e.id = :idAsent", CatTipoAsentamiento.class);
            consulta.setParameter("idAsent", idAsent);
            resultado = consulta.getResultList();
            log.info("Finaliza proceso de obtener tododos los tipos de asentamiento en base al asentamiento.");
        }
        catch(Exception ex){
            log.error("Error al obtener todos los tipos de asentamiento en base al asentamiento de la tabla CatTipoAsentamiento. " + ex);
            super.rollback(em);
            throw new SGPException("Hubo un problema al obtener todos los tipos de asentamiento");
        }
        finally{
            super.close(em);
        }
        
        return resultado;
        
    }
}
