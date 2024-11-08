
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatLocalidad;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LocalidadDAO extends BaseDAO{
    
    private static Logger log = LogManager.getLogger(FpClientDAO.class);
    
    public LocalidadDAO(Class<CatLocalidad> modelClass) {
        super(modelClass);
    }
    
    public LocalidadDAO(){
        super(CatLocalidad.class);
    }
    
    public synchronized List<CatLocalidad> obtenerTodosPorMuncipio(Integer idPais, Integer idEstado, Integer idMunicipio) throws SGPException{
        EntityManager em = null;
        List<CatLocalidad> localidades = null;
        try{
            log.info("Inicia proceso de obtener todos los localidades.");
            em = super.getEntityManager();
            TypedQuery<CatLocalidad> resultado = em.createQuery("select e from  CatLocalidad e where e.key.municipio.key.estado.key.pais.id = :idPais and e.key.municipio.key.estado.key.id = :idEstado and e.key.municipio.key.id = :idMunicipio", CatLocalidad.class);
            resultado.setParameter("idPais", idPais);
            resultado.setParameter("idEstado", idEstado);
            resultado.setParameter("idMunicipio", idMunicipio);
            localidades = resultado.getResultList();
            
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatLocalidad");
            throw new SGPException("Problema al obtener los registros" + ex);
        }
        finally{
            log.info("Finaliza proceso de obtener todos los localidades");
            super.close(em);
        }
        return localidades;
    }
    
    public synchronized List<CatLocalidad> obtenerTodos() throws SGPException{
        EntityManager em = null;
        List<CatLocalidad> localidades = null;
        try{
            log.info("Inicia proceso de obtener todos los localidades.");
            em = super.getEntityManager();
            TypedQuery<CatLocalidad> resultado = em.createQuery("select e from  CatLocalidad e", CatLocalidad.class);
            localidades = resultado.getResultList();
            
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatLocalidad");
            throw new SGPException("Problema al obtener los registros" + ex);
        }
        finally{
            log.info("Finaliza proceso de obtener todos los localidades");
            super.close(em);
        }
        return localidades;
    }
    
    public synchronized CatLocalidad buscarPorId(Integer id) throws SGPException{
        
        EntityManager em = null;
        CatLocalidad localidad = null;
        
        try{
          log.info("Inicia proceso de obtener elemento con id: {}", id);
          em = super.getEntityManager();
          em.getTransaction().begin();
          localidad = em.find(CatLocalidad.class, id);
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
        return localidad;
    }
    
    public synchronized void guardar(CatLocalidad localidad) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de guardar el registro en la tabla CatLocalidad");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.persist(localidad);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun proble al guardar el registro {} en la tabla CatLocalidad", localidad);
            throw new SGPException("Problema al guardar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de guardar el registro en la tabla CatLocalidad");
        }
    }
    
    public synchronized void actualizar(CatLocalidad localidad) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de actualizar el registro de la tabla CatLocalidad");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.merge(localidad);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al actualizar el registro {} en la tabla CatLocalidad", localidad);
            throw new SGPException("Problema al actualizar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatLocalidad");
        }
    }
    
    public synchronized void eliminar(CatLocalidad localidad) throws SGPException{
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de eliminar el registro de la tabla CatLocalidad");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(localidad) ? localidad : em.merge(localidad));
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al eliminar el registro {} de la tabla CatLocalidad", localidad);
            throw new SGPException("Problema al eliminar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatLocalidad");
        }
    }
}
