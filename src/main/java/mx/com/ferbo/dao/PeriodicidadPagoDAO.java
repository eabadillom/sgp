package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.PeriodicidadPagoDTO;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.util.SGPException;

public class PeriodicidadPagoDAO extends IBaseDAO<PeriodicidadPagoDTO, String> implements Serializable {

	private static final long serialVersionUID = -6403760078874012505L;
	private static Logger log = LogManager.getLogger(PeriodicidadPagoDAO.class);
	
	public static synchronized PeriodicidadPagoDTO getDTO(CatPeriodicidadPago e) {
		PeriodicidadPagoDTO p = null;
		try {
			p = new PeriodicidadPagoDTO();
			p.setPeriodicidad(e.getPeriodicidad());
			p.setDescripcion(e.getDescripcion());
			p.setVigenciaInicio(e.getVigenciaInicio());
			p.setVigenciaFin(e.getVigenciaFin());
		} catch(Exception ex) {
			p = null;
		}
		return p;
	}
	
	public static synchronized CatPeriodicidadPago getModel(PeriodicidadPagoDTO dto) {
		CatPeriodicidadPago model;
		try {
			model = new CatPeriodicidadPago();
			model.setPeriodicidad(dto.getPeriodicidad());
			model.setDescripcion(dto.getDescripcion());
			model.setVigenciaInicio(dto.getVigenciaInicio());
			model.setVigenciaFin(dto.getVigenciaFin());
		} catch(Exception ex) {
			model = null;
		}
		
		return model;
	}

	@Override
	public PeriodicidadPagoDTO buscarPorId(String id) {
		PeriodicidadPagoDTO pp = null;
		CatPeriodicidadPago p = null;
		
		try {
			p = emSGP.find(CatPeriodicidadPago.class, id);
			pp = getDTO(p);
		} catch(Exception ex) {
			log.error("Problema para buscar la periodicidad con id {}", id);
		}
		
		return pp;
	}

	@Override
	public List<PeriodicidadPagoDTO> buscarTodos() {
		List<PeriodicidadPagoDTO> result = null;
		List<CatPeriodicidadPago> list = null;
		
		try {
			list = emSGP.createNamedQuery("CatPeriodicidadPago.buscarTodos", CatPeriodicidadPago.class)
					.getResultList()
					;
			result = new ArrayList<PeriodicidadPagoDTO>();
			for(CatPeriodicidadPago model : list) {
				PeriodicidadPagoDTO dto = getDTO(model);
				result.add(dto);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de periodicidades de pago...", ex);
		}
		
		return result;
	}
	
	public List<PeriodicidadPagoDTO> buscarTodos(Date vigenciaFin) {
		List<PeriodicidadPagoDTO> result = null;
		List<CatPeriodicidadPago> list = null;
		
		try {
			list = emSGP.createNamedQuery("CatPeriodicidadPago.buscarTodosActivos", CatPeriodicidadPago.class)
					.setParameter("vigenciaFin", vigenciaFin)
					.getResultList()
					;
			result = new ArrayList<PeriodicidadPagoDTO>();
			for(CatPeriodicidadPago model : list) {
				PeriodicidadPagoDTO dto = getDTO(model);
				result.add(dto);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de periodicidades de pago...", ex);
		}
		
		return result;
	}
	
	@Override
	public List<PeriodicidadPagoDTO> buscarActivo() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public List<PeriodicidadPagoDTO> buscarActivo(Date fecha) {
		List<PeriodicidadPagoDTO> result = null;
		List<CatPeriodicidadPago> lista = null;
		PeriodicidadPagoDTO pp = null;
		
		try {
			lista = emSGP.createNamedQuery("CatPeriodicidadPago.buscarActivo", CatPeriodicidadPago.class)
					.setParameter("fecha", fecha)
					.getResultList()
					;
			
			result = new ArrayList<PeriodicidadPagoDTO>();
			for(CatPeriodicidadPago p : lista) {
				pp = getDTO(p);
				result.add(pp);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de periodicidad de pago...", ex);
		}
		
		
		
		return result;
	}

	@Override
	public List<PeriodicidadPagoDTO> buscarPorCriterios(PeriodicidadPagoDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(PeriodicidadPagoDTO e) throws SGPException {
		CatPeriodicidadPago p = null;
		try {
			p = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.merge(p);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			emSGP.getTransaction().rollback();
		}
	}

	@Override
	public void eliminar(PeriodicidadPagoDTO e) throws SGPException {
		
		
	}

	@Override
	public void guardar(PeriodicidadPagoDTO e) throws SGPException {
		CatPeriodicidadPago p = null;
		try {
			p = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.persist(p);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			emSGP.getTransaction().rollback();
		}
	}

}
