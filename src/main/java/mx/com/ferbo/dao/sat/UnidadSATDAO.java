package mx.com.ferbo.dao.sat;

import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.UnidadSATDTO;
import mx.com.ferbo.model.sat.CatUnidadSAT;

public class UnidadSATDAO extends DAO<UnidadSATDTO, CatUnidadSAT, String> {

	@Override
	public UnidadSATDTO getDTO(CatUnidadSAT model) {
		UnidadSATDTO dto = null;
		try {
			dto = new UnidadSATDTO();
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setDescripcion(model.getDescripcion());
			dto.setNota(model.getNota());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
			dto.setSimbolo(model.getSimbolo());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public CatUnidadSAT getModel(UnidadSATDTO dto) {
		CatUnidadSAT model = null;
		try {
			model = new CatUnidadSAT();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setDescripcion(dto.getDescripcion());
			model.setNota(dto.getNota());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
			model.setSimbolo(dto.getSimbolo());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public UnidadSATDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnidadSATDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnidadSATDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnidadSATDTO> buscarPorCriterios(UnidadSATDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
