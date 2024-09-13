package mx.com.ferbo.dao.n;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.CatCuotaIMSSPK;

public class CuotaIMSSDAO extends BaseDAO<CatCuotaIMSS, CatCuotaIMSSPK> {
	private static Logger log = LogManager.getLogger(CuotaIMSSDAO.class);

	public CuotaIMSSDAO(Class<CatCuotaIMSS> modelClass) {
		super(modelClass);
	}
	
	public CuotaIMSSDAO() {
		super(CatCuotaIMSS.class);
	}
	
	public CatCuotaIMSS buscarPor(String tipoCuota, String clave, Date fechaInicio, Date fechaFin, BigDecimal base) {
		CatCuotaIMSS tarifa = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			tarifa = em.createNamedQuery("CatCuotaIMSS.findByClavePeriodoBase", CatCuotaIMSS.class)
					.setParameter("clave", clave)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.setParameter("base", base)
					.setParameter("tipoCuota", tipoCuota)
					.getSingleResult()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la cuota del IMSS...", ex);
		} finally {
			this.close(em);
		}
		
		return tarifa;
	}
}
