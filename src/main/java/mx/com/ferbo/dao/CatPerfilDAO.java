package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatPerfilDTO;
import mx.com.ferbo.model.CatPerfil;

@Deprecated
@Stateless
@LocalBean
public class CatPerfilDAO extends IBaseDAO<CatPerfilDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatPerfilDAO.class);
    
    public static synchronized CatPerfilDTO getDTO(CatPerfil model) {
    	CatPerfilDTO dto = new CatPerfilDTO();
    	dto.setIdPerfil(model.getIdPerfil());
    	dto.setDescripcion(model.getDescripcion());
    	dto.setActivo(model.getActivo());
    	return dto;
    }
    
    public static synchronized CatPerfil getModel(CatPerfilDTO dto) {
    	CatPerfil model = new CatPerfil();
    	model.setIdPerfil(dto.getIdPerfil());
    	model.setDescripcion(dto.getDescripcion());
    	model.setActivo(dto.getActivo());
    	return model;
    }

    @Override
    public CatPerfilDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPerfilDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPerfilDTO> buscarPorCriterios(CatPerfilDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatPerfilDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatPerfilDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPerfilDTO> buscarActivo() {
        return emSGP.createNamedQuery("CatPerfil.findByActive", CatPerfilDTO.class).getResultList();
    }

    @Override
    public void eliminar(CatPerfilDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
