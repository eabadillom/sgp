package mx.com.ferbo.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.MetodoPagoDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.Nomina;

public class NominaDAO extends DAO<Nomina, DetNomina, Integer> {
	
	private static Logger log = LogManager.getLogger(NominaDAO.class);

	@Override
	public Nomina getDTO(DetNomina model) {
		Nomina dto = null;
		
		try {
			dto = new Nomina();
			dto.setId(model.getId());
			dto.setMoneda(model.getMoneda());
			dto.setFechaEmision(model.getFechaEmision());
			dto.setNumeroCertificado(model.getNumeroCertificado());
			dto.setFechaTimbrado(model.getFechaTimbrado());
			dto.setUuid(model.getUuid());
			dto.setIdPac(model.getIdPac());
			dto.setTipoComprobante(model.getTipoComprobante());
			dto.setClaveExportacion(model.getClaveExportacion());
			dto.setMetodoPago(new MetodoPagoDAO().getDTO(model.getMetodoPago()));
			dto.setSerie(model.getSerie());
			dto.setFolio(model.getFolio());
			dto.setSubtotal(model.getSubtotal());
			dto.setDescuento(model.getDescuento());
			dto.setTotal(model.getTotal());
			dto.setEmisor(new NominaEmisorDAO().getDTO(model.getEmisor()));
			dto.setReceptor(new NominaReceptorDAO().getDTO(model.getReceptor()));
			dto.setConceptos(new NominaConceptoDAO().getDTOList(model.getConceptos()));
			dto.setPercepciones(new NominaPercepcionDAO().getDTOList(model.getPercepciones()));
			dto.setOtrosPagos(new NominaOtroPagoDAO().getDTOList(model.getOtrosPagos()));
			dto.setDeducciones(new NominaDeduccionDAO().getDTOList(model.getDeducciones()));
			
		} catch(Exception ex) {
			log.warn("Problema para obtener la información de la nómina: " + model.getId(), ex.getMessage());
		} finally {
			dto = null;
		}
		
		return dto;
	}
	
	public Nomina getDTO(DetNomina model, boolean isFullInfo) {
		Nomina dto = null;
		
		try {
			dto = new Nomina();
			dto.setId(model.getId());
			dto.setMoneda(model.getMoneda());
			dto.setFechaEmision(model.getFechaEmision());
			dto.setNumeroCertificado(model.getNumeroCertificado());
			dto.setFechaTimbrado(model.getFechaTimbrado());
			dto.setUuid(model.getUuid());
			dto.setIdPac(model.getIdPac());
			dto.setTipoComprobante(model.getTipoComprobante());
			dto.setClaveExportacion(model.getClaveExportacion());
			dto.setSerie(model.getSerie());
			dto.setFolio(model.getFolio());
			dto.setSubtotal(model.getSubtotal());
			dto.setDescuento(model.getDescuento());
			dto.setTotal(model.getTotal());
			if(isFullInfo) {
				dto.setMetodoPago(new MetodoPagoDAO().getDTO(model.getMetodoPago()));
				dto.setEmisor(new NominaEmisorDAO().buscarPorNomina(model.getId(), true));
				
			}
		} catch(Exception ex) {
			log.warn("Problema para obtener la información de la nómina: " + model.getId(), ex.getMessage());
		} finally {
			dto = null;
		}
		
		return dto;
	}

	@Override
	public DetNomina getModel(Nomina dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Nomina buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Nomina> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Nomina> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Nomina> buscarPorCriterios(Nomina dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Nomina buscarPorId(Integer id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
