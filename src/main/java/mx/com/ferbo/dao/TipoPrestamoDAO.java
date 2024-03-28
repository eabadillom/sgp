package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.TipoPrestamoDTO;
import mx.com.ferbo.model.CatTipoPrestamo;
import mx.com.ferbo.util.SGPException;

public class TipoPrestamoDAO extends IBaseDAO<TipoPrestamoDTO, String> {
	
	private static Logger log = LogManager.getLogger(TipoPrestamoDAO.class);
	
	public static synchronized TipoPrestamoDTO getDTO(CatTipoPrestamo model) {
		TipoPrestamoDTO dto = null;
		try {
			dto = new TipoPrestamoDTO();
			dto.setTipoPrestamo(model.getTipoPrestamo());
			dto.setDescripcion(model.getDescripcion());
		} catch(Exception ex) {
			
		}
		return dto;
	}
	
	public static synchronized CatTipoPrestamo getModel(TipoPrestamoDTO dto) {
		CatTipoPrestamo model = null;
		try {
			model = new CatTipoPrestamo();
			model.setTipoPrestamo(dto.getTipoPrestamo());
			model.setDescripcion(dto.getDescripcion());
			
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public TipoPrestamoDTO buscarPorId(String id) {
		TipoPrestamoDTO resultado = null;
		CatTipoPrestamo prestamo = null;
		
		try {
			prestamo = this.emSGP.find(CatTipoPrestamo.class, id);
			resultado = getDTO(prestamo);
		} catch(Exception ex) {
			log.error("Problema para obtener el tipo de préstamo...", ex);
		}
		
		return resultado;
	}

	@Override
	public List<TipoPrestamoDTO> buscarTodos() {
		List<TipoPrestamoDTO> list = null;
		List<CatTipoPrestamo> lPrestamo;
		
		try {
			lPrestamo = this.emSGP.createNamedQuery("CatTipoPrestamo.findAll", CatTipoPrestamo.class)
				.getResultList();
			
			list = new ArrayList<TipoPrestamoDTO>();
			for(CatTipoPrestamo p : lPrestamo) {
				TipoPrestamoDTO tp = getDTO(p);
				list.add(tp);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de tipos de préstamo...", ex);
		}
		
		return list;
	}

	@Override
	public List<TipoPrestamoDTO> buscarActivo() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<TipoPrestamoDTO> buscarPorCriterios(TipoPrestamoDTO e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void actualizar(TipoPrestamoDTO e) throws SGPException {
		throw new UnsupportedOperationException("Not supported yet.");
		
	}

	@Override
	public void eliminar(TipoPrestamoDTO e) throws SGPException {
		throw new UnsupportedOperationException("Not supported yet.");
		
	}

	@Override
	public void guardar(TipoPrestamoDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

}
