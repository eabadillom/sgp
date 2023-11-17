package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatPuestoDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author Gabo
 */
@Stateless
@LocalBean
public class CatPuestoDAO extends IBaseDAO<CatPuestoDTO, Integer>  implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(CatPuestoDAO.class);

    @Override
    public CatPuestoDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPuestoDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPuestoDTO> buscarPorCriterios(CatPuestoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatPuestoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatPuestoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<CatPuestoDTO> buscarActivo() {
         return emSGP.createNamedQuery("CatPuesto.findByActive", CatPuestoDTO.class).getResultList();
    }

    @Override
    public void eliminar(CatPuestoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
