package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTarifaISR;

public class TarifaISRDAO extends BaseDAO<CatTarifaISR, Integer> {
	
	private static Logger log = LogManager.getLogger(TarifaISRDAO.class);

	public TarifaISRDAO(Class<CatTarifaISR> modelClass) {
		super(modelClass);
	}
	
	public List<CatTarifaISR> buscar(Date fechaInicio, Date fechaFin, String tipo) {
    	List<CatTarifaISR> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("CatTarifaISR.findByTipoAnioBaseISR", modelClass)
    				.setParameter("fechaInicio", fechaInicio)
    				.setParameter("fechaFin", fechaFin)
    				.setParameter("tipo", tipo)
    				.getResultList()
    				;
    		
    	} catch(NoResultException ex) {
    		log.error("No se encontró la lista de tarifas de ISR para el periodo indicado...", ex);
    		modelList = new ArrayList<>();
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }

}
