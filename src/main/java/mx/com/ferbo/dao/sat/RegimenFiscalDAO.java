package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.RegimenFiscalDTO;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.SGPException;

public class RegimenFiscalDAO extends DAO<RegimenFiscalDTO, CatRegimenFiscal, String>{
	
	private static Logger log = LogManager.getLogger(RegimenFiscalDAO.class);
	
	public RegimenFiscalDTO getDTO(CatRegimenFiscal model) {
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
	
	public CatRegimenFiscal getModel(RegimenFiscalDTO dto) {
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
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			model = em.find(CatRegimenFiscal.class, id);
			dto = getDTO(model);
		} catch(Exception ex) {
			dto = null;
			log.warn("No es posible obtener el regimen fiscal solicitado: {}", id);
		} finally {
			EntityManagerUtil.close(em);
		}
		return dto;
	}

	@Override
	public List<RegimenFiscalDTO> buscarTodos() {
		List<RegimenFiscalDTO> list = null;
		List<CatRegimenFiscal> result = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			result = em.createNamedQuery("CatRegimenFiscal.findAll", CatRegimenFiscal.class)
					.getResultList();
			list = new ArrayList<RegimenFiscalDTO>();
			for(CatRegimenFiscal model : result) {
				RegimenFiscalDTO dto = getDTO(model);
				list.add(dto);
			}
		} catch(Exception ex) {
			log.error("Probleam para obtener la lista de regímenes fiscales", ex);
		} finally {
			EntityManagerUtil.close(em);
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
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			result = em.createNamedQuery("CatRegimenFiscal.findByActivos", CatRegimenFiscal.class)
					.setParameter("fecha", fecha)
					.getResultList();
			list = new ArrayList<RegimenFiscalDTO>();
			for(CatRegimenFiscal model : result) {
				RegimenFiscalDTO dto = getDTO(model);
				list.add(dto);
			}
		} catch(Exception ex) {
			log.error("Probleam para obtener la lista de regímenes fiscales", ex);
		} finally {
			EntityManagerUtil.close(em);
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
		EntityManager em = null;
		try {
			model = getModel(e);
			em.getTransaction().begin();
			em.merge(model);
			em.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para guardar el régimen fiscal...", ex);
			em.getTransaction().rollback();
		} finally {
			EntityManagerUtil.close(em);
		}
	}

	@Override
	public void eliminar(RegimenFiscalDTO e) throws SGPException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
	}

	@Override
	public void guardar(RegimenFiscalDTO e) throws SGPException {
		CatRegimenFiscal model = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			model = getModel(e);
			em.getTransaction().begin();
			em.persist(model);
			em.getTransaction().commit();
		} catch(Exception ex) {
			em.getTransaction().rollback();
		} finally {
			EntityManagerUtil.close(em);
		}
	}

	@Override
	public RegimenFiscalDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}
}
