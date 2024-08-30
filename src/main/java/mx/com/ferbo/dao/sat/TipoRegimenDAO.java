package mx.com.ferbo.dao.sat;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoRegimenDTO;
import mx.com.ferbo.model.sat.CatTipoRegimen;

@Deprecated
public class TipoRegimenDAO extends DAO<TipoRegimenDTO, CatTipoRegimen, String> {
	private static Logger log = LogManager.getLogger(TipoRegimenDAO.class);

	@Override
	public TipoRegimenDTO getDTO(CatTipoRegimen model) {
		TipoRegimenDTO dto = null;
		try {
			dto = new TipoRegimenDTO();
			dto.setClave(model.getClave());
			dto.setNombre(model.getNombre());
			dto.setVigenciaInicio(model.getVigenciaInicio());
			dto.setVigenciaFin(model.getVigenciaFin());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public CatTipoRegimen getModel(TipoRegimenDTO dto) {
		CatTipoRegimen model = null;
		try {
			model = new CatTipoRegimen();
			model.setClave(dto.getClave());
			model.setNombre(dto.getNombre());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public TipoRegimenDTO buscarPorId(String id) {
		TipoRegimenDTO dto = null;
		CatTipoRegimen model = null;
		EntityManager em = null;
		try {
			em = getEntityManager();
			model = em.find(CatTipoRegimen.class, id);
			dto = this.getDTO(model);
		} catch(Exception ex) {
			dto = null;
			log.warn("No es posible obtener el tipo de régimen solicitado: {}", id);
		} finally {
			close(em);
		}
		return dto;
	}

	@Override
	public List<TipoRegimenDTO> buscarTodos() {
		List<TipoRegimenDTO> dtoList = null;
		List<CatTipoRegimen> modelList = null;
		EntityManager em = null;
		try {
			em = getEntityManager();
			modelList = em.createNamedQuery("CatTipoRegimen.findAll", CatTipoRegimen.class)
					.getResultList()
					;
			dtoList = toDTOList(modelList);
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de tipos de régimen...", ex);
		} finally {
			close(em);
		}
		return dtoList;
	}

	@Override
	public List<TipoRegimenDTO> buscarActivo() {
		throw new  UnsupportedOperationException("Not supported yet.");
	}
	
	public List<TipoRegimenDTO> buscarActivo(Date fecha) {
		List<TipoRegimenDTO> dtoList = null;
		List<CatTipoRegimen> modelList = null;
		EntityManager em = null;
		try {
			em = getEntityManager();
			modelList = em.createNamedQuery("CatTipoRegimen.findActive", CatTipoRegimen.class)
					.setParameter("fecha", fecha)
					.getResultList()
					;
			
			dtoList = toDTOList(modelList);
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de tipos de régimen...", ex);
		} finally {
			close(em);
		}
		return dtoList;
	}

	@Override
	public List<TipoRegimenDTO> buscarPorCriterios(TipoRegimenDTO dto) {
		throw new  UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public TipoRegimenDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}

}
