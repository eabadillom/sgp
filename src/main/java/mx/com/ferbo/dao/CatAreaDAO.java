package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatAreaDTO;
import mx.com.ferbo.model.CatArea;

/**
 *
 * @author Gabo
 */
@Stateless
@LocalBean
public class CatAreaDAO extends IBaseDAO<CatAreaDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatAreaDAO.class);
    
    public static synchronized CatAreaDTO getDTO(CatArea model) {
    	CatAreaDTO dto = null;
    	try {
    		dto = new CatAreaDTO();
    		dto.setIdArea(model.getIdArea());
    		dto.setDescripcion(model.getDescripcion());
    		dto.setActivo(model.getActivo());
    	} catch(Exception ex) {
    		log.warn("Problema para generar el DTO...", ex.getMessage());
    		dto = null;
    	}
    	return dto;
    }
    
    public static synchronized CatArea getModel(CatAreaDTO dto) {
    	CatArea model = null;
    	try {
    		model = new CatArea();
    		model.setIdArea(dto.getIdArea());
    		model.setDescripcion(dto.getDescripcion());
    		model.setActivo(dto.getActivo());
    	} catch(Exception ex) {
    		log.warn("Problema para generar el model...", ex.getMessage());
    		model = null;
    	}
    	return model;
    }

    @Override
    public CatAreaDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatAreaDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatAreaDTO> buscarPorCriterios(CatAreaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatAreaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatAreaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatAreaDTO> buscarActivo() {
        return emSGP.createNamedQuery("CatArea.findByActive", CatAreaDTO.class).getResultList();
    }

    @Override
    public void eliminar(CatAreaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
