package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatTallaDTO;
import mx.com.ferbo.util.SGPException;

@Stateless
@LocalBean
public class CatTallaDAO extends IBaseDAO<CatTallaDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatTallaDAO.class);

    @Override
    public CatTallaDTO buscarPorId(Integer id) {
        return emSGP.createNamedQuery("CatTalla.findForId", CatTallaDTO.class).setParameter("idTalla", id).getSingleResult();
    }

    @Override
    public List<CatTallaDTO> buscarTodos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CatTallaDTO> buscarActivo() {
        return emSGP.createNamedQuery("CatTalla.findAllActive", CatTallaDTO.class).getResultList();
    }

    @Override
    public List<CatTallaDTO> buscarPorCriterios(CatTallaDTO e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void actualizar(CatTallaDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

    @Override
    public void eliminar(CatTallaDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

    @Override
    public void guardar(CatTallaDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

}
