package mx.com.ferbo.dao;

import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoPercepcionDTO;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

@Deprecated
public class TipoPercepcionDAO extends DAO<TipoPercepcionDTO, CatTipoPercepcion, String> {
	
	public TipoPercepcionDAO() {
		super(CatTipoPercepcion.class);
	}
	
	@Override
	public TipoPercepcionDTO getDTO(CatTipoPercepcion model) {
		TipoPercepcionDTO dto = null;
		try {
			dto = new TipoPercepcionDTO();
			dto.setClave(model.getClave());
			dto.setDescripcion(model.getDescripcion());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public CatTipoPercepcion getModel(TipoPercepcionDTO dto) {
		CatTipoPercepcion model = null;
		try {
			model = new CatTipoPercepcion();
			model.setClave(dto.getClave());
			model.setDescripcion(dto.getDescripcion());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public TipoPercepcionDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoPercepcionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoPercepcionDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoPercepcionDTO> buscarPorCriterios(TipoPercepcionDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
