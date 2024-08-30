package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetRegistro;

public class RegistroDAO extends BaseDAO<DetRegistro, Integer> {
	
	private static Logger log = LogManager.getLogger(RegistroDAO.class);

	public RegistroDAO(Class<DetRegistro> modelClass) {
		super(modelClass);
	}
	
	public DetRegistro buscarPorEmpleadoFechaEntrada(Integer idEmpleado, Date fechaEntradaInicio, Date fechaEntradaFin) {
		DetRegistro model = null;
		EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		model = emSGP.createNamedQuery("DetRegistro.findByIdEmpleadoAndFecha", DetRegistro.class)
    				.setParameter("idEmpleado", idEmpleado)
    				.setParameter("fechaEntradaInicio", fechaEntradaInicio)
    				.setParameter("fechaEntradaFin", fechaEntradaFin)
    				.getSingleResult()
    				;
    		log.debug("IdEmpleado: {}", model.getIdEmpleado().getIdEmpleado());
    		log.debug("Estatus: {}", model.getIdEstatus().getIdEstatus());
    	} catch(Exception ex) {
    		model = null;
    	} finally {
    		close(emSGP);
    	}
    	
    	return model;
	}
	
	public List<DetRegistro> buscar(Integer idEmpleado, Date fechaEntrada, Date fechaSalida){
		List<DetRegistro> modelList = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("DetRegistro.findByEmpleadoPeriodo", modelClass)
					.setParameter("idEmpleado", idEmpleado)
	    			.setParameter("fechaEntrada", fechaEntrada)
	    			.setParameter("fechaSalida", fechaSalida)
	    			.getResultList()
	    			;
			
			for(DetRegistro r : modelList) {
				log.info("Registro - idEmpleado: {}", r.getIdEmpleado().getIdEmpleado());
				log.info("Status registro: {}", r.getIdEstatus().getIdEstatus());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el registro de asistencia del empleado...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}
