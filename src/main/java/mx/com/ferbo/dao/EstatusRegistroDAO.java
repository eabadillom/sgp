package mx.com.ferbo.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatEstatusRegistroDTO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.util.SGPException;

public class EstatusRegistroDAO extends IBaseDAO<CatEstatusRegistroDTO, Integer> {
	
	private static Logger log = LogManager.getLogger(EstatusRegistroDAO.class);
	
	public static synchronized CatEstatusRegistroDTO getDTO(CatEstatusRegistro model) {
		CatEstatusRegistroDTO dto = null;
		
		try {
			dto = new CatEstatusRegistroDTO();
			dto.setIdEstatus(model.getIdEstatus());
			dto.setDescripcion(model.getDescripcion());
			dto.setActivo(model.getActivo());
		} catch(Exception ex) {
			log.warn("No es posible convertir Model a DTO: {}", ex.getMessage());
			dto = null;
		}
		
		return dto;
	}
	
	public static synchronized CatEstatusRegistro getModel(CatEstatusRegistroDTO dto) {
		CatEstatusRegistro model = null;
		
		try {
			model = new CatEstatusRegistro();
			model.setIdEstatus(dto.getIdEstatus());
			model.setDescripcion(dto.getDescripcion());
			model.setActivo(dto.getActivo());
		} catch(Exception ex) {
			log.warn("No es posible convertir DTO a Model: {}", ex.getMessage());
			model = null;
		}
		
		return model;
	}

	@Override
	public CatEstatusRegistroDTO buscarPorId(Integer id) {
		
		return null;
	}

	@Override
	public List<CatEstatusRegistroDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CatEstatusRegistroDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CatEstatusRegistroDTO> buscarPorCriterios(CatEstatusRegistroDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(CatEstatusRegistroDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(CatEstatusRegistroDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(CatEstatusRegistroDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

}
