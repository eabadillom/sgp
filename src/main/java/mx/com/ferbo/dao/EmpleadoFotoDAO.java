package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.EmpleadoFotoDTO;
import mx.com.ferbo.model.DetEmpleadoFoto;

public class EmpleadoFotoDAO extends DAO<EmpleadoFotoDTO, DetEmpleadoFoto, Integer> {
	
	private static Logger log = LogManager.getLogger();

	@Override
	public EmpleadoFotoDTO getDTO(DetEmpleadoFoto model) {
		EmpleadoFotoDTO dto = null;
		try {
			dto = new EmpleadoFotoDTO();
			dto.setId(model.getId());
			dto.setFotografia(model.getFotografia());
		} catch(Exception ex) {
			dto = null;
		}
		
		return dto;
	}

	@Override
	public DetEmpleadoFoto getModel(EmpleadoFotoDTO dto) {
		DetEmpleadoFoto model = null;
		try {
			model = new DetEmpleadoFoto();
			model.setId(dto.getId());
			model.setFotografia(dto.getFotografia());
		} catch(Exception ex) {
			model = null;
		}
		
		return model;
	}

	@Override
	public EmpleadoFotoDTO buscarPorId(Integer id) {
		EmpleadoFotoDTO dto = null;
		DetEmpleadoFoto model = null;
		EntityManager em = null;
		
		try {
			em = getEntityManager();
			model = em.find(DetEmpleadoFoto.class, id);
			
			dto = this.getDTO(model);
		} catch(Exception ex) {
			log.warn("Problema para obtener la foto del empleado: {}", id);
		} finally {
			close(em);
		}
		
		return dto;
	}

	@Override
	public List<EmpleadoFotoDTO> buscarTodos() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarTodos'");
	}

	@Override
	public List<EmpleadoFotoDTO> buscarActivo() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
	}

	@Override
	public List<EmpleadoFotoDTO> buscarPorCriterios(EmpleadoFotoDTO dto) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
	}

	@Override
	public EmpleadoFotoDTO buscarPorId(Integer id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}

}
