package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatSubsidio;

public class SubsidioDAO extends BaseDAO<CatSubsidio, Integer> {
	
	private static Logger log = LogManager.getLogger(SubsidioDAO.class);

	public SubsidioDAO(Class<CatSubsidio> modelClass) {
		super(modelClass);
	}
	
	public List<CatSubsidio> buscar(Date fechaInicio, Date fechaFin, String periodo) {
    	List<CatSubsidio> modelList = null;
    	EntityManager emSGP = null;
    	
    	try {
    		emSGP = this.getEntityManager();
    		modelList = emSGP.createNamedQuery("CatSubsidio.findByPeriodoTipo", modelClass)
    				.setParameter("fechaInicio", fechaInicio)
    				.setParameter("fechaFin", fechaFin)
    				.setParameter("periodo", periodo)
    				.getResultList()
    				;
    	} catch(NoResultException ex) {
    		log.error("No se encontró la tabla de subsidio solicitada...", ex);
    		modelList = new ArrayList<>();
    	} finally {
    		this.close(emSGP);
    	}
    	
    	return modelList;
    }
}
