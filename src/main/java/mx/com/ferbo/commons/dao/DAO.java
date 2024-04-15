package mx.com.ferbo.commons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.SGPException;

public abstract class DAO<DTO, MODEL, PK> {
	private static Logger log = LogManager.getLogger(DAO.class);
	
	public EntityManager getEntityManager() {
		EntityManager em = null;
		EntityManagerFactory emf = null;
		String PERSIST_UNIT = "sgpPU";
		try {
			emf = Persistence.createEntityManagerFactory(PERSIST_UNIT);
			em = emf.createEntityManager();
		} catch(Exception ex) {
			log.error("Problema para obtener el entity manager...", ex);
		}
		
		return em;
	}
	
	public abstract DTO getDTO(MODEL model);
	public abstract MODEL getModel(DTO dto);
	    
	public abstract DTO buscarPorId(PK id);
	public abstract List<DTO> buscarTodos();
	public synchronized void guardar(DTO dto) throws SGPException {
		EntityManager em = null;
		MODEL model = null;
		try {
			em = getEntityManager();
			model = this.getModel(dto);
			em.getTransaction().begin();
			em.persist(model);
			em.getTransaction().commit();
		} catch(Exception ex) {
			rollback(em);
		} finally {
			close(em);
		}
		
	}
	public synchronized void actualizar(DTO dto)throws SGPException {
		EntityManager em = null;
		MODEL model = null;
		try {
			em = getEntityManager();
			model = this.getModel(dto);
			em.getTransaction().begin();
			model = em.merge(model);
			em.getTransaction().commit();
			dto = this.getDTO(model);
		} catch(Exception ex) {
			rollback(em);
		} finally {
			close(em);
		}
	}
	public void eliminar(DTO dto) throws SGPException {
		EntityManager em = null;
		MODEL model = null;
		try {
			em = getEntityManager();
			model = this.getModel(dto);
			em.getTransaction().begin();
			em.remove(model);
			em.getTransaction().commit();
		} catch(Exception ex) {
			rollback(em);
		} finally {
			close(em);
		}
	}
	
	public static void close(EntityManager em) {
		if(em == null)
			return;
		
		if(em.isOpen())
			em.close();
		em = null;
		return;
	}
	
	public static void rollback(EntityManager em) {
		if(em == null)
			return;
		
		if(em.isOpen() == false)
			return;
		
		em.getTransaction().rollback();
	}
	
	@Deprecated
    public abstract List<DTO> buscarActivo(); //TODO Por compatibilidad se deja este método. Se pretende eliminar a futuro.
	
	@Deprecated
    public abstract List<DTO> buscarPorCriterios(DTO dto); //TODO Por compatibilidad se deja este metodo. Se pretende eliminar a futuro.
}
