package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;

public class EmpleadoDAO extends BaseDAO<DetEmpleado, Integer> {
	
	private static Logger log = LogManager.getLogger(EmpleadoDAO.class);

	public EmpleadoDAO(Class<DetEmpleado> modelClass) {
		super(modelClass);
	}
	
	public DetEmpleado buscarPorId(Integer id, boolean isFullInfo, boolean isGetFoto) {
		DetEmpleado model = null;
    	EntityManager emSGP = null;
    	
    	try {
    		emSGP = getEntityManager();
    		model = emSGP.find(this.modelClass, id);
    		if(isFullInfo) {
    			log.info("id dato empresa: {}", model.getDatoEmpresa().getId() == null ? null : model.getDatoEmpresa().getId());
    		}
    		
    		if(isGetFoto) {
    			log.info("id foto: {}", model.getEmpleadoFoto() == null ? null : model.getEmpleadoFoto().getId());
    		}
    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado con id " + id, ex);
    	} finally {
    		close(emSGP);
    	}
    	
    	return model;
	}
	
	public DetEmpleado buscarPorNumeroEmpleado(String numeroEmpleado, boolean isFullInfo) {
		DetEmpleado model = null;
    	EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		model = emSGP.createNamedQuery("DetEmpleado.findByNumero", modelClass)
    				.setParameter("numero",numeroEmpleado)
    				.getSingleResult()
    				;
    		if(isFullInfo) {
    			log.info("Dato empresa: {}", model.getDatoEmpresa().getId());
    			log.info("Perfil: {}", model.getDatoEmpresa().getPerfil().getIdPerfil());
    		}
    	} catch(Exception ex) {
    		log.error("Problema para obtener el número de empleado " + numeroEmpleado, ex);
    	} finally {
    		close(emSGP);
    	}
		
		return model;
	}
	
	public DetEmpleado buscarPorNumeroEmpleadoFoto(String numeroEmpleado, boolean isFullInfo) {
		DetEmpleado model = null;
    	EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		model = emSGP.createNamedQuery("DetEmpleado.findByNumero", modelClass)
    				.setParameter("numero",numeroEmpleado)
    				.getSingleResult()
    				;
    		if(isFullInfo) {
    			log.info("Dato empresa: {}", model.getDatoEmpresa().getId());
    			log.info("Perfil: {}", model.getDatoEmpresa().getPerfil().getIdPerfil());
    			log.info("Foto id: {}", model.getEmpleadoFoto().getId());
    		}
    	} catch(Exception ex) {
    		log.error("Problema para obtener el número de empleado " + numeroEmpleado, ex);
    	} finally {
    		close(emSGP);
    	}
		
		return model;
	}
	public List<DetEmpleado> buscarActivoEmpresaIngreso(Integer idEmpresa, Date periodoPagoInicio) {
    	List<DetEmpleado> modelList = null;
    	EntityManager emSGP = null;
    	
    	try {
    		emSGP = this.getEntityManager();
    		
    		modelList = emSGP.createNamedQuery("DetEmpleado.findByActiveEmpresaIngreso", modelClass)
    				.setParameter("idEmpresa", idEmpresa)
    				.setParameter("periodoPagoInicio", periodoPagoInicio)
    				.getResultList()
    				;
    		
    	} catch(Exception ex) {
    		log.warn("Problema para obtener la lista de empleados...", ex);
    	} finally {
    		close(emSGP);
    		
    	}
    	
    	return modelList;
	}
	
	public List<DetEmpleado> buscarTodos(boolean isFullInfo) {
		List<DetEmpleado> modelList = null;
        EntityManager emSGP = null;
        
        try {
        	emSGP = getEntityManager();
        	modelList = emSGP.createNamedQuery("DetEmpleado.getAll", DetEmpleado.class)
        			.getResultList();
        	
        	for(DetEmpleado model : modelList) {
        		
        		if(isFullInfo == false) {
        			continue;
        		}
        		
        		log.info("id area: {}", model.getDatoEmpresa().getArea().getIdArea());
        		log.info("id dato empresa: {}", model.getDatoEmpresa());
        	}
        } catch(Exception ex) {
        	log.error("Problema para obtener el listado de empleados...", ex);
        } finally {
        	close(emSGP);
        }
        
        return modelList;
	}
	
	public DetEmpleadoFoto buscarFoto(String numeroEmpleado) {
		DetEmpleadoFoto foto = null;
		
		return foto;
	}
	

}
