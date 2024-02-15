package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatPrendaDTO;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
@LocalBean
public class CatPrendaDAO extends IBaseDAO<CatPrendaDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatPrendaDAO.class);

    @Override
    public CatPrendaDTO buscarPorId(Integer id) {
        return emSGP.createNamedQuery("CatPrenda.findById", CatPrendaDTO.class).setParameter("idPrenda", id).getSingleResult();
    }

    @Override
    public List<CatPrendaDTO> buscarTodos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CatPrendaDTO> buscarActivo() {
        return emSGP.createNamedQuery("CatPrenda.findAllActive", CatPrendaDTO.class).getResultList();
    }

    @Override
    public List<CatPrendaDTO> buscarPorCriterios(CatPrendaDTO e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void actualizar(CatPrendaDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

    @Override
    public void eliminar(CatPrendaDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

    @Override
    public void guardar(CatPrendaDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

}
