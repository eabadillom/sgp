package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.PaisDTO;
import mx.com.ferbo.model.Pais;
import mx.com.ferbo.util.SGPException;

public class PaisDAO extends IBaseDAO<PaisDTO, String>{
	private static Logger log = LogManager.getLogger(PaisDAO.class);

	@Override
	public PaisDTO buscarPorId(String id) {
		PaisDTO pais = null;
		Pais p = null;
		
		try {
			p = emSGP.find(Pais.class, id);
			pais = new PaisDTO(p.getClavePais(), p.getNombrePais());
		} catch(NoResultException ex) {
			log.warn("No se encontró el pais con id {}", id);
		} catch(Exception ex) {
			log.error("Problema para obtener el pais con id " + id, ex);
		}
		
		return pais;
	}

	@Override
	public List<PaisDTO> buscarTodos() {
		List<PaisDTO> list = null;
		try {
			list = emSGP.createNamedQuery("Pais.findAll", PaisDTO.class)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de todos los paises...",ex);
		}
		return list;
	}

	@Override
	public List<PaisDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PaisDTO> buscarPorCriterios(PaisDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(PaisDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(PaisDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(PaisDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	
	

}
