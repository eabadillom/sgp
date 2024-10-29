
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetRolSistema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RolSistemaDAO extends BaseDAO {
    
    private static Logger log = LogManager.getLogger(DetRolSistema.class);
    EntityManager em = null;
    
    public RolSistemaDAO(Class<DetRolSistema> modelClass) {
        super(modelClass);
    }

    public RolSistemaDAO() {
        super(DetRolSistema.class);
    }
    
    public synchronized DetRolSistema buscarId(Integer id){
        DetRolSistema buscado = null;
        
        try{
            em = super.getEntityManager();
            
            em.getTransaction().commit();
            buscado = em.find(DetRolSistema.class, id);
        }
        catch(Exception ex){
            super.rollback(em);
            log.info("El elemento con id " + id + " no se encuentra en la base tabla DetRolSistema", ex);
        }
        finally{
            super.close(em);
        }
        
        return buscado;
    }

    public synchronized void eliminar(DetRolSistema rol){
        try{
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.remove(rol);
            em.getTransaction().commit(); 
        }
        catch(Exception ex){
            super.rollback(em);
            log.info("Problema al eliminar " + rol.toString() + " de la tabla DetRolSistema", ex);
        }
        finally{
            super.close(em);  
        }
    }

    public synchronized void actualizar(DetRolSistema rol){    
        try{
            
            em = super.getEntityManager();
            
            em.getTransaction().begin();
            em.merge(rol);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.info("Problema al actualizar " + rol.toString() + " en la tabla DetRolSistema", ex);
        }
        finally{
            super.close(em);
        }
        
    }

    public synchronized void guardar(DetRolSistema rol){
        try{
            em = super.getEntityManager();
         
            em.getTransaction().begin();
            em.persist(rol);
            em.getTransaction().commit();
        }
        catch(Exception ex){
            super.rollback(em);
            log.info("Problema al guerdar " + rol.toString() + " en la tabla DetRolSistema", ex);
        }
        finally{
            super.close(em);
        }
    }

    
    public List<DetRolSistema> obtenerTodos(){
        List<DetRolSistema> clientes = null;
        
        try{
            em = super.getEntityManager();
            
            TypedQuery<DetRolSistema> resultado =  em.createQuery("select e from DetRolSistema e", DetRolSistema.class);
            clientes = resultado.getResultList();
        }
        catch(Exception ex){
            super.rollback(em);
            log.info("Problema con la tabla DetRolSistema al momento de obtener los registros...", ex);
        }
        finally{
            super.close(em);
        }
        return clientes;
    }
}
