package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatSubsidioDTO;
import mx.com.ferbo.util.SGPException;
import org.apache.log4j.Logger;

/**
 *
 * @author erale
 */
@Stateless
@LocalBean
public class CatSubsidioDAO extends IBaseDAO<CatSubsidioDTO, Integer> implements Serializable { 
    
    private static final long serialVersionUID = 1L; 
 
    private static final Logger log = Logger.getLogger(CatSubsidioDAO.class);

    @Override
    public CatSubsidioDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatSubsidioDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatSubsidioDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatSubsidioDTO> buscarPorCriterios(CatSubsidioDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatSubsidioDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(CatSubsidioDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatSubsidioDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public List<CatSubsidioDTO> buscarActuales(Integer fecha){
        return emSGP.createNamedQuery("CatSubsidio.findActual", CatSubsidioDTO.class).setParameter("fecha", fecha + "%").getResultList();
    }
}
