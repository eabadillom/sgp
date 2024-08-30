package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.NominaPercepcion;
import mx.com.ferbo.model.NominaPercepcionPK;

public class NominaPercepcionDAO extends DAO<NominaPercepcion, DetNominaPercepcion, DetNominaPercepcionPK> {

	@Override
	public NominaPercepcion getDTO(DetNominaPercepcion model) {
		NominaPercepcion dto = null;
		try {
			dto = new NominaPercepcion();
			dto.setKey(new NominaPercepcionPK(model.getKey().getId(), new NominaDAO().getDTO(model.getKey().getNomina(), false)));
			dto.setTipoPercepcion(new TipoPercepcionDAO().getDTO(model.getTipoPercepcion()));
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setImporteGravado(model.getImporteGravado());
			dto.setImporteExcento(model.getImporteExcento());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public DetNominaPercepcion getModel(NominaPercepcion dto) {
		DetNominaPercepcion model = null;
		try {
			model = new DetNominaPercepcion();
			model.setKey(new DetNominaPercepcionPK(new NominaDAO().getModel(dto.getKey().getNomina()), dto.getKey().getIdPercepcion()));
			model.setTipoPercepcion(new TipoPercepcionDAO().getModel(dto.getTipoPercepcion()));
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setImporteGravado(dto.getImporteGravado());
			model.setImporteExcento(dto.getImporteExcento());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public List<NominaPercepcion> getDTOList(List<DetNominaPercepcion> modelList) {
		List<NominaPercepcion> dtoList = null;
		try {
			dtoList = new ArrayList<>();
			for(DetNominaPercepcion model : modelList) {
				NominaPercepcion dto = this.getDTO(model);
				dtoList.add(dto);
			}
		} catch(Exception ex) {
			dtoList = new ArrayList<>();
		}
		return dtoList;
	}

	@Override
	public NominaPercepcion buscarPorId(DetNominaPercepcionPK id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaPercepcion> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaPercepcion> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaPercepcion> buscarPorCriterios(NominaPercepcion dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
