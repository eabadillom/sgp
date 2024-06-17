package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.NominaDAO;
import mx.com.ferbo.dao.TipoPercepcionDAO;
import mx.com.ferbo.dto.NominaPercepcionDTO;
import mx.com.ferbo.dto.NominaPercepcionDTOPK;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;

public class NominaPercepcionDAO extends DAO<NominaPercepcionDTO, DetNominaPercepcion, DetNominaPercepcionPK> {

	@Override
	public NominaPercepcionDTO getDTO(DetNominaPercepcion model) {
		NominaPercepcionDTO dto = null;
		try {
			dto = new NominaPercepcionDTO();
			dto.setKey(new NominaPercepcionDTOPK(model.getKey().getId(), new NominaDAO().getDTO(model.getKey().getNomina(), false)));
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
	public DetNominaPercepcion getModel(NominaPercepcionDTO dto) {
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
	
	public List<NominaPercepcionDTO> getDTOList(List<DetNominaPercepcion> modelList) {
		List<NominaPercepcionDTO> dtoList = null;
		try {
			dtoList = new ArrayList<>();
			for(DetNominaPercepcion model : modelList) {
				NominaPercepcionDTO dto = this.getDTO(model);
				dtoList.add(dto);
			}
		} catch(Exception ex) {
			dtoList = new ArrayList<>();
		}
		return dtoList;
	}

	@Override
	public NominaPercepcionDTO buscarPorId(DetNominaPercepcionPK id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaPercepcionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaPercepcionDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaPercepcionDTO> buscarPorCriterios(NominaPercepcionDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
