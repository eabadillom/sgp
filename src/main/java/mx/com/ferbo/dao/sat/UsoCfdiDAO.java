package mx.com.ferbo.dao.sat;

import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.UsoCFDIDTO;
import mx.com.ferbo.model.sat.CatUsoCFDI;

public class UsoCfdiDAO extends DAO<UsoCFDIDTO, CatUsoCFDI, String> {

	@Override
	public UsoCFDIDTO getDTO(CatUsoCFDI model) {
		UsoCFDIDTO dto = null;
		
		try {
			dto = new UsoCFDIDTO();
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setAplicaPersonaFisica(model.getAplicaPersonaFisica());
			dto.setAplicaPersonaMoral(model.getAplicaPersonaMoral());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
			
		} catch(Exception ex) {
			dto = null;
		}
		
		return dto;
	}

	@Override
	public CatUsoCFDI getModel(UsoCFDIDTO dto) {
		CatUsoCFDI model = null;
		
		try {
			model = new CatUsoCFDI();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setAplicaPersonaFisica(dto.getAplicaPersonaFisica());
			model.setAplicaPersonaMoral(dto.getAplicaPersonaMoral());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
			
		} catch(Exception ex) {
			model = null;
		}
		
		return model;
	}

	@Override
	public UsoCFDIDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsoCFDIDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsoCFDIDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsoCFDIDTO> buscarPorCriterios(UsoCFDIDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
