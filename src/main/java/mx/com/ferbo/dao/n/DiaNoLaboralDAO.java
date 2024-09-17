package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;

public class DiaNoLaboralDAO extends BaseDAO<CatDiaNoLaboral, Integer> {
	
	private static Logger log = LogManager.getLogger(DiaNoLaboralDAO.class);

	public DiaNoLaboralDAO(Class<CatDiaNoLaboral> modelClass) {
		super(modelClass);
	}
	
	public List<CatDiaNoLaboral> buscarPorPeriodo(String clavePais, Date fechaInicio, Date fechaFin) {
		List<CatDiaNoLaboral> list = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			list = em.createNamedQuery("CatDiaNoLaboral.buscaPorPeriodo", CatDiaNoLaboral.class)
					.setParameter("clavePais", clavePais)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.getResultList()
					;
			
			for(CatDiaNoLaboral d : list) {
				log.info("Pais: {}", d.getPais().getClavePais());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de dias no laborales del periodo " + fechaInicio +" al " + fechaFin, ex);
		} finally {
			this.close(em);
		}
		
		return list;
	}

}
