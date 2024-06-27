package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.RegimenFiscalDAO;
import mx.com.ferbo.dto.NominaEmisorDTO;
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.util.EntityManagerUtil;

public class NominaEmisorDAO extends DAO<NominaEmisorDTO, DetNominaEmisor, Integer> {
	
	private static Logger log = LogManager.getLogger(NominaEmisorDAO.class);
	
	@Override
	public NominaEmisorDTO getDTO(DetNominaEmisor model) {
		NominaEmisorDTO dto = null;
		try {
			dto = new NominaEmisorDTO();
			dto.setNomina(new NominaDAO().getDTO(model.getNomina()));
			dto.setNombre(model.getNombre());
			dto.setRfc(model.getRfc());
			dto.setCodigoPostal(model.getCodigoPostal());
			dto.setRegistroPatronal(model.getRegistroPatronal());
			dto.setRegimenFiscal(new RegimenFiscalDAO().getDTO(model.getRegimenFiscal()));
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public NominaEmisorDTO getDTO(DetNominaEmisor model, boolean isFullInfo) {
		NominaEmisorDTO dto = null;
		try {
			dto = new NominaEmisorDTO();
			dto.setNomina(new NominaDAO().getDTO(model.getNomina()));
			dto.setNombre(model.getNombre());
			dto.setRfc(model.getRfc());
			dto.setCodigoPostal(model.getCodigoPostal());
			dto.setRegistroPatronal(model.getRegistroPatronal());
			
			if(isFullInfo) {
				dto.setRegimenFiscal(new RegimenFiscalDAO().getDTO(model.getRegimenFiscal()));
			}
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public DetNominaEmisor getModel(NominaEmisorDTO dto) {
		DetNominaEmisor model = null;
		try {
			model = new DetNominaEmisor();
			model.setNomina(new NominaDAO().getModel(dto.getNomina()));
			model.setNombre(dto.getNombre());
			model.setRfc(dto.getRfc());
			model.setCodigoPostal(dto.getCodigoPostal());
			model.setRegistroPatronal(dto.getRegistroPatronal());
			model.setRegimenFiscal(new RegimenFiscalDAO().getModel(dto.getRegimenFiscal()));
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public DetNominaEmisor getModel(NominaEmisorDTO dto, boolean isFullInfo) {
		DetNominaEmisor model = null;
		try {
			model = new DetNominaEmisor();
			model.setNomina(new NominaDAO().getModel(dto.getNomina()));
			model.setNombre(dto.getNombre());
			model.setRfc(dto.getRfc());
			model.setCodigoPostal(dto.getCodigoPostal());
			model.setRegistroPatronal(dto.getRegistroPatronal());
			if(isFullInfo) {
				model.setRegimenFiscal(new RegimenFiscalDAO().getModel(dto.getRegimenFiscal()));
			}
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public NominaEmisorDAO() {
		
	}

	@Override
	public NominaEmisorDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public NominaEmisorDTO buscarPorId(Integer id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaEmisorDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaEmisorDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominaEmisorDTO> buscarPorCriterios(NominaEmisorDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public NominaEmisorDTO buscarPorNomina(Integer id, boolean isFullInfo) {
		NominaEmisorDTO dto = null;
		DetNominaEmisor model = null;
		EntityManager em = null;
		
		
		try {
			em = EntityManagerUtil.getEntityManager();
			model = em.createNamedQuery("DetNominaEmisor.buscarPorNomina", DetNominaEmisor.class)
				.setParameter("idNomina", id)
				.getSingleResult()
				;
			
			if(isFullInfo == false) {
				this.getDTO(model, false);
				return dto;
			}
			
			dto = this.getDTO(model,true);
			
		} catch(Exception ex) {
			log.warn("Problema para obtener el listado de emisores de nómina...", ex.getMessage());
		} finally {
			
		}
		
		return dto;
	}

}
