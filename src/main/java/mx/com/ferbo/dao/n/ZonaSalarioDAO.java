package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatZonaSalario;

public class ZonaSalarioDAO extends BaseDAO<CatZonaSalario, String> {
	
	private static Logger log = LogManager.getLogger(ZonaSalarioDAO.class);

	public ZonaSalarioDAO(Class<CatZonaSalario> modelClass) {
		super(modelClass);
	}
	
	public ZonaSalarioDAO() {
		super(CatZonaSalario.class);
	}
	
	public List<CatZonaSalario> buscarTodos() {
		List<CatZonaSalario> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("ZonaSalario.buscarTodos", this.modelClass)
					.getResultList();
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de zonas de salario...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
