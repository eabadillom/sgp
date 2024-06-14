package mx.com.ferbo.dao.sat;

import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.MetodoPagoDTO;
import mx.com.ferbo.model.sat.CatMetodoPago;

public class MetodoPagoDAO extends DAO<MetodoPagoDTO, CatMetodoPago, String> {

	@Override
	public MetodoPagoDTO getDTO(CatMetodoPago model) {
		MetodoPagoDTO dto = null;
		try {
			dto = new MetodoPagoDTO();
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public CatMetodoPago getModel(MetodoPagoDTO dto) {
		CatMetodoPago model = null;
		try {
			model = new CatMetodoPago();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public MetodoPagoDTO buscarPorId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetodoPagoDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetodoPagoDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetodoPagoDTO> buscarPorCriterios(MetodoPagoDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetodoPagoDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}

}
