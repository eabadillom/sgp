package mx.com.ferbo.dao;

import java.util.List;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.RiesgoPuestoDAO;
import mx.com.ferbo.dao.sat.EntidadFederativaDAO;
import mx.com.ferbo.dao.sat.RegimenFiscalDAO;
import mx.com.ferbo.dao.sat.TipoContratoDAO;
import mx.com.ferbo.dao.sat.TipoJornadaDAO;
import mx.com.ferbo.dao.sat.TipoRegimenDAO;
import mx.com.ferbo.dao.sat.UsoCfdiDAO;
import mx.com.ferbo.dto.NominaReceptorDTO;
import mx.com.ferbo.model.DetNominaReceptor;

public class NominaReceptorDAO extends DAO<NominaReceptorDTO, DetNominaReceptor, Integer> {

	@Override
	public NominaReceptorDTO getDTO(DetNominaReceptor model) {
		NominaReceptorDTO dto = null;
		try {
			dto = new NominaReceptorDTO();
			dto.setNombre(model.getNombre());
			dto.setRfc(model.getRfc());
			dto.setCodigoPostal(model.getCodigoPostal());
			dto.setRegimenFiscal(new RegimenFiscalDAO().getDTO(model.getRegimenFiscal()));
			dto.setUsoCfdi(new UsoCfdiDAO().getDTO(model.getUsoCfdi()));
			dto.setCurp(model.getCurp());
			dto.setNss(model.getNss());
			dto.setInicioRelacionLaboral(model.getInicioRelacionLaboral());
			dto.setAntiguedad(model.getAntiguedad());
			dto.setTipoContrato(new TipoContratoDAO().getDTO(model.getTipoContrato()));
			dto.setSindicalizado(model.getSindicalizado());
			dto.setTipoJornada(new TipoJornadaDAO().getDTO(model.getTipoJornada()));
			dto.setTipoRegimen(new TipoRegimenDAO().getDTO(model.getTipoRegimen()));
			dto.setNumeroEmpleado(model.getNumeroEmpleado());
			dto.setDepartamento(model.getDepartamento());
			dto.setPuesto(model.getPuesto());
			dto.setRiesgoPuesto(new RiesgoPuestoDAO().getDTO(model.getRiesgoPuesto()));
			dto.setPeriodicidadPago(new PeriodicidadPagoDAO().getDTO(model.getPeriodicidadPago()));
			dto.setSalarioDiario(model.getSalarioDiario());
			dto.setSalarioDiarioIntegrado(model.getSalarioDiarioIntegrado());
			dto.setEntidadFederativa(new EntidadFederativaDAO().getDTO(model.getEntidadFederativa()));
			
		} catch(Exception ex) {
			dto = null;
		}
		
		return dto;
	}

	@Override
	public DetNominaReceptor getModel(NominaReceptorDTO dto) {
		DetNominaReceptor model = null;
		
		try {
			model = new DetNominaReceptor();
			model.setNombre(dto.getNombre());
			model.setRfc(dto.getRfc());
			model.setCodigoPostal(dto.getCodigoPostal());
			model.setRegimenFiscal(new RegimenFiscalDAO().getModel(dto.getRegimenFiscal()));
			model.setUsoCfdi(new UsoCfdiDAO().getModel(dto.getUsoCfdi()));
			model.setCurp(dto.getCurp());
			model.setNss(dto.getNss());
			model.setInicioRelacionLaboral(dto.getInicioRelacionLaboral());
			model.setAntiguedad(dto.getAntiguedad());
			model.setTipoContrato(new TipoContratoDAO().getModel(dto.getTipoContrato()));
			model.setSindicalizado(dto.getSindicalizado());
			model.setTipoJornada(new TipoJornadaDAO().getModel(dto.getTipoJornada()));
			model.setTipoRegimen(new TipoRegimenDAO().getModel(dto.getTipoRegimen()));
			model.setNumeroEmpleado(dto.getNumeroEmpleado());
			model.setDepartamento(dto.getDepartamento());
			model.setPuesto(dto.getPuesto());
			model.setRiesgoPuesto(new RiesgoPuestoDAO().getModel(dto.getRiesgoPuesto()));
			model.setPeriodicidadPago(new PeriodicidadPagoDAO().getModel(dto.getPeriodicidadPago()));
			model.setSalarioDiario(dto.getSalarioDiario());
			model.setSalarioDiarioIntegrado(dto.getSalarioDiarioIntegrado());
			model.setEntidadFederativa(new EntidadFederativaDAO().getModel(dto.getEntidadFederativa()));
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public NominaReceptorDTO buscarPorId(Integer id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaReceptorDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaReceptorDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaReceptorDTO> buscarPorCriterios(NominaReceptorDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
