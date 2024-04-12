package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoContratoDTO;
import mx.com.ferbo.model.sat.CatTipoContrato;
import mx.com.ferbo.util.SGPException;

public class TipoContratoDAO extends DAO<TipoContratoDTO, CatTipoContrato, Integer> {
	
	private static Logger log = LogManager.getLogger(TipoContratoDAO.class);

	@Override
	public synchronized TipoContratoDTO getDTO(CatTipoContrato model) {
		TipoContratoDTO dto = null;
			try {
				dto = new TipoContratoDTO();
				
				dto.setClave(model.getClave());
				dto.setNombre(model.getNombre());
			} catch(Exception ex ) {
				dto = null;
			}
		return dto;
	}

	@Override
	public synchronized CatTipoContrato getModel(TipoContratoDTO dto) {
		CatTipoContrato model = null;;
		try {
			model = new CatTipoContrato();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public TipoContratoDTO buscarPorId(Integer id) {
		TipoContratoDTO dto = null;
		CatTipoContrato model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.find(CatTipoContrato.class, id);
			dto = this.getDTO(model);
		} catch(Exception ex) {
			log.error("No es posible recuperar el model solicitado...", ex);
		} finally {
			close(em);
		}
		
		return dto;
	}

	@Override
	public List<TipoContratoDTO> buscarTodos() {
		List<TipoContratoDTO> list = null;
		List<CatTipoContrato> result = null;
		EntityManager em = null;
		
		try {
			em = getEntityManager();
			result = em.createNamedQuery("CatTipoContrato.findAll", CatTipoContrato.class)
					.getResultList()
					;
			list = new ArrayList<TipoContratoDTO>();
			for(CatTipoContrato model : result) {
				TipoContratoDTO dto = getDTO(model);
				list.add(dto);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de tipos de contrato...", ex);
		} finally {
			close(em);
		}
		
		return list;
	}

	@Override
	public void eliminar(TipoContratoDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TipoContratoDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoContratoDTO> buscarPorCriterios(TipoContratoDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

}
