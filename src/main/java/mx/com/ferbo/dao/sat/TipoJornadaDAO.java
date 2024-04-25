package mx.com.ferbo.dao.sat;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoJornadaDTO;
import mx.com.ferbo.model.sat.CatTipoJornada;

public class TipoJornadaDAO extends DAO<TipoJornadaDTO, CatTipoJornada, String> {
	
	private static Logger log = LogManager.getLogger(TipoJornadaDAO.class);

	@Override
	public TipoJornadaDTO getDTO(CatTipoJornada model) {
		TipoJornadaDTO dto = null;
		try {
			dto = new TipoJornadaDTO();
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
		} catch(Exception ex) {
			dto = null;
		}
			
		return dto;
	}

	@Override
	public CatTipoJornada getModel(TipoJornadaDTO dto) {
		CatTipoJornada model = null;
		try {
			model = new CatTipoJornada();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public TipoJornadaDTO buscarPorId(String id) {
		TipoJornadaDTO dto = null;
		CatTipoJornada model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.find(CatTipoJornada.class, id);
			dto = this.getDTO(model);
		} catch(Exception ex) {
			log.error("No es posible recuperar el model solicitado...", ex);
		} finally {
			close(em);
		}
		
		return dto;
	}

	@Override
	public List<TipoJornadaDTO> buscarTodos() {
		List<TipoJornadaDTO> dtoList = null;
		List<CatTipoJornada> modelList = null;
		EntityManager em = null;
		
		try {
			em = getEntityManager();
			modelList = em.createNamedQuery("CatTipoJornada.findAll", CatTipoJornada.class)
					.getResultList()
					;
			dtoList = this.toDTOList(modelList);
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de tipos de contrato...", ex);
		} finally {
			close(em);
		}
		
		return dtoList;
	}

	@Override
	public List<TipoJornadaDTO> buscarActivo() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<TipoJornadaDTO> buscarPorCriterios(TipoJornadaDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
