package mx.com.ferbo.dao.sat;

import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.ConceptoDTO;
import mx.com.ferbo.model.sat.CatConcepto;

public class ConceptoDAO extends DAO<ConceptoDTO, CatConcepto, String> {

	@Override
	public ConceptoDTO getDTO(CatConcepto model) {
		ConceptoDTO dto = null;
		try {
			dto = new ConceptoDTO();
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setPalabraSimilar(model.getPalabraSimilar());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public CatConcepto getModel(ConceptoDTO dto) {
		CatConcepto model = null;
		try {
			model = new CatConcepto();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setPalabraSimilar(dto.getPalabraSimilar());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public ConceptoDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptoDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptoDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptoDTO> buscarPorCriterios(ConceptoDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
