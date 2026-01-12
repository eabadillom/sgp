package mx.com.ferbo.dao.n;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatSubsidio2;

public class Subsidio2DAO extends BaseDAO<CatSubsidio2, Integer> {
	
	private static Logger log = LogManager.getLogger(Subsidio2DAO.class);

	public Subsidio2DAO(Class<CatSubsidio2> modelClass) {
		super(modelClass);
	}
	
	public Subsidio2DAO() {
		super(CatSubsidio2.class);
	}
	
	public CatSubsidio2 buscarVigente(LocalDate fecha) {
		CatSubsidio2 model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("Subsidio2.buscarVigente", this.modelClass)
					.setParameter("fecha", fecha)
					.getSingleResult()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la tasa de subsidio...", ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}
	
	public List<CatSubsidio2> buscarTodos() {
		List<CatSubsidio2> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("Subsidio2.buscarTodos", modelClass).getResultList();
		} catch(Exception ex) {
			log.error("Problema para consultar la lista de Subsidios");
		} finally {
			close(em);
		}
		
		return modelList;
	}
}
