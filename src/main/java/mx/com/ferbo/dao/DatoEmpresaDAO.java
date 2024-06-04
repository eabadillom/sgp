package mx.com.ferbo.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.TipoContratoDAO;
import mx.com.ferbo.dao.sat.TipoJornadaDAO;
import mx.com.ferbo.dao.sat.TipoRegimenDAO;
import mx.com.ferbo.dto.DatoEmpresaDTO;
import mx.com.ferbo.model.InfDatoEmpresa;

public class DatoEmpresaDAO extends DAO<DatoEmpresaDTO, InfDatoEmpresa, Integer> {
	private static Logger log = LogManager.getLogger(DatoEmpresaDAO.class);

	@Override
	public DatoEmpresaDTO getDTO(InfDatoEmpresa model) {
		DatoEmpresaDTO dto = null;
		try {
			dto = new DatoEmpresaDTO();
			dto.setId(model.getId());
			dto.setPerfil(CatPerfilDAO.getDTO(model.getPerfil()));
			dto.setEmpresa(CatEmpresaDAO.getDTO(model.getEmpresa()));
			dto.setPlanta(CatPlantaDAO.getDTO(model.getPlanta()));
			dto.setArea(CatAreaDAO.getDTO(model.getArea()));
			dto.setPuesto(CatPuestoDAO.getDTO(model.getPuesto()));
			dto.setTipoContrato(new TipoContratoDAO().getDTO(model.getTipoContrato()));
			dto.setTipoJornada(new TipoJornadaDAO().getDTO(model.getTipoJornada()));
			dto.setTipoRegimen(new TipoRegimenDAO().getDTO(model.getTipoRegimen()));
			dto.setFechaIngreso(model.getFechaIngreso());
			dto.setNss(model.getNss());
			dto.setRfc(model.getRfc());
			dto.setSalarioDiario(model.getSalarioDiario());
			dto.setHoraEntrada(model.getHoraEntrada());
			dto.setMinutosTolerancia(model.getMinutosTolerancia());
			
		} catch(Exception ex) {
			dto = null;
		}
		
		return dto;
	}

	@Override
	public InfDatoEmpresa getModel(DatoEmpresaDTO dto) {
		InfDatoEmpresa model = null;
		
		try {
			model = new InfDatoEmpresa();
			model.setId(dto.getId());
			model.setPerfil(CatPerfilDAO.getModel(dto.getPerfil()));
			model.setEmpresa(CatEmpresaDAO.getModel(dto.getEmpresa()));
			model.setPlanta(CatPlantaDAO.getModel(dto.getPlanta()));
			model.setArea(CatAreaDAO.getModel(dto.getArea()));
			model.setPuesto(CatPuestoDAO.getModel(dto.getPuesto()));
			model.setTipoContrato(new TipoContratoDAO().getModel(dto.getTipoContrato()));
			model.setTipoJornada(new TipoJornadaDAO().getModel(dto.getTipoJornada()));
			model.setTipoRegimen(new TipoRegimenDAO().getModel(dto.getTipoRegimen()));
			model.setFechaIngreso(dto.getFechaIngreso());
			model.setNss(dto.getNss());
			model.setRfc(dto.getRfc());
			model.setSalarioDiario(dto.getSalarioDiario());
			model.setHoraEntrada(dto.getHoraEntrada());
			model.setMinutosTolerancia(dto.getMinutosTolerancia());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public DatoEmpresaDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatoEmpresaDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatoEmpresaDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatoEmpresaDTO> buscarPorCriterios(DatoEmpresaDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
