package mx.com.ferbo.commons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabo
 */
public abstract class IBaseDAO<E, ID> {

	private static Logger log = LogManager.getLogger(IBaseDAO.class);

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sgpPU");
    
    public static synchronized EntityManager getEntityManager() {
    	EntityManager em = null;
    	EntityManagerFactory factory = null;
    	try {
    		factory = Persistence.createEntityManagerFactory("sgpPU");
    		em = factory.createEntityManager();
    	} catch(Exception ex) {
    		log.error("Problema para obtener el entity maanger...", ex);
    		em = null;
    	}
    	return em;
    }
    
    public static synchronized void close(EntityManager em) {
    	if(em == null)
			return;
		
		if(em.isOpen())
			em.close();
		em = null;
		return;
    }

    @PersistenceContext(unitName = "sgpPU")
    protected EntityManager emSGP = emf.createEntityManager();

    public abstract E buscarPorId(ID id);

    public abstract List<E> buscarTodos();
    
    public abstract List<E> buscarActivo();

    public abstract List<E> buscarPorCriterios(E e);

    public abstract void actualizar(E e)throws SGPException;
    
    public abstract void eliminar(E e)throws SGPException;

    public abstract void guardar(E e) throws SGPException;
    
}
