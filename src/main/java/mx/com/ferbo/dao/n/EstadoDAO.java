
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstado;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EstadoDAO extends BaseDAO {
    
    private static Logger log = LogManager.getLogger(CatEstado.class);
    
    public EstadoDAO(Class<CatEstado> modelClass) {
        super(modelClass);
    }
    
    public EstadoDAO(){
        super(CatEstado.class);
    }
    
    public synchronized List<CatEstado> obtenerTodosPorPais(Integer id) throws SGPException{
        EntityManager em = null;
        List<CatEstado> estados = null;
        try{
            log.info("Inicia proceso de obtener todos los estados.");
            em = super.getEntityManager();
            
            TypedQuery<CatEstado> resultado = em.createQuery("select e from  CatEstado e where e.key.pais.id = :id", CatEstado.class);
            resultado.setParameter("id", id);
            estados = resultado.getResultList();
            log.info("Finaliza proceso de obtener todos los estados");
            
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatEstado");
            throw new SGPException("Problema al obtener los registros" + ex);
        }
        finally{
            super.close(em);
        }
        return estados;
    }
    
    public synchronized List<CatEstado> obtenerTodos() throws SGPException{
        EntityManager em = null;
        List<CatEstado> estados = null;
        try{
            log.info("Inicia proceso de obtener todos los estados.");
            em = super.getEntityManager();
            TypedQuery<CatEstado> resultado = em.createQuery("select e from  CatEstado e", CatEstado.class);
            estados = resultado.getResultList();
            log.info("Finaliza proceso de obtener todos los estados");
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatEstado");
            throw new SGPException("Problema al obtener los registros" + ex);
        }
        finally{
            super.close(em);
        }
        return estados;
    }
    
    public synchronized CatEstado buscarPorId(Integer id) throws SGPException{
        
        EntityManager em = null;
        CatEstado estado = null;
        
        try{
          log.info("Inicia proceso de obtener elemento con id: {}", id);
          em = super.getEntityManager();
          em.getTransaction().begin();
          estado = em.find(CatEstado.class, id);
          em.getTransaction().commit();
          log.info("Finaliza proceso de obtener elemento con id: {}", id);
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al buscar el elemento con id: " + id);
            throw new SGPException("Problema al obtener el elemento por id " + ex);
        }
        finally{
            super.close(em);           
        }
        return estado;
    }
    
    public synchronized void guardar(CatEstado estado) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de guardar el registro en la tabla CatEstados");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.persist(estado);
            em.getTransaction().commit();
            log.info("Finaliza prodceo de guardar el registro en la tabla CatEstados");
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun proble al guardar el registro {} en la tabla CatEstado", estado);
            throw new SGPException("Problema al guardar el registro " + ex);
        }
        finally{
            super.close(em);
        }
    }
    
    public synchronized void actualizar(CatEstado estado) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de actualizar el registro de la tabla CatEstados");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.merge(estado);
            em.getTransaction().commit();
            log.info("Finaliza proceso de actualizar el registro de la tabla CatEstados");
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al actualizar el registro {} en la tabla CatEstado", estado);
            throw new SGPException("Problema al actualizar el registro " + ex);
        }
        finally{
            super.close(em);
        }
    }
    
    public synchronized void eliminar(CatEstado estado) throws SGPException{
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de eliminar el registro de la tabla CatEstados");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(estado) ? estado : em.merge(estado));
            em.getTransaction().commit();
            log.info("Finaliza proceso de eliminar el registro de la tabla CatEstados");
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al eliminar el registro {} de la tabla CatEstado", estado);
            throw new SGPException("Problema al eliminar el registro " + ex);
        }
        finally{
            super.close(em);
        }
    }
}
