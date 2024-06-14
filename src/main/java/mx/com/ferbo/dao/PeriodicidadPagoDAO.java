package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.PeriodicidadPagoDTO;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.util.SGPException;

public class PeriodicidadPagoDAO extends DAO<PeriodicidadPagoDTO, CatPeriodicidadPago, String> implements Serializable {

	private static final long serialVersionUID = -6403760078874012505L;
	private static Logger log = LogManager.getLogger(PeriodicidadPagoDAO.class);
	
	public synchronized PeriodicidadPagoDTO getDTO(CatPeriodicidadPago e) {
		PeriodicidadPagoDTO dto = null;
		try {
			dto = new PeriodicidadPagoDTO();
			dto.setPeriodicidad(e.getPeriodicidad());
			dto.setDescripcion(e.getDescripcion());
			dto.setVigenciaInicio(e.getVigenciaInicio());
			dto.setVigenciaFin(e.getVigenciaFin());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public synchronized CatPeriodicidadPago getModel(PeriodicidadPagoDTO dto) {
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
	public PeriodicidadPagoDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PeriodicidadPagoDTO> buscarTodos() {
		List<PeriodicidadPagoDTO> result = null;
		List<CatPeriodicidadPago> list = null;
		EntityManager emSGP = null;
		
		try {
			emSGP = this.getEntityManager();
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
		} finally {
			close(emSGP);
		}
		
		return result;
	}
	
	public List<PeriodicidadPagoDTO> buscarTodos(Date vigenciaFin) {
		List<PeriodicidadPagoDTO> result = null;
		List<CatPeriodicidadPago> list = null;
		EntityManager emSGP = null;
		
		try {
			emSGP = this.getEntityManager();
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
		} finally {
			close(emSGP);
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
		EntityManager emSGP = null;
		try {
			emSGP = this.getEntityManager();
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
		} finally {
			close(emSGP);
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
		CatPeriodicidadPago model = null;
		EntityManager emSGP = null;
		try {
			emSGP = this.getEntityManager();
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.merge(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			emSGP.getTransaction().rollback();
		} finally {
			close(emSGP);
		}
	}

	@Override
	public void eliminar(PeriodicidadPagoDTO e) throws SGPException {
		
		
	}

	@Override
	public void guardar(PeriodicidadPagoDTO e) throws SGPException {
		CatPeriodicidadPago model = null;
		EntityManager emSGP = null;
		try {
			emSGP = this.getEntityManager();
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.persist(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			emSGP.getTransaction().rollback();
		} finally {
			close(emSGP);
		}
	}
}
