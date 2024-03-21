package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatPlantaDTO;
import mx.com.ferbo.model.CatPlanta;

/**
 *
 * @author Gabo
 */
@Stateless
@LocalBean
public class CatPlantaDAO extends IBaseDAO<CatPlantaDTO, Integer> implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatPlantaDAO.class);
    
    public static synchronized CatPlantaDTO getDTO(CatPlanta model) {
    	CatPlantaDTO dto = null;
    	try  {
    		dto = new CatPlantaDTO();
    		dto.setIdPlanta(model.getIdPlanta());
    		dto.setDescripcion(model.getDescripcion());
    		dto.setActivo(model.getActivo());
    	} catch (Exception ex) {
    		dto = null;
		}
    	return dto;
    }
    
    public static synchronized CatPlanta getModel(CatPlantaDTO dto) {
    	CatPlanta model = null;
    	try {
    		model = new CatPlanta();
    		model.setIdPlanta(dto.getIdPlanta());
    		model.setDescripcion(dto.getDescripcion());
    		model.setActivo(dto.getActivo());
    	} catch(Exception ex) {
    		model = null;
    	}
    	return model;
    }

    @Override
    public CatPlantaDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPlantaDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPlantaDTO> buscarPorCriterios(CatPlantaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatPlantaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatPlantaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPlantaDTO> buscarActivo() {
        return emSGP.createNamedQuery("CatPlanta.findByActive", CatPlantaDTO.class).getResultList();
    }

    @Override
    public void eliminar(CatPlantaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
