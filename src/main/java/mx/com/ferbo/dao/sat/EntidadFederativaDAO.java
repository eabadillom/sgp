package mx.com.ferbo.dao.sat;

import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.EntidadFederativaDTO;
import mx.com.ferbo.model.sat.CatEntidadFederativa;

public class EntidadFederativaDAO extends DAO<EntidadFederativaDTO, CatEntidadFederativa, String> {

	@Override
	public EntidadFederativaDTO getDTO(CatEntidadFederativa model) {
		EntidadFederativaDTO dto = null;
		
		try {
			dto = new EntidadFederativaDTO();
			dto.setClave(model.getClave());
			dto.setPais(model.getPais());
			dto.setNombre(model.getNombre());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
		} catch(Exception ex) {
			dto = null;
		}
		
		return dto;
	}

	@Override
	public CatEntidadFederativa getModel(EntidadFederativaDTO dto) {
		CatEntidadFederativa model = null;
		
		try {
			model = new CatEntidadFederativa();
			model.setClave(dto.getClave());
			model.setPais(dto.getPais());
			model.setNombre(dto.getNombre());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public EntidadFederativaDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadFederativaDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadFederativaDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadFederativaDTO> buscarPorCriterios(EntidadFederativaDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
