package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;

public class RiesgoPuestoDAO extends BaseDAO<CatRiesgoPuesto, String>{

	private static Logger log = LogManager.getLogger(RiesgoPuestoDAO.class);
	
	public RiesgoPuestoDAO(Class<CatRiesgoPuesto> modelClass) {
		super(modelClass);
	}
	
	public List<CatRiesgoPuesto> buscarTodos() {
		List<CatRiesgoPuesto> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatRiesgoPuesto.findByAll", modelClass)
					.getResultList()
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de riesgos de puesto.");
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
