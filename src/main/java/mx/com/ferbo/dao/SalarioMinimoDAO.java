package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.SalarioMinimoDTO;
import mx.com.ferbo.model.CatSalarioMinimo;
import mx.com.ferbo.util.SGPException;

public class SalarioMinimoDAO extends IBaseDAO<SalarioMinimoDTO, Integer> {
	
	private static Logger log = LogManager.getLogger(SalarioMinimoDAO.class);
	
	public static synchronized SalarioMinimoDTO getDTO(CatSalarioMinimo model) {
		SalarioMinimoDTO dto = null;
		
		try {
			dto = new SalarioMinimoDTO();
			dto.setId(model.getId());
			dto.setVigencia(model.getVigencia());
			dto.setZonaG(model.getZonaG());
			dto.setZonaLFN(model.getZonaLFN());
		} catch(Exception ex) {
			dto = null;
		}
		return dto;
	}
	
	public static synchronized CatSalarioMinimo getModel(SalarioMinimoDTO dto) {
		CatSalarioMinimo model = null;
		
		try {
			model = new CatSalarioMinimo();
			model.setId(dto.getId());
			model.setVigencia(dto.getVigencia());
			model.setZonaG(dto.getZonaG());
			model.setZonaLFN(dto.getZonaLFN());
		} catch(Exception ex) {
			model = null;
		}
		return model;
	}

	@Override
	public SalarioMinimoDTO buscarPorId(Integer id) {
		SalarioMinimoDTO dto = null;
		CatSalarioMinimo model = null;
		
		try {
			model = emSGP.find(CatSalarioMinimo.class, id);
			dto = getDTO(model);
		} catch(Exception ex) {
			log.error("Problema para obtener el salario mínimo solicitado...", ex);
		}
		
		return dto;
	}

	@Override
	public List<SalarioMinimoDTO> buscarTodos() {
		List<SalarioMinimoDTO> list = null;
		List<CatSalarioMinimo> result = null;
		
		try {
			result = emSGP.createNamedQuery("CatSalarioMinimo.findAll", CatSalarioMinimo.class)
					.getResultList()
					;
			list = new ArrayList<SalarioMinimoDTO>();
			for(CatSalarioMinimo model : result) {
				list.add(getDTO(model));
			}
		} catch(Exception ex ) {
			log.error("Problema para obtener la lista de salarios minimos...", ex);
		}
		return list;
	}

	@Override
	public List<SalarioMinimoDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalarioMinimoDTO> buscarPorCriterios(SalarioMinimoDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(SalarioMinimoDTO e) throws SGPException {
		CatSalarioMinimo model = null;
		
		try {
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.merge(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para guardar el salario minimo...", ex);
			emSGP.getTransaction().rollback();
		}
	}

	@Override
	public void eliminar(SalarioMinimoDTO e) throws SGPException {
		CatSalarioMinimo model = null;
		
		try {
			model = emSGP.find(CatSalarioMinimo.class, e.getId());
			emSGP.getTransaction().begin();
			emSGP.remove(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para eliminar el salario minimo...", ex);
			emSGP.getTransaction().rollback();
		}
	}

	@Override
	public void guardar(SalarioMinimoDTO e) throws SGPException {
		CatSalarioMinimo model = null;
		
		try {
			model = getModel(e);
			emSGP.getTransaction().begin();
			emSGP.persist(model);
			emSGP.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para guardar el salario minimo...", ex);
			emSGP.getTransaction().rollback();
		}
	}

}
