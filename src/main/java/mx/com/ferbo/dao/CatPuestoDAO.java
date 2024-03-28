package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatPuestoDTO;
import mx.com.ferbo.model.CatPuesto;

/**
 *
 * @author Gabo
 */
@Stateless
@LocalBean
public class CatPuestoDAO extends IBaseDAO<CatPuestoDTO, Integer>  implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatPuestoDAO.class);
    
    public static synchronized CatPuestoDTO getDTO(CatPuesto model) {
    	CatPuestoDTO dto = null;
    	try {
    		dto = new CatPuestoDTO();
    		dto.setIdPuesto(model.getIdPuesto());
    		dto.setDescripcion(model.getDescripcion());
    		dto.setActivo(model.getActivo());
    	} catch(Exception ex) {
    		dto = null;
    	}
    	return dto;
    }
    
    public static synchronized CatPuesto getModel(CatPuestoDTO dto) {
    	CatPuesto model = null;
    	try {
    		model = new CatPuesto();
    		model.setIdPuesto(dto.getIdPuesto());
    		model.setDescripcion(dto.getDescripcion());
    		model.setActivo(dto.getActivo());
    	} catch(Exception ex) {
    		model = null;
    	}
    	return model;
    }

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
