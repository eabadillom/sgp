package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.PrestamoDTO;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.util.SGPException;

public class PrestamoDAO extends IBaseDAO<PrestamoDTO, Integer>{
	
	private static Logger log = LogManager.getLogger(PrestamoDAO.class);
	
	public PrestamoDTO getDTO(DetPrestamo model) {
		PrestamoDTO dto = null;
		try {
			dto = new PrestamoDTO();
			dto.setIdPrestamo(model.getIdPrestamo());
			dto.setEmpleado(EmpleadoDAO.getDTO(model.getEmpleado()));
			dto.setFechaInicio(model.getFechaInicio());
			dto.setFechaFin(model.getFechaFin());
			dto.setAcumulado(model.getAcumulado());
			dto.setImporte(model.getImporte());
			dto.setTotal(model.getTotal()); 
			dto.setPeriodicidadPago(new PeriodicidadPagoDAO().getDTO(model.getPeriodicidadPago()));
			dto.setTipoPrestamo(TipoPrestamoDAO.getDTO(model.getTipoPrestamo()));
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public DetPrestamo getModel(PrestamoDTO dto) {
		DetPrestamo model = null;
		try {
			model = new DetPrestamo();
			model.setIdPrestamo(dto.getIdPrestamo());
			model.setEmpleado(EmpleadoDAO.getModel(dto.getEmpleado()));
			model.setFechaInicio(dto.getFechaInicio());
			model.setFechaFin(dto.getFechaFin());
			model.setAcumulado(dto.getAcumulado());
			model.setImporte(dto.getImporte());
			model.setTotal(dto.getTotal());
			model.setPeriodicidadPago(new PeriodicidadPagoDAO().getModel(dto.getPeriodicidadPago()));
			model.setTipoPrestamo(TipoPrestamoDAO.getModel(dto.getTipoPrestamo()));
			
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public PrestamoDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrestamoDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrestamoDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrestamoDTO> buscarPorCriterios(PrestamoDTO e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<PrestamoDTO> buscar(String tipoPrestamo, Integer idEmpleado) {
		List<PrestamoDTO> list = null;
		List<DetPrestamo> result = null;
		try {
			result = emSGP.createNamedQuery("DetPrestamo.findByTipoAndEmpleado", DetPrestamo.class)
					.setParameter("idEmpleado", idEmpleado)
					.setParameter("tipoPrestamo", tipoPrestamo)
					.getResultList()
					;
			list = new ArrayList<PrestamoDTO>();
			
			for(DetPrestamo model : result) {
				log.debug("Tipo prestamo: {}", model.getTipoPrestamo().getTipoPrestamo());
				log.debug("Periodicidad: {}", model.getPeriodicidadPago().getPeriodicidad());
				PrestamoDTO dto = getDTO(model);
				list.add(dto);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de préstamos...", ex);
		}
		
		return list;
	}
	
	public List<PrestamoDTO> buscar(Integer idEmpleado) {
		List<PrestamoDTO> list = null;
		List<DetPrestamo> result = null;
		try {
			result = emSGP.createNamedQuery("DetPrestamo.findByEmpleado", DetPrestamo.class)
					.setParameter("idEmpleado", idEmpleado)
					.getResultList()
					;
			list = new ArrayList<PrestamoDTO>();
			
			for(DetPrestamo model : result) {
				log.debug("Tipo prestamo: {}", model.getTipoPrestamo().getTipoPrestamo());
				log.debug("Periodicidad: {}", model.getPeriodicidadPago().getPeriodicidad());
				PrestamoDTO dto = getDTO(model);
				list.add(dto);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de préstamos...", ex);
		}
		
		return list;
	}

	@Override
	public void actualizar(PrestamoDTO e) throws SGPException {
		DetPrestamo model = null;
		
		try {
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.merge(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para guardar el préstamo", ex);
			emSGP.getTransaction().rollback();
		}
	}

	@Override
	public void eliminar(PrestamoDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(PrestamoDTO e) throws SGPException {
		DetPrestamo model = null;
		
		try {
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.persist(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para guardar el préstamo", ex);
			emSGP.getTransaction().rollback();
		}
	}
}
