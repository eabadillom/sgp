package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.DetNominaPeriodoPK;

public class NominaPeriodoDAO extends BaseDAO<DetNominaPeriodo, DetNominaPeriodoPK> {
	
	private static Logger log = LogManager.getLogger(NominaPeriodoDAO.class);

	public NominaPeriodoDAO(Class<DetNominaPeriodo> modelClass) {
		super(modelClass);
	}
	
	public NominaPeriodoDAO() {
		super(DetNominaPeriodo.class);
	}
	
	public List<DetNominaPeriodo>  buscar(Integer idEmpresa, String tipoNomina, String periodicidad, Integer anio) {
		List<DetNominaPeriodo> modelList = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetNominaPeriodo.buscarPorEmpresaTipoNominaPeriodicidadAnio", modelClass)
					.setParameter("idEmpresa", idEmpresa)
					.setParameter("tipoNomina", tipoNomina)
					.setParameter("periodicidad", periodicidad)
					.setParameter("anio", anio)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de periodos de nómina...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
	
	public DetNominaPeriodo buscarUltimo(Integer idEmpresa, String tipoNomina, String periodicidad, Integer anio) {
		DetNominaPeriodo model = null;
		EntityManager em = null;
		String query = null;
		
		try {
			query = "select dnp.* from det_nomina_periodo dnp "
					+ "inner join ( "
					+ "	select p1.id_empresa, p1.tp_nomina, p1.cd_periodicidad, p1.nu_anio, max(p1.nu_periodo) as nu_periodo "
					+ "	from det_nomina_periodo p1 "
					+ "	group by p1.id_empresa, p1.tp_nomina, p1.cd_periodicidad, p1.nu_anio "
					+ ") p on p.tp_nomina = dnp.tp_nomina and p.cd_periodicidad = dnp.cd_periodicidad and p.nu_anio = dnp.nu_anio and p.nu_periodo = dnp.nu_periodo "
					+ "where dnp.id_empresa = :idEmpresa and dnp.tp_nomina = :tpNomina and dnp.cd_periodicidad = :cdPeriodicidad and dnp.nu_anio = :anio "
					;
			em = this.getEntityManager();
			model = (DetNominaPeriodo) em.createNativeQuery(query, this.modelClass)
					.setParameter("idEmpresa", idEmpresa)
					.setParameter("tpNomina", tipoNomina)
					.setParameter("cdPeriodicidad", periodicidad)
					.setParameter("anio", anio)
					.getSingleResult()
					;
			
			log.debug("Id Empresa: {}", model.getKey().getEmpresa().getIdEmpresa());
			log.debug("Periodicidad: {}", model.getKey().getPeriodicidad().getPeriodicidad());
			
		} catch(NoResultException ex){
			log.warn("No hay resultados para el periodo solicitado: Tipo nomina={}, Periodicidad={}, Año={}", tipoNomina, periodicidad, anio);
		} catch(Exception ex) {
			log.error("Problema para obtener el último periodo de nómina solicitado...", ex);
		} finally {
			this.close(em);
		}
		
		return model;
	}
}
