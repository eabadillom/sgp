package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSalarioDiario;

public class SalarioDiarioDAO extends BaseDAO<DetSalarioDiario, Integer> {
	
	private static final Logger log = LogManager.getLogger(SalarioDiarioDAO.class);
	
	public SalarioDiarioDAO() {
		super(DetSalarioDiario.class);
	}

	private SalarioDiarioDAO(Class<DetSalarioDiario> modelClass) {
		super(DetSalarioDiario.class);
	}
	
	public List<DetSalarioDiario> buscar(DetEmpleado empleado) {
		List<DetSalarioDiario> modelList;
		EntityManager em = null;
		String query = null;
		
		try {
			query = "SELECT sd FROM DetSalarioDiario sd "
					+ "WHERE sd.datoEmpresa = :datoEmpresa "
					+ "ORDER BY sd.datoEmpresa.id ASC, sd.fechaRegistro ASC";
			
			em = this.getEntityManager();
			
			modelList = em.createQuery(query, DetSalarioDiario.class)
					.setParameter("datoEmpresa", empleado.getDatoEmpresa())
					.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el histórico de salarios diarios del empleado...", ex);
			modelList = new ArrayList<DetSalarioDiario>();
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
