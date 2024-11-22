
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatMunicipio;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MunicipioDAO extends BaseDAO{
    
     private static Logger log = LogManager.getLogger(FpClientDAO.class);
    
    public MunicipioDAO(Class<CatMunicipio> modelClass) {
        super(modelClass);
    }
    
    public MunicipioDAO(){
        super(CatMunicipio.class);
    }
    
    public synchronized List<CatMunicipio> obtenerTodos() throws SGPException{
        EntityManager em = null;
        List<CatMunicipio> municipios = null;
        try{
            log.info("Inicia proceso de obtener todos los municipios.");
            em = super.getEntityManager();
            TypedQuery<CatMunicipio> resultado = em.createQuery("select e from  CatMunicipio e", CatMunicipio.class);
            municipios = resultado.getResultList();
            
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatMunicipio");
            throw new SGPException("Problema al obtener los registros" + ex);
        }
        finally{
            log.info("Finaliza proceso de obtener todos los municipios");
            super.close(em);
        }
        return municipios;
    }
    
    public synchronized CatMunicipio buscarPorId(Integer id) throws SGPException{
        
        EntityManager em = null;
        CatMunicipio municipio = null;
        
        try{
          log.info("Inicia proceso de obtener elemento con id: {}", id);
          em = super.getEntityManager();
          em.getTransaction().begin();
          municipio = em.find(CatMunicipio.class, id);
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
        return municipio;
    }
    
    public synchronized void guardar(CatMunicipio municipio) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de guardar el registro en la tabla CatMunicipio");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.persist(municipio);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun proble al guardar el registro {} en la tabla CatMunicipio", municipio);
            throw new SGPException("Problema al guardar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de guardar el registro en la tabla CatMunicipio");
        }
    }
    
    public synchronized void actualizar(CatMunicipio municipio) throws SGPException{
        
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de actualizar el registro de la tabla CatMunicipio");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.merge(municipio);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al actualizar el registro {} en la tabla CatMunicipio", municipio);
            throw new SGPException("Problema al actualizar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatMunicipio");
        }
    }
    
    public synchronized void eliminar(CatMunicipio municipio) throws SGPException{
        EntityManager em = null;
        
        try{
            log.info("Inicia proceso de eliminar el registro de la tabla CatMunicipio");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(municipio) ? municipio : em.merge(municipio));
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al eliminar el registro {} de la tabla CatMunicipio", municipio);
            throw new SGPException("Problema al eliminar el registro " + ex);
        }
        finally{
            super.close(em);
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatMunicipio");
        }
    }
}
