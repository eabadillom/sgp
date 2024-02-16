package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DiaNoLaboralDTO;
import mx.com.ferbo.dto.PaisDTO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.util.SGPException;

public class DiaNoLaboralDAO extends IBaseDAO<DiaNoLaboralDTO, Integer> {
	private static Logger log = LogManager.getLogger(DiaNoLaboralDAO.class);

	@Override
	public DiaNoLaboralDTO buscarPorId(Integer id) {
		DiaNoLaboralDTO d = null;
		try {
			d = emSGP.find(DiaNoLaboralDTO.class, id);
		} catch(NoResultException ex) {
			log.warn("No se encontrò el dia con id {}", id);
		} catch (Exception ex) {
			log.error("Problema para obtener el dia no laboral con id " + id, ex);
		}
		return d;
	}

	@Override
	public List<DiaNoLaboralDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<DiaNoLaboralDTO> buscarPorPeriodo(String clavePais, Date fechaInicio, Date fechaFin) {
		List<DiaNoLaboralDTO> list = null;
		List<CatDiaNoLaboral> listTmp = null;
		DiaNoLaboralDTO dia = null;
		PaisDTO pais = null;
		
		try {
			
			listTmp = emSGP.createNamedQuery("CatDiaNoLaboral.buscaPorPeriodo", CatDiaNoLaboral.class)
					.setParameter("clavePais", clavePais)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.getResultList()
					;
			
			list = new ArrayList<DiaNoLaboralDTO>();
			
			for(CatDiaNoLaboral d : listTmp) {
				pais = new PaisDTO(d.getPais().getClavePais(), d.getPais().getNombrePais());
				dia = new DiaNoLaboralDTO(d.getId(), d.getFecha(), d.getDescripcion(), pais);
				list.add(dia);
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de dias no laborales del periodo " + fechaInicio +" al " + fechaFin, ex);
		}
		return list;
	}

	@Override
	public List<DiaNoLaboralDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DiaNoLaboralDTO> buscarPorCriterios(DiaNoLaboralDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(DiaNoLaboralDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(DiaNoLaboralDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(DiaNoLaboralDTO e) throws SGPException {
		CatDiaNoLaboral dia = null;
		
		try {
			dia = new CatDiaNoLaboral(null, e.getFecha(), e.getDescripcion(), e.getPais().getClavePais(), e.getPais().getNombrePais());
			emSGP.getTransaction().begin();
			emSGP.persist(dia);
			emSGP.getTransaction().commit();
			e.setId(dia.getId());
		} catch(Exception ex) {
			log.error("Ocurrió un problema al guardar el día no laboral...", ex);
			throw new SGPException("Problema para guardar el día no laboral...");
		} finally {
			
		}
		
	}
	
	

}
