package mx.com.ferbo.dao.n;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.NominaFormula;

public class NominaFormulaDAO extends BaseDAO<NominaFormula, Integer> {
	
	private static Logger log = LogManager.getLogger(NominaFormulaDAO.class);

	public NominaFormulaDAO(Class<NominaFormula> modelClass) {
		super(modelClass);
	}
	
	public NominaFormulaDAO() {
		super(NominaFormula.class);
	}
	
	public NominaFormula buscarVigente(String clave, LocalDate fecha) {
		NominaFormula formula = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			formula = em.createNamedQuery("NominaFormula.buscarVigente", this.modelClass)
					.setParameter("clave", clave)
					.setParameter("fecha", fecha)
					.getSingleResult()
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener la clase controladora del cálculo con clave " + clave, ex);
		} finally {
			this.close(em);
		}
		
		return formula;
	}
}
