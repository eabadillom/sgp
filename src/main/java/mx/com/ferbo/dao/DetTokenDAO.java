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
			dto.setDetEmpleadoDTO(EmpleadoDAO.getDTO(model.getEmpleado()));
			dto.setNbToken(model.getNbToken());
			dto.setCaducidad(model.getCaducidad());
			dto.setValido(model.isValido());
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
			model.setEmpleado(EmpleadoDAO.getModel(dto.getDetEmpleadoDTO()));
			model.setNbToken(dto.getNbToken());
			model.setCaducidad(dto.getCaducidad());
			model.setValido(dto.isValido());
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
	
	public DetTokenDTO buscarPorIdEmpleadoAndFecha(Integer idEmpleado) {
		DetToken model = null;
		DetTokenDTO token = null;
		
		model = emSGP.createNamedQuery("DetToken.findByEmpleadoAndFecha", DetToken.class)
				.setParameter("idEmpleado", idEmpleado)
				.getSingleResult()
				;
		
		token = getDTO(model);
		return token;
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
