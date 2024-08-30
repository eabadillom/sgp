package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.TipoDeduccionDAO;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.NominaDeduccion;
import mx.com.ferbo.model.NominaDeduccionPK;

public class NominaDeduccionDAO extends DAO<NominaDeduccion, DetNominaDeduccion, DetNominaDeduccionPK> {

	@Override
	public NominaDeduccion getDTO(DetNominaDeduccion model) {
		NominaDeduccion dto = null;
		try {
			dto = new NominaDeduccion();
			dto.setKey(new NominaDeduccionPK(model.getKey().getId(), new NominaDAO().buscarPorId(model.getKey().getNomina().getId(), false)));
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
	public DetNominaDeduccion getModel(NominaDeduccion dto) {
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
	
	public List<NominaDeduccion> getDTOList(List<DetNominaDeduccion> modelList) {
		List<NominaDeduccion> dtoList = null;
		try {
			dtoList = new ArrayList<>();
			for(DetNominaDeduccion model: modelList) {
				NominaDeduccion dto = this.getDTO(model);
				dtoList.add(dto);
			}
		} catch(Exception ex) {
			dtoList = new ArrayList<>();
		}
		return dtoList;
	}

	@Override
	public NominaDeduccion buscarPorId(DetNominaDeduccionPK id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaDeduccion> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaDeduccion> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaDeduccion> buscarPorCriterios(NominaDeduccion dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
