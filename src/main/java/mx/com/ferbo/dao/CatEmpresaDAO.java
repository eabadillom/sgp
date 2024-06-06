package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dao.sat.RegimenFiscalDAO;
import mx.com.ferbo.dto.CatEmpresaDTO;
import mx.com.ferbo.model.CatEmpresa;

public class CatEmpresaDAO extends IBaseDAO<CatEmpresaDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CatEmpresaDAO.class);
    
    public static synchronized CatEmpresaDTO getDTO(CatEmpresa model) {
    	CatEmpresaDTO dto = null;
    	try {
    		dto = new CatEmpresaDTO();
    		dto.setIdEmpresa(model.getIdEmpresa());
    		dto.setDescripcion(model.getDescripcion());
    		dto.setActivo(model.getActivo());
    		dto.setRazonSocial(model.getRazonSocial());
    		dto.setTipoPersona(model.getTipoPersona());
    	    dto.setRegimenCapital(model.getRegimenCapital());
    	    dto.setRfc(model.getRfc());
    	    dto.setFechaInicioOperacion(model.getFechaInicioOperacion());
    	    dto.setFechaUltimoCambio(model.getFechaUltimoCambio());
    	    dto.setStatusPadron(model.getStatusPadron());
    	    dto.setRegimenFiscal(RegimenFiscalDAO.getDTO(model.getRegimenFiscal()));
    	    dto.setRegistroPatronal(model.getRegistroPatronal());
    		
    	} catch(Exception ex) {
    		log.warn("Problema para generar el DTO...", ex);
    		dto = null;
    	}
    	return dto;
    }
    
    public static synchronized CatEmpresa getModel(CatEmpresaDTO dto) {
    	CatEmpresa model = null;
    	try {
    		model = new CatEmpresa();
    		model.setIdEmpresa(dto.getIdEmpresa());
    		model.setDescripcion(dto.getDescripcion());
    		model.setActivo(dto.getActivo());
    		model.setRazonSocial(dto.getRazonSocial());
    		model.setTipoPersona(dto.getTipoPersona());
    	    model.setRegimenCapital(dto.getRegimenCapital());
    	    model.setRfc(dto.getRfc());
    	    model.setFechaInicioOperacion(dto.getFechaInicioOperacion());
    	    model.setFechaUltimoCambio(dto.getFechaUltimoCambio());
    	    model.setStatusPadron(dto.getStatusPadron());
    	    model.setRegimenFiscal(RegimenFiscalDAO.getModel(dto.getRegimenFiscal()));
    	    model.setRegistroPatronal(dto.getRegistroPatronal());
    	    
    	} catch(Exception ex) {
    		log.warn("Problema para generar el model...", ex.getMessage());
    		model = null;
    	}
    	return model;
    }

    @Override
    public CatEmpresaDTO buscarPorId(Integer id) {
    	CatEmpresaDTO dto = null;
    	CatEmpresa model = null;
    	
    	try {
    		model = emSGP.find(CatEmpresa.class, id);
    		dto = getDTO(model);
    	} catch(Exception ex) {
    		log.warn("Problema para obtener la empresa con id " + id, ex);
    	}
    	
    	return dto;
    }

    @Override
    public List<CatEmpresaDTO> buscarTodos() {
    	List<CatEmpresaDTO> list = null;
    	List<CatEmpresa> result = null;
        try {
        	result = emSGP.createNamedQuery("CatEmpresa.findAll", CatEmpresa.class)
        			.getResultList();
        	list = new ArrayList<CatEmpresaDTO>();
        	for(CatEmpresa model : result) {
        		CatEmpresaDTO dto = getDTO(model);
        		list.add(dto);
        	}
        	
        } catch(Exception ex) {
        	log.warn("Problema para obtener el listado de empresas...", ex);
        }
        
        return list;
    }

    @Override
    public List<CatEmpresaDTO> buscarPorCriterios(CatEmpresaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatEmpresaDTO e) {
        CatEmpresa model = null;
        
        try {
        	model = getModel(e);
        	emSGP.getTransaction().begin();
        	emSGP.merge(model);
        	emSGP.getTransaction().commit();
        } catch(Exception ex) {
        	log.error("Problema para guardar la empresa...", ex);
        	emSGP.getTransaction().rollback();
        }
    }

    @Override
    public void guardar(CatEmpresaDTO e) {
    	CatEmpresa model = null;
        
        try {
        	model = getModel(e);
        	emSGP.getTransaction().begin();
        	emSGP.persist(model);
        	emSGP.getTransaction().commit();
        } catch(Exception ex) {
        	log.error("Problema para guardar la empresa...", ex);
        	emSGP.getTransaction().rollback();
        }
    }

    @Override
    public List<CatEmpresaDTO> buscarActivo() {
    	List<CatEmpresaDTO> list = null;
    	List<CatEmpresa> result = null;
    	
    	try {
    		result = emSGP.createNamedQuery("CatEmpresa.findByActive", CatEmpresa.class).getResultList();
    		list = new ArrayList<CatEmpresaDTO>();
        	for(CatEmpresa model : result) {
        		CatEmpresaDTO dto = getDTO(model);
        		list.add(dto);
        	}
    	} catch(Exception ex) {
    		log.warn("Problema para obtener el listado de empresas...", ex);
    	}
    	
        return list;
    }

    @Override
    public void eliminar(CatEmpresaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
