package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetTokenDTO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetToken;
import mx.com.ferbo.util.SGPException;

public class DetTokenDAO extends IBaseDAO<DetTokenDTO, Integer> implements Serializable{

	private static final long serialVersionUID = 1L;
	private Logger log = LogManager.getLogger(DetTokenDAO.class);
	
	public static synchronized DetTokenDTO getDTO(DetToken model) {
		DetTokenDTO dto = null;
		try {
			dto = new DetTokenDTO();
			dto.setIdToken(model.getIdToken());
			dto.setDetEmpleadoDTO(new EmpleadoDAO().getDTO(model.getEmpleado()));
			dto.setNbToken(model.getNbToken());
			dto.setCaducidad(model.getCaducidad());
			dto.setValido(model.isValido());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public static synchronized DetTokenDTO getDTO(DetToken model, boolean isFullInfo) {
		DetTokenDTO dto = null;
		try {
			dto = new DetTokenDTO();
			dto.setIdToken(model.getIdToken());
			dto.setNbToken(model.getNbToken());
			dto.setCaducidad(model.getCaducidad());
			dto.setValido(model.isValido());
			if(isFullInfo == false)
				return dto;
			dto.setDetEmpleadoDTO(new EmpleadoDAO().getDTO(model.getEmpleado()));
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public static synchronized DetToken getModel(DetTokenDTO dto) {
		DetToken model = null;
		
		try {
			model = new DetToken();
			model.setIdToken(dto.getIdToken());
			model.setEmpleado(new EmpleadoDAO().getModel(dto.getDetEmpleadoDTO()));
			model.setNbToken(dto.getNbToken());
			model.setCaducidad(dto.getCaducidad());
			model.setValido(dto.isValido());
		} catch(Exception ex) {
			model = null;
		}
		
		return model;
	}
	
	public static synchronized DetToken getModel(DetTokenDTO dto, boolean isFullInfo) {
		DetToken model = null;
		try {
			model = new DetToken();
			model.setIdToken(dto.getIdToken());
			model.setNbToken(dto.getNbToken());
			model.setCaducidad(dto.getCaducidad());
			model.setValido(dto.isValido());
			
			if(isFullInfo == false)
				return model;
			
			model.setEmpleado(new EmpleadoDAO().getModel(dto.getDetEmpleadoDTO()));
		} catch(Exception ex) {
			model = null;
		}
		
		return model;
	}
	
	@Override
	public DetTokenDTO buscarPorId(Integer id) {
		return null;
	}
	
	public DetTokenDTO buscarPorNumEmpleado(String numeroEmpleado) {
		
		List<DetTokenDTO> token = emSGP.createNamedQuery("DetToken.findIdEmpleado", DetTokenDTO.class).setParameter("numEmpleado",numeroEmpleado).getResultList();
		return token.isEmpty() ? new DetTokenDTO() : token.get(0);
	}
	
	public DetTokenDTO buscarPorToken(String token) {
		DetTokenDTO dto = null;
		DetToken model = null;
		try {
			model = emSGP.createNamedQuery("DetToken.findByToken",DetToken.class)
					.setParameter("token", token)
					.getSingleResult()
					;
			dto = getDTO(model);
		} catch(Exception ex) {
			log.error("Problema para obtener el token...", ex);
		}
		return dto;
	}
	
	public DetTokenDTO buscarPorIdEmpleadoAndFecha(Integer idEmpleado) {
		DetToken model = null;
		DetTokenDTO dto = null;
		String query = null;
		
		try {
			log.info("Consultando empleado con native query...");
			query = "SELECT t.* FROM det_token_empleado t "
					+ "INNER JOIN ( "
					+ "	SELECT "
					+ "		t2.id_empleado, "
					+ "		MAX(t2.caducidad) AS caducidad "
					+ "	FROM det_token_empleado t2 "
					+ "	GROUP BY t2.id_empleado "
					+ ") t3 ON t.id_empleado = t3.id_empleado AND t.caducidad = t3.caducidad "
					+ "WHERE t.id_empleado = :idEmpleado";
			
			model = (DetToken) emSGP.createNativeQuery(query, DetToken.class)
					.setParameter("idEmpleado", idEmpleado)
					.getSingleResult()
					;
			
			dto = getDTO(model);
			
		} catch(Exception ex) {
			log.error("Problema para obtener el empleado con id: " + idEmpleado, ex);
			dto = null;
		}
		
		return dto;
	}
	
	public DetTokenDTO buscarPorIdEmpleado(Integer idEmpleado) {
		DetToken model = null;
		DetTokenDTO dto = null;
		String query = null;
		
		try {
			log.info("Consultando empleado con native query...");
			query = "SELECT t.* FROM det_token_empleado t "
					+ "INNER JOIN ( "
					+ "	SELECT "
					+ "		t2.id_empleado, "
					+ "		MAX(t2.caducidad) AS caducidad "
					+ "	FROM det_token_empleado t2 "
					+ "	GROUP BY t2.id_empleado "
					+ ") t3 ON t.id_empleado = t3.id_empleado AND t.caducidad = t3.caducidad "
					+ "WHERE t. = :idEmpleado";
			
			model = (DetToken) emSGP.createNativeQuery(query, DetToken.class)
					.setParameter("idEmpleado", idEmpleado)
					.getSingleResult()
					;
			
			dto = getDTO(model);
			
		} catch(Exception ex) {
			log.error("Problema para obtener el empleado con id: " + idEmpleado, ex);
			dto = null;
		}
		
		return dto;
	}

	@Override
	public List<DetTokenDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetTokenDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetTokenDTO> buscarPorCriterios(DetTokenDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(DetTokenDTO e) throws SGPException {
		
		DetToken detToken = new DetToken();
		
		try {
			
			emSGP.getTransaction().begin();
			detToken.setIdToken(e.getIdToken());
			detToken.setEmpleado(emSGP.getReference(DetEmpleado.class, e.getDetEmpleadoDTO().getIdEmpleado()));
			detToken.setCaducidad(e.getCaducidad());
			detToken.setNbToken(e.getNbToken());
			detToken.setValido(e.isValido());
			emSGP.merge(detToken);
			emSGP.getTransaction().commit();
			emSGP.detach(detToken);
			
		} catch (Exception ex) {
			emSGP.getTransaction().rollback();
            log.warn("EX-0014: " + ex.getMessage() + ". Error al actualizar el token " );
            throw new SGPException("Problema para el registro del token.");
		}
		
	}

	@Override
	public void eliminar(DetTokenDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(DetTokenDTO e) throws SGPException {
	    final DetToken token = new DetToken();
        try {
            emSGP.getTransaction().begin();
            token.setEmpleado(emSGP.getReference(DetEmpleado.class,e.getDetEmpleadoDTO().getIdEmpleado()));
            token.setNbToken(e.getNbToken());
            token.setCaducidad(e.getCaducidad());
            token.setValido(e.isValido());
            emSGP.persist(token);
            emSGP.getTransaction().commit();
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0013: " + ex.getMessage() + ". Error al guardar la huella del empleado " + e.getDetEmpleadoDTO().getNumEmpleado() != null ? e.getDetEmpleadoDTO().getNumEmpleado() : null);
        }
		
	}
}
