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
	
	public PeriodicidadPagoDTO getDTO(CatPeriodicidadPago e) {
		PeriodicidadPagoDTO p = new PeriodicidadPagoDTO();
		
		p.setPeriodicidad(e.getPeriodicidad());
		p.setDescripcion(e.getDescripcion());
		p.setVigenciaInicio(e.getVigenciaInicio());
		p.setVigenciaFin(e.getVigenciaFin());
		
		return p;
	}
	
	public CatPeriodicidadPago getModel(PeriodicidadPagoDTO e) {
		CatPeriodicidadPago p;
		
		p = new CatPeriodicidadPago();
		p.setPeriodicidad(e.getPeriodicidad());
		p.setDescripcion(e.getDescripcion());
		p.setVigenciaInicio(e.getVigenciaInicio());
		p.setVigenciaFin(e.getVigenciaFin());
		
		return p;
	}

	@Override
	public PeriodicidadPagoDTO buscarPorId(String id) {
		PeriodicidadPagoDTO pp = null;
		CatPeriodicidadPago p = null;
		
		try {
			p = emSGP.find(CatPeriodicidadPago.class, id);
			pp = this.getDTO(p);
		} catch(Exception ex) {
			log.error("Problema para buscar la periodicidad con id {}", id);
		}
		
		return pp;
	}

	@Override
	public List<PeriodicidadPagoDTO> buscarTodos() {
		throw new UnsupportedOperationException("Not supported yet.");
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
				pp = this.getDTO(p);
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
			p = this.getModel(e);
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
			p = this.getModel(e);
			emSGP.getTransaction().begin();
			emSGP.persist(p);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			emSGP.getTransaction().rollback();
		}
	}

}
