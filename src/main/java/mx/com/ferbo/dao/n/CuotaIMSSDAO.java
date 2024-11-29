package mx.com.ferbo.dao.n;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatCuotaIMSS;

public class CuotaIMSSDAO extends BaseDAO<CatCuotaIMSS, Integer> {
	private static Logger log = LogManager.getLogger(CuotaIMSSDAO.class);

	public CuotaIMSSDAO(Class<CatCuotaIMSS> modelClass) {
		super(modelClass);
	}

	public CuotaIMSSDAO() {
		super(CatCuotaIMSS.class);
	}

	public List<CatCuotaIMSS> obtenerLista() {
		List<CatCuotaIMSS> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatCuotaIMSS.findAll", CatCuotaIMSS.class).getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener la cuota del IMSS...", ex);
		} finally {
			this.close(em);
		}

		return modelList;
	}

	public CatCuotaIMSS buscarPor(String tipoCuota, String clave, Date fechaInicio, Date fechaFin, BigDecimal base) {
		CatCuotaIMSS tarifa = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			tarifa = em.createNamedQuery("CatCuotaIMSS.findByClavePeriodoBase", CatCuotaIMSS.class)
					.setParameter("clave", clave).setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin).setParameter("base", base).setParameter("tipoCuota", tipoCuota)
					.getSingleResult();
		} catch (Exception ex) {
			log.error("Problema para obtener la cuota del IMSS...", ex);
		} finally {
			this.close(em);
		}

		return tarifa;
	}

	public List<CatCuotaIMSS> buscarPorPeriodo(Date fecha) {
		List<CatCuotaIMSS> modelList = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatCuotaIMSS.findByPeriodo", modelClass).setParameter("fecha", fecha)
					.getResultList();
		} catch (Exception ex) {
			log.error("No es posible obtener las cuotas del IMSS vigentes...", ex);
		} finally {
			this.close(em);
		}
		return modelList;
	}

	@SuppressWarnings("unchecked")
	public List<CatCuotaIMSS> buscarPorAnio(Integer anio) {
		List<CatCuotaIMSS> modelList = null;
		EntityManager em = null;
		String query = null;
		try {
			query = "select\n"
					+ "	id_cuota, cd_cuota, nb_clave, nb_cuota, nb_base_salarial, nu_base_min, nu_base_max, nu_cuota, nb_tipo_cuota, fh_vigencia_ini, fh_vigencia_fin\n"
					+ "from\n"
					+ "	cat_cuota_imss cci\n"
					+ "where YEAR (cci.fh_vigencia_ini) = :anio";
			em = this.getEntityManager();
			modelList = em.createNativeQuery(query, modelClass)
					.setParameter("anio", anio).getResultList();
		} catch (Exception ex) {
			log.error("No es posible obtener las cuotas del IMSS vigentes...", ex);
		} finally {
			this.close(em);
		}
		return modelList;
	}
}
