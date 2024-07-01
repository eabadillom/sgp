package mx.com.ferbo.commons.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.SGPException;

public abstract class DAO<DTO, MODEL, PK> {
	private static Logger log = LogManager.getLogger(DAO.class);
	
	protected Class<MODEL> modelClass;
	
	public DAO() {
		super();
	}
	
	public DAO(Class<MODEL> modelClass) {
		this.modelClass = modelClass;
	}
	
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
	
	public List<DTO> toDTOList(List<MODEL> modelList) {
		List<DTO> dtoList;
		dtoList = new ArrayList<DTO>();
		for(MODEL model : modelList) {
			DTO dto = this.getDTO(model);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public List<MODEL> toModelList(List<DTO> dtoList) {
		List<MODEL> modelList;
		modelList = new ArrayList<MODEL>();
		for(DTO dto : dtoList) {
			MODEL model = this.getModel(dto);
			modelList.add(model);
		}
		return modelList;
	}
	    
	public DTO buscarPorId(PK id) {
		DTO dto = null;
		MODEL model;
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			model = em.find(modelClass, id);
			dto = this.getDTO(model);
		} catch(Exception ex) {
			log.warn("Problema para obtener el elemento por ID: {}", id);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return dto;
	}
	
	public abstract DTO buscarPorId(PK id, boolean isFullInfo);
	public abstract List<DTO> buscarTodos();
	
	public synchronized void guardar(DTO dto) throws SGPException {
		EntityManager em = null;
		MODEL model = null;
		try {
			log.info("Guardando objeto: {}", dto);
			em = getEntityManager();
			model = this.getModel(dto);
			em.getTransaction().begin();
			em.persist(model);
			em.getTransaction().commit();
			log.info("Objeto guardado correctamente: {}", dto);
		} catch(Exception ex) {
			rollback(em);
			log.error("Problema para guardar el objeto: " + dto, ex);
		} finally {
			close(em);
		}
		
	}
	public synchronized void actualizar(DTO dto)throws SGPException {
		EntityManager em = null;
		MODEL model = null;
		try {
			log.info("Actualizando objeto: {}", dto);
			em = getEntityManager();
			model = this.getModel(dto);
			em.getTransaction().begin();
			model = em.merge(model);
			em.getTransaction().commit();
			dto = this.getDTO(model);
			log.info("Objeto actualizado correctamente: {}", dto);
		} catch(Exception ex) {
			rollback(em);
			log.error("Problema para actualizar el objeto: " + dto, ex);
		} finally {
			close(em);
		}
	}
	public synchronized void eliminar(DTO dto) throws SGPException {
		EntityManager em = null;
		MODEL model = null;
		try {
			log.info("Eliminando objeto: {}", dto);
			em = getEntityManager();
			model = this.getModel(dto);
			em.getTransaction().begin();
			em.remove(model);
			em.getTransaction().commit();
			log.info("Objeto eliminado correctamente: {}", dto);
		} catch(Exception ex) {
			rollback(em);
			log.error("Probleam para eliminar el objeto: " + dto, ex);
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
    public abstract List<DTO> buscarActivo(); //TODO Por compatibilidad con la clase IBaseDAO, se deja este método. Se pretende eliminar a futuro.
	
	@Deprecated
    public abstract List<DTO> buscarPorCriterios(DTO dto); //TODO Por compatibilidad con la clase IBaseDAO, se deja este metodo. Se pretende eliminar a futuro.
}
