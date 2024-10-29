package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dao.sat.TipoDeduccionDAO;
import mx.com.ferbo.dto.TipoPrestamoDTO;
import mx.com.ferbo.model.CatTipoPrestamo;
import mx.com.ferbo.util.EntityManagerUtil;

@Deprecated
public class TipoPrestamoDAO extends DAO<TipoPrestamoDTO, CatTipoPrestamo, String> {
	
	private static Logger log = LogManager.getLogger(TipoPrestamoDAO.class);
	
	public TipoPrestamoDAO() {
		super(CatTipoPrestamo.class);
	}
	
	public synchronized TipoPrestamoDTO getDTO(CatTipoPrestamo model) {
		TipoPrestamoDTO dto = null;
		try {
			dto = new TipoPrestamoDTO();
			dto.setClave(model.getTipoPrestamo());
			dto.setDescripcion(model.getDescripcion());
			dto.setTipoDeduccion(new TipoDeduccionDAO().getDTO(model.getTipoDeduccion()));
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public synchronized CatTipoPrestamo getModel(TipoPrestamoDTO dto) {
		CatTipoPrestamo model = null;
		try {
			model = new CatTipoPrestamo();
			model.setTipoPrestamo(dto.getClave());
			model.setDescripcion(dto.getDescripcion());
			model.setTipoDeduccion(new TipoDeduccionDAO().getModel(dto.getTipoDeduccion()));
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}
	
	public synchronized TipoPrestamoDTO getDTO(CatTipoPrestamo model, boolean isFullInfo) {
		TipoPrestamoDTO dto = null;
		try {
			dto = this.getDTO(model);
			if(isFullInfo) {
				dto.setTipoDeduccion(new TipoDeduccionDAO().getDTO(model.getTipoDeduccion()));
			}
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public synchronized CatTipoPrestamo getModel(TipoPrestamoDTO dto, boolean isFullInfo) {
		CatTipoPrestamo model = null;
		try {
			model = this.getModel(dto);
			if(isFullInfo) {
				model.setTipoDeduccion(new TipoDeduccionDAO().getModel(null));
			}
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public List<TipoPrestamoDTO> buscarTodos() {
		List<TipoPrestamoDTO> list = null;
		List<CatTipoPrestamo> lPrestamo;
		EntityManager emSGP = null;
		try {
			emSGP = this.getEntityManager();
			lPrestamo = emSGP.createNamedQuery("CatTipoPrestamo.findAll", CatTipoPrestamo.class)
				.getResultList();
			
			list = new ArrayList<TipoPrestamoDTO>();
			for(CatTipoPrestamo p : lPrestamo) {
				TipoPrestamoDTO tp = getDTO(p);
				list.add(tp);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de tipos de préstamo...", ex);
		} finally {
			close(emSGP);
		}
		
		return list;
	}
	
	public TipoPrestamoDTO buscarPorId(String clave, boolean isFullInfo) {
		TipoPrestamoDTO dto = null;
		CatTipoPrestamo model = null;
		EntityManager em = null;
		try {
			em = this.getEntityManager();
			model = em.find(this.modelClass, clave);
			dto = this.getDTO(model, isFullInfo);
		} catch(Exception ex) {
			
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return dto;
	}

	@Override
	public List<TipoPrestamoDTO> buscarActivo() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<TipoPrestamoDTO> buscarPorCriterios(TipoPrestamoDTO e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
