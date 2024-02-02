package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatEmpresaDTO;

/**
 *
 * @author Gabo
 */
@Stateless
@LocalBean
public class CatEmpresaDAO extends IBaseDAO<CatEmpresaDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatEmpresaDAO.class);

    @Override
    public CatEmpresaDTO buscarPorId(Integer id) {
        return emSGP.createNamedQuery("CatEmpresa.findById", CatEmpresaDTO.class).setParameter("idEmpresa", id).getSingleResult();
    }

    @Override
    public List<CatEmpresaDTO> buscarTodos() {
        return emSGP.createNamedQuery("CatEmpresa.findAll", CatEmpresaDTO.class).getResultList();
    }

    @Override
    public List<CatEmpresaDTO> buscarPorCriterios(CatEmpresaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatEmpresaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatEmpresaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatEmpresaDTO> buscarActivo() {
        return emSGP.createNamedQuery("CatEmpresa.findByActive", CatEmpresaDTO.class).getResultList();
    }

    @Override
    public void eliminar(CatEmpresaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
