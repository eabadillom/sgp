package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.ConceptoDAO;
import mx.com.ferbo.dao.sat.UnidadSATDAO;
import mx.com.ferbo.model.DetNominaConcepto;
import mx.com.ferbo.model.DetNominaConceptoPK;
import mx.com.ferbo.model.NominaConcepto;
import mx.com.ferbo.model.NominaConceptoPK;

public class NominaConceptoDAO extends DAO<NominaConcepto, DetNominaConcepto, DetNominaConceptoPK>{

	@Override
	public NominaConcepto getDTO(DetNominaConcepto model) {
		NominaConcepto dto = null;
		try {
			dto = new NominaConcepto();
			dto.setKey(new NominaConceptoPK(model.getKey().getId(), new NominaDAO().buscarPorId(model.getKey().getNomina().getId(), false)));
			dto.setConcepto(new ConceptoDAO().getDTO(model.getConcepto()));
			dto.setCantidad(model.getCantidad());
			dto.setUnidad(new UnidadSATDAO().getDTO(model.getUnidad()));
			dto.setNombreConcepto(model.getNombreConcepto());
			dto.setObjetoImpuesto(model.getObjetoImpuesto());
			dto.setValorUnitario(model.getValorUnitario());
			dto.setImporte(model.getImporte());
			dto.setDescuento(model.getDescuento());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public DetNominaConcepto getModel(NominaConcepto dto) {
		DetNominaConcepto model = null;
		try {
			model = new DetNominaConcepto();
			model.setKey(new DetNominaConceptoPK(new NominaDAO().getModel(dto.getKey().getNomina()), dto.getKey().getIdConcepto()));
			model.setConcepto(new ConceptoDAO().getModel(dto.getConcepto()));
			model.setCantidad(dto.getCantidad());
			model.setUnidad(new UnidadSATDAO().getModel(dto.getUnidad()));
			model.setNombreConcepto(dto.getNombreConcepto());
			model.setObjetoImpuesto(dto.getObjetoImpuesto());
			model.setValorUnitario(dto.getValorUnitario());
			model.setImporte(dto.getImporte());
			model.setDescuento(dto.getDescuento());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public List<NominaConcepto> getDTOList(List<DetNominaConcepto> modelList) {
		List<NominaConcepto> dtoList = null;
		try {
			dtoList = new ArrayList<>();
			for(DetNominaConcepto model : modelList) {
				NominaConcepto dto = this.getDTO(model);
				dtoList.add(dto);
			}
		} catch(Exception ex) {
			dtoList = new ArrayList<>();
		}
		return dtoList;
	}

	@Override
	public NominaConcepto buscarPorId(DetNominaConceptoPK id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaConcepto> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaConcepto> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaConcepto> buscarPorCriterios(NominaConcepto dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
