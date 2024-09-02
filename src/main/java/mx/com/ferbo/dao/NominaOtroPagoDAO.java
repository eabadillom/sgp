package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.TipoOtroPagoDAO;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.NominaOtroPago;
import mx.com.ferbo.model.NominaOtroPagoPK;

@Deprecated
public class NominaOtroPagoDAO extends DAO<NominaOtroPago, DetNominaOtroPago, DetNominaOtroPagoPK> {

	@Override
	public NominaOtroPago getDTO(DetNominaOtroPago model) {
		NominaOtroPago dto = null;
		try {
			dto = new NominaOtroPago();
			dto.setKey(new NominaOtroPagoPK(new NominaDAO().buscarPorId(model.getKey().getNomina().getId(), false), model.getKey().getId()));
//			dto.setTipoOtroPago(new TipoOtroPagoDAO().getDTO(model.getTipoOtroPago()));
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setImporte(model.getImporte());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public NominaOtroPago getDTO(DetNominaOtroPago model, boolean isFullInfo) {
		NominaOtroPago dto = null;
		try {
			dto = new NominaOtroPago();
			dto.setKey(new NominaOtroPagoPK(new NominaDAO().buscarPorId(model.getKey().getNomina().getId(), false), model.getKey().getId()));
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setImporte(model.getImporte());
			if(isFullInfo == true) {
//				dto.setTipoOtroPago(new TipoOtroPagoDAO().getDTO(model.getTipoOtroPago()));
			}
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public DetNominaOtroPago getModel(NominaOtroPago dto) {
		DetNominaOtroPago model = null;
		try {
			model = new DetNominaOtroPago();
			model.setKey(new DetNominaOtroPagoPK(new NominaDAO().getModel(dto.getKey().getNomina()), dto.getKey().getId()));
//			model.setTipoOtroPago(new TipoOtroPagoDAO().getModel(dto.getTipoOtroPago()));
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setImporte(dto.getImporte());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public DetNominaOtroPago getModel(NominaOtroPago dto, boolean isFullInfo) {
		DetNominaOtroPago model = null;
		try {
			model = new DetNominaOtroPago();
			model.setKey(new DetNominaOtroPagoPK(new NominaDAO().getModel(dto.getKey().getNomina()), dto.getKey().getId()));
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setImporte(dto.getImporte());
			
			if(isFullInfo == true) {
//				model.setTipoOtroPago(new TipoOtroPagoDAO().getModel(dto.getTipoOtroPago()));
			}
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public List<NominaOtroPago> getDTOList(List<DetNominaOtroPago> modelList) {
		List<NominaOtroPago> dtoList = null;
		
		try {
			dtoList = new ArrayList<>();
			for(DetNominaOtroPago model : modelList) {
				NominaOtroPago dto = this.getDTO(model, false);
				dtoList.add(dto);
			}
			
		} catch(Exception ex) {
			dtoList = new ArrayList<>();
		}
		return dtoList;
	}

	@Override
	public NominaOtroPago buscarPorId(DetNominaOtroPagoPK id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaOtroPago> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaOtroPago> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaOtroPago> buscarPorCriterios(NominaOtroPago dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
