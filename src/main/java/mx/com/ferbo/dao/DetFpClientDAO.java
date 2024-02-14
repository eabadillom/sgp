package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetFpClientDTO;
import mx.com.ferbo.util.SGPException;

@Stateless
@LocalBean
public class DetFpClientDAO extends IBaseDAO<DetFpClientDTO, Integer> implements Serializable {

    private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(DetFpClientDAO.class);
	
	@Override
	public DetFpClientDTO buscarPorId(Integer id) {
		DetFpClientDTO fpClient = null;
		
		try {
			fpClient = emSGP.createNamedQuery("DetFpClient.findById", DetFpClientDTO.class)
					.setParameter("id", id)
					.getSingleResult(); 			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("ERROR" + e.getMessage());
		}
		return fpClient;
	}

	@Override
	public List<DetFpClientDTO> buscarTodos() {
		List<DetFpClientDTO> dtFpClientList = emSGP.createNamedQuery( "DetFpClient.findByAll" , DetFpClientDTO.class).getResultList();
		return dtFpClientList.isEmpty() ? new ArrayList<>(): dtFpClientList ;
	}

	@Override
	public List<DetFpClientDTO> buscarActivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetFpClientDTO> buscarPorCriterios(DetFpClientDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(DetFpClientDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(DetFpClientDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(DetFpClientDTO e) throws SGPException {
		// TODO Auto-generated method stub
		}

}
