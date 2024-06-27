package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.TipoDeduccionDAO;
import mx.com.ferbo.dto.NominaDeduccionDTO;
import mx.com.ferbo.dto.NominaDeduccionDTOPK;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;

public class NominaDeduccionDAO extends DAO<NominaDeduccionDTO, DetNominaDeduccion, DetNominaDeduccionPK> {

	@Override
	public NominaDeduccionDTO getDTO(DetNominaDeduccion model) {
		NominaDeduccionDTO dto = null;
		try {
			dto = new NominaDeduccionDTO();
			dto.setKey(new NominaDeduccionDTOPK(model.getKey().getId(), new NominaDAO().buscarPorId(model.getKey().getNomina().getId(), false)));
			dto.setTipoDeduccion(new TipoDeduccionDAO().getDTO(model.getTipoDeduccion()));
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setImporte(model.getImporte());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public DetNominaDeduccion getModel(NominaDeduccionDTO dto) {
		DetNominaDeduccion model = null;
		try {
			model = new DetNominaDeduccion();
			model.setKey(new DetNominaDeduccionPK(new NominaDAO().getModel(dto.getKey().getNomina()), dto.getKey().getIdDeduccion()));
			model.setTipoDeduccion(new TipoDeduccionDAO().getModel(dto.getTipoDeduccion()));
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setImporte(dto.getImporte());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public List<NominaDeduccionDTO> getDTOList(List<DetNominaDeduccion> modelList) {
		List<NominaDeduccionDTO> dtoList = null;
		try {
			dtoList = new ArrayList<>();
			for(DetNominaDeduccion model: modelList) {
				NominaDeduccionDTO dto = this.getDTO(model);
				dtoList.add(dto);
			}
		} catch(Exception ex) {
			dtoList = new ArrayList<>();
		}
		return dtoList;
	}

	@Override
	public NominaDeduccionDTO buscarPorId(DetNominaDeduccionPK id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaDeduccionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaDeduccionDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaDeduccionDTO> buscarPorCriterios(NominaDeduccionDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
