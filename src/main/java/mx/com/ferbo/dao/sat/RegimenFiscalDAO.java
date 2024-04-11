package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.sat.RegimenFiscalDTO;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.util.SGPException;

public class RegimenFiscalDAO extends IBaseDAO<RegimenFiscalDTO, String>{
	
	private static Logger log = LogManager.getLogger(RegimenFiscalDAO.class);
	
	public static synchronized RegimenFiscalDTO getDTO(CatRegimenFiscal model) {
		RegimenFiscalDTO dto = null;
		try {
			dto = new RegimenFiscalDTO();
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setPersonaFisica(model.getPersonaFisica());
			dto.setPersonaMoral(model.getPersonaMoral());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
		} catch(Exception ex) {
			dto = null;
		}
		
		return dto;
	}
	
	public static synchronized CatRegimenFiscal getModel(RegimenFiscalDTO dto) {
		CatRegimenFiscal model = null;
		
		try {
			model = new CatRegimenFiscal();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setPersonaFisica(dto.getPersonaFisica());
			model.setPersonaMoral(dto.getPersonaMoral());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
		} catch(Exception ex) {
			model = null;
		}
		
		return model;
	}

	@Override
	public RegimenFiscalDTO buscarPorId(String id) {
		RegimenFiscalDTO dto = null;
		CatRegimenFiscal model = null;
		try {
			model = emSGP.find(CatRegimenFiscal.class, id);
			dto = getDTO(model);
		} catch(Exception ex) {
			dto = null;
			log.warn("No es posible obtener el regimen fiscal solicitado: {}", id);
		}
		return dto;
	}

	@Override
	public List<RegimenFiscalDTO> buscarTodos() {
		List<RegimenFiscalDTO> list = null;
		List<CatRegimenFiscal> result = null;
		
		try {
			result = emSGP.createNamedQuery("CatRegimenFiscal.findAll", CatRegimenFiscal.class)
					.getResultList();
			list = new ArrayList<RegimenFiscalDTO>();
			for(CatRegimenFiscal model : result) {
				RegimenFiscalDTO dto = getDTO(model);
				list.add(dto);
			}
		} catch(Exception ex) {
			log.error("Probleam para obtener la lista de regímenes fiscales", ex);
		}
		
		return list;
	}

	@Override
	public List<RegimenFiscalDTO> buscarActivo() {
		throw new  UnsupportedOperationException("Not supported yet.");
	}
	
	public List<RegimenFiscalDTO> buscarActivo(Date fecha) {
		List<RegimenFiscalDTO> list = null;
		List<CatRegimenFiscal> result = null;
		
		try {
			result = emSGP.createNamedQuery("CatRegimenFiscal.findByActivos", CatRegimenFiscal.class)
					.setParameter("fecha", fecha)
					.getResultList();
			list = new ArrayList<RegimenFiscalDTO>();
			for(CatRegimenFiscal model : result) {
				RegimenFiscalDTO dto = getDTO(model);
				list.add(dto);
			}
		} catch(Exception ex) {
			log.error("Probleam para obtener la lista de regímenes fiscales", ex);
		}
		
		return list;
	}

	@Override
	public List<RegimenFiscalDTO> buscarPorCriterios(RegimenFiscalDTO e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public void actualizar(RegimenFiscalDTO e) throws SGPException {
		CatRegimenFiscal model = null;
		try {
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.merge(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para guardar el régimen fiscal...", ex);
			emSGP.getTransaction().rollback();
		}
	}

	@Override
	public void eliminar(RegimenFiscalDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(RegimenFiscalDTO e) throws SGPException {
		CatRegimenFiscal model = null;
		try {
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.persist(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			emSGP.getTransaction().rollback();
		}
	}
}
