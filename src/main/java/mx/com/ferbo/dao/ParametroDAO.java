package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.ParametroDTO;
import mx.com.ferbo.model.CatParametro;

@Deprecated
public class ParametroDAO extends DAO<ParametroDTO, CatParametro, Integer> {
	private static Logger log = LogManager.getLogger(ParametroDAO.class);
	
	public ParametroDAO() {
		super(CatParametro.class);
	}
	
	@Override
	public ParametroDTO getDTO(CatParametro model) {
		ParametroDTO dto = null;
		try {
			dto = new ParametroDTO();
			dto.setId(model.getId());
			dto.setClave(model.getClave());
			dto.setValor(model.getValor());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}

	@Override
	public CatParametro getModel(ParametroDTO dto) {
		CatParametro model = null;
		try {
			model = new CatParametro();
			model.setId(dto.getId());
			model.setClave(dto.getClave());
			model.setValor(dto.getValor());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public ParametroDTO buscarPorId(Integer id, boolean isFullInfo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public ParametroDTO buscarPorClave(String clave) {
		ParametroDTO dto = null;
		CatParametro model = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("CatParametro.buscarPorClave", CatParametro.class)
					.setParameter("clave", clave)
					.getSingleResult()
					;
			dto = this.getDTO(model);
		} catch(Exception ex) {
			log.warn("Problema para obtener el parametro: {}", clave);
		} finally {
			close(em);
		}
		return dto;
	}

	@Override
	public List<ParametroDTO> buscarTodos() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<ParametroDTO> buscarActivo() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<ParametroDTO> buscarPorCriterios(ParametroDTO dto) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	

}
