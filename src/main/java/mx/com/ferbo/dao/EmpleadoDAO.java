package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.util.SGPException;



/**
 *
 * @author Gabo
 */
@Stateless
@LocalBean
public class EmpleadoDAO extends IBaseDAO<DetEmpleadoDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final Logger log = LogManager.getLogger(EmpleadoDAO.class);
    
    public static synchronized DetEmpleadoDTO getDTO(DetEmpleado model) {
    	DetEmpleadoDTO dto = null;
    	try {
    		dto = new DetEmpleadoDTO();
    		dto.setIdEmpleado(model.getIdEmpleado());
    		dto.setNumEmpleado(model.getNumEmpleado());
    		dto.setNombre(model.getNombre());
    		dto.setPrimerAp(model.getPrimerAp());
    		dto.setSegundoAp(model.getSegundoAp());
    		dto.setFechaNacimiento(model.getFechaNacimiento());
    		dto.setFechaRegistro(model.getFechaRegistro());
    		dto.setFechaModificacion(model.getFechaModificacion());
    		dto.setCatPerfilDTO(CatPerfilDAO.getDTO(model.getIdPerfil()));
    		dto.setCatPuestoDTO(CatPuestoDAO.getDTO(model.getIdPuesto()));
    		dto.setCurp(model.getCurp());
    		dto.setRfc(model.getRfc());
    		dto.setCorreo(model.getCorreo());
    		dto.setFechaIngreso(model.getFechaIngreso());
    		dto.setNss(model.getNss());
    		dto.setCatEmpresaDTO(CatEmpresaDAO.getDTO(model.getIdEmpresa()));
    		dto.setActivo(model.getActivo());
    		dto.setCatPlantaDTO(CatPlantaDAO.getDTO(model.getIdPlanta()));
    		dto.setCatAreaDTO(CatAreaDAO.getDTO(model.getIdArea()));
    		dto.setFotografia(model.getFotografia());
    		dto.setSueldoDiario(model.getSueldoDiario());
    		dto.setDatoEmpresa(new DatoEmpresaDAO().getDTO(model.getDatoEmpresa()));
    		dto.setEmpleadoFoto(new EmpleadoFotoDAO().getDTO(model.getEmpleadoFoto()));
    	} catch(Exception ex) {
    		dto = null;
    	}
    	return dto;
    }
    
    public static synchronized DetEmpleadoDTO getDTO(DetEmpleado model, boolean isFullInfo) {
    	DetEmpleadoDTO dto = null;
    	try {
    		dto = new DetEmpleadoDTO();
    		dto.setIdEmpleado(model.getIdEmpleado());
    		dto.setNumEmpleado(model.getNumEmpleado());
    		dto.setNombre(model.getNombre());
    		dto.setPrimerAp(model.getPrimerAp());
    		dto.setSegundoAp(model.getSegundoAp());
    		dto.setFechaNacimiento(model.getFechaNacimiento());
    		dto.setFechaRegistro(model.getFechaRegistro());
    		dto.setFechaModificacion(model.getFechaModificacion());
    		dto.setCurp(model.getCurp());
    		dto.setRfc(model.getRfc());
    		dto.setCorreo(model.getCorreo());
    		dto.setFechaIngreso(model.getFechaIngreso());
    		dto.setNss(model.getNss());
    		dto.setActivo(model.getActivo());
    		dto.setFotografia(model.getFotografia());
    		dto.setSueldoDiario(model.getSueldoDiario());
    		dto.setCatPerfilDTO(CatPerfilDAO.getDTO(model.getIdPerfil()));
    		
    		if(isFullInfo == false)
    			return dto;
    		
    		dto.setCatAreaDTO(CatAreaDAO.getDTO(model.getIdArea()));
    		dto.setCatPlantaDTO(CatPlantaDAO.getDTO(model.getIdPlanta()));
    		dto.setCatEmpresaDTO(CatEmpresaDAO.getDTO(model.getIdEmpresa()));
    		dto.setCatPuestoDTO(CatPuestoDAO.getDTO(model.getIdPuesto()));
    		dto.setDatoEmpresa(new DatoEmpresaDAO().getDTO(model.getDatoEmpresa()));
    		
    	} catch(Exception ex) {
    		dto = null;
    	}
    	return dto;
    }
    
    public static synchronized DetEmpleado getModel(DetEmpleadoDTO dto) {
    	DetEmpleado model = null;
    	try {
    		model = new DetEmpleado();
    		model.setIdEmpleado(dto.getIdEmpleado());
    		model.setNumEmpleado(dto.getNumEmpleado());
    		model.setNombre(dto.getNombre());
    		model.setPrimerAp(dto.getPrimerAp());
    		model.setSegundoAp(dto.getSegundoAp());
    		model.setFechaNacimiento(dto.getFechaNacimiento());
    		model.setFechaRegistro(dto.getFechaRegistro());
    		model.setFechaModificacion(dto.getFechaModificacion());
    		model.setIdPerfil(CatPerfilDAO.getModel(dto.getCatPerfilDTO()));
    		model.setIdPuesto(CatPuestoDAO.getModel(dto.getCatPuestoDTO()));
    		model.setCurp(dto.getCurp());
    		model.setRfc(dto.getRfc());
    		model.setCorreo(dto.getCorreo());
    		model.setFechaIngreso(dto.getFechaIngreso());
    		model.setNss(dto.getNss());
    		model.setIdEmpresa(CatEmpresaDAO.getModel(dto.getCatEmpresaDTO()));
    		model.setActivo(dto.getActivo());
    		model.setIdPlanta(CatPlantaDAO.getModel(dto.getCatPlantaDTO()));
    		model.setIdArea(CatAreaDAO.getModel(dto.getCatAreaDTO()));
    		model.setFotografia(dto.getFotografia());
    		model.setSueldoDiario(dto.getSueldoDiario());
    		model.setDatoEmpresa(new DatoEmpresaDAO().getModel(dto.getDatoEmpresa()));
    		model.setEmpleadoFoto(new EmpleadoFotoDAO().getModel(dto.getEmpleadoFoto()));
    	} catch(Exception ex) {
    		model = null;
    	}
    	return model;
    }
    
    public static synchronized DetEmpleado getModel(DetEmpleadoDTO dto, boolean isFullInfo) {
    	DetEmpleado model = null;
    	try {
    		model = new DetEmpleado();
    		model.setIdEmpleado(dto.getIdEmpleado());
    		model.setNumEmpleado(dto.getNumEmpleado());
    		model.setNombre(dto.getNombre());
    		model.setPrimerAp(dto.getPrimerAp());
    		model.setSegundoAp(dto.getSegundoAp());
    		model.setFechaNacimiento(dto.getFechaNacimiento());
    		model.setFechaRegistro(dto.getFechaRegistro());
    		model.setFechaModificacion(dto.getFechaModificacion());
    		model.setCurp(dto.getCurp());
    		model.setRfc(dto.getRfc());
    		model.setCorreo(dto.getCorreo());
    		model.setFechaIngreso(dto.getFechaIngreso());
    		model.setNss(dto.getNss());
    		model.setActivo(dto.getActivo());
    		model.setFotografia(dto.getFotografia());
    		model.setSueldoDiario(dto.getSueldoDiario());
    		
    		if(isFullInfo == false)
    			return model;
    		
    		model.setIdPerfil(CatPerfilDAO.getModel(dto.getCatPerfilDTO()));
    		model.setIdPuesto(CatPuestoDAO.getModel(dto.getCatPuestoDTO()));
    		model.setIdEmpresa(CatEmpresaDAO.getModel(dto.getCatEmpresaDTO()));
    		model.setIdPlanta(CatPlantaDAO.getModel(dto.getCatPlantaDTO()));
    		model.setIdArea(CatAreaDAO.getModel(dto.getCatAreaDTO()));
    		model.setDatoEmpresa(new DatoEmpresaDAO().getModel(dto.getDatoEmpresa()));
    		
    	} catch(Exception ex) {
    		model = null;
    	}
    	return model;
    }
    
    
    
    @Override
    public DetEmpleadoDTO buscarPorId(Integer id) {
        List<DetEmpleadoDTO> empleado = emSGP.createNamedQuery("DetEmpleado.findByID", DetEmpleadoDTO.class).setParameter("idEmp", id).getResultList();
        return empleado.isEmpty() ? new DetEmpleadoDTO() : empleado.get(0);
    }
    
    public DetEmpleadoDTO buscarPorId(Integer id, boolean isFullInfo) {
    	DetEmpleadoDTO dto = null;
    	DetEmpleado model = null;
    	
    	try {
    		model = emSGP.find(DetEmpleado.class, id);
    		if(isFullInfo) {
    			log.info("id dato empresa: {}", model.getDatoEmpresa() == null ? null : model.getDatoEmpresa().getId());
    			log.info("id area: {}", model.getIdArea() == null ? null : model.getIdArea().getIdArea());
    			log.info("id empresa: {}", model.getIdEmpresa() == null ? null : model.getIdEmpresa().getIdEmpresa());
    			log.info("id perfil: {}", model.getIdPerfil() == null ? null : model.getIdPerfil().getIdPerfil());
    			log.info("id planta: {}", model.getIdPlanta() == null ? null : model.getIdPlanta().getIdPlanta());
    			log.info("id puesto: {}", model.getIdPuesto() == null ? null : model.getIdPuesto().getIdPuesto());
    			log.info("id foto: {}", model.getEmpleadoFoto() == null ? null : model.getEmpleadoFoto().getId());
    		}
    		dto = getDTO(model);
    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado con id " + id, ex);
    	}
    	
    	return dto;
    }
    
    @Override
    public List<DetEmpleadoDTO> buscarTodos() {
        List<DetEmpleadoDTO> dtoList = null;
        List<DetEmpleado> modelList = null;
        
        try {
        	modelList = emSGP.createNamedQuery("DetEmpleado.getAll", DetEmpleado.class)
        			.getResultList();
        	
        	dtoList = new ArrayList<DetEmpleadoDTO>();
        	for(DetEmpleado model : modelList) {
        		DetEmpleadoDTO dto = getDTO(model);
        		dtoList.add(dto);
        	}
        } catch(Exception ex) {
        	log.error("Problema para obtener el listado de empleados...", ex);
        }
        
        return dtoList;
    }
    
    public List<DetEmpleadoDTO> buscarTodos(boolean isFullInfo) {
        List<DetEmpleadoDTO> dtoList = null;
        List<DetEmpleado> modelList = null;
        
        try {
        	modelList = emSGP.createNamedQuery("DetEmpleado.getAll", DetEmpleado.class)
        			.getResultList();
        	
        	dtoList = new ArrayList<DetEmpleadoDTO>();
        	for(DetEmpleado model : modelList) {
        		DetEmpleadoDTO dto = getDTO(model);
        		dtoList.add(dto);
        		
        		if(isFullInfo == false) {
        			continue;
        		}
        		
        		log.info("id area: {}", model.getIdArea().getIdArea());
        		log.info("id empresa: {}", model.getIdEmpresa().getIdEmpresa());
        		log.info("id perfil: {}", model.getIdPerfil().getIdPerfil());
        		log.info("id planta: {}", model.getIdPlanta().getIdPlanta());
        		log.info("id puesto: {}", model.getIdPuesto());
        		log.info("id dato empresa: {}", model.getDatoEmpresa());
        	}
        } catch(Exception ex) {
        	log.error("Problema para obtener el listado de empleados...", ex);
        }
        
        return dtoList;
    }
    
    @Override
    public List<DetEmpleadoDTO> buscarActivo() {
        return emSGP.createNamedQuery("DetEmpleado.findByActive", DetEmpleadoDTO.class).getResultList();
    }
    
    public List<DetEmpleadoDTO> buscarActivo(boolean isFullInfo) {
    	List<DetEmpleadoDTO> dtoList = null;
    	List<DetEmpleado> modelList = null;
    	
    	try {
    		modelList = emSGP.createNamedQuery("DetEmpleado.getAll", DetEmpleado.class)
    				.getResultList()
    				;
    		dtoList = new ArrayList<DetEmpleadoDTO>();
    		
    		for(DetEmpleado model : modelList) {
    			DetEmpleadoDTO dto = getDTO(model, isFullInfo);
    			dtoList.add(dto);
    		}
    	} catch(Exception ex) {
    		log.error("Problema para obtener el listado de empleados...", ex);
    	}
    	
    	return dtoList;
    }
    
    @Override
    public List<DetEmpleadoDTO> buscarPorCriterios(DetEmpleadoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void actualizar(DetEmpleadoDTO e)throws SGPException {
        DetEmpleado model = null;
        try {
        	model = getModel(e);
            emSGP.getTransaction().begin();
            emSGP.merge(model);
            emSGP.getTransaction().commit();
            emSGP.detach(model);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0011: " + ex.getMessage() + ". Error al actualizar el empleado " + e.getNumEmpleado() != null ? e.getNumEmpleado() : null);
            log.error("Problema al actualizar el empleado...", ex);
        }
    }
    
    @Override
    public void eliminar(DetEmpleadoDTO e) throws SGPException {
        DetEmpleado empleado = new DetEmpleado();
        try {
            emSGP.getTransaction().begin();
            empleado = emSGP.getReference(DetEmpleado.class, e.getIdEmpleado());
            empleado.setActivo((short) 0);
            empleado.setFechaModificacion(new Date());
            emSGP.merge(empleado);
            emSGP.getTransaction().commit();
            emSGP.detach(empleado);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            throw new SGPException("Error al eliminar al empleado");
        }
    }
    
    @Override
    public void guardar(DetEmpleadoDTO e) throws SGPException {
        DetEmpleado model = null;
        try {
        	model = getModel(e);
        	
            emSGP.getTransaction().begin();
            emSGP.persist(model);
            emSGP.getTransaction().commit();
            e.setIdEmpleado(model.getIdEmpleado());
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            ex.printStackTrace();
            log.warn("EX-0009: " + ex.getMessage() + ". Error al guardar empleado " + e.getNumEmpleado() != null ? e.getNumEmpleado() : null);
        }
    }
    
    public DetEmpleadoDTO buscarPorNumEmpl(String numEmpl) {
    	List<DetEmpleadoDTO> empleado = emSGP.createNamedQuery("DetEmpleado.findByNumEmpl", DetEmpleadoDTO.class).setParameter("numEmpl", numEmpl).getResultList();
    	return !empleado.isEmpty() ? empleado.get(0) : null;
    }
    
    public DetEmpleadoDTO buscarPorNumeroEmpleado(String numeroEmpleado, boolean isFullInfo) {
    	DetEmpleadoDTO dto = null;
    	DetEmpleado model = null;
    	try {
    		model = emSGP.createNamedQuery("DetEmpleado.findByNumero", DetEmpleado.class)
    				.setParameter("numero",numeroEmpleado)
    				.getSingleResult()
    				;
    		
    		dto = getDTO(model, isFullInfo);
    	} catch(Exception ex) {
    		log.error("Problema para obtener el número de empleado " + numeroEmpleado, ex);
    		dto = null;
    	}
    	
    	return dto;
    }
    
    public DetEmpleadoDTO buscarPorNumEmplAndBiometricos(String numEmpl) {
    	List<DetEmpleadoDTO> empleado = emSGP.createNamedQuery("DetEmpleado.findByNumEmpl", DetEmpleadoDTO.class).setParameter("numEmpl", numEmpl).getResultList();
    	return !empleado.isEmpty() ? empleado.get(0) : null;
    }
    
    public DetEmpleadoDTO buscarConFoto(Integer idEmpleado) {
    	DetEmpleadoDTO dto = null;
    	DetEmpleado model = null;
    	
    	try {
    		model = emSGP.find(DetEmpleado.class, idEmpleado);
    		
    		if(model.getEmpleadoFoto() != null)
    			log.info("Id foto empleado: {}",model.getEmpleadoFoto().getId());
    		
    		dto = getDTO(model,false);
    		dto.setEmpleadoFoto(new EmpleadoFotoDAO().getDTO(model.getEmpleadoFoto()));
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener la foto del empleado...", ex);
    	}
    	
    	return dto;
    }
    
    public DetEmpleadoDTO buscarPorNumEmplFechaRegistro(String numEmpl, String fechaEntrada) {
    	List<DetEmpleadoDTO> empleado = emSGP.createNamedQuery("DetEmpleado.findByNumEmpl", DetEmpleadoDTO.class).setParameter("numEmpl", numEmpl).getResultList();
    	return !empleado.isEmpty() ? empleado.get(0) : null;
    }
    
    public DetEmpleadoDTO buscarPorIdSDI(Integer id) {
        List<DetEmpleadoDTO> empleado = emSGP.createNamedQuery("DetEmpleado.findByNumEmplSD", DetEmpleadoDTO.class).setParameter("idEmp", id).getResultList();
        return empleado.isEmpty() ? new DetEmpleadoDTO() : empleado.get(0);
    }
    
    public List<DetEmpleadoDTO> buscarActivoConSDI() {
        return emSGP.createNamedQuery("DetEmpleado.findByActiveSDI", DetEmpleadoDTO.class).getResultList();
    }
    
    public List<DetEmpleadoDTO> buscarActivoAndEmpresa(Integer idEmpresa) {
    	List<DetEmpleadoDTO> lista = null;
    	lista = emSGP.createNamedQuery("DetEmpleado.findByActiveAndIdEmpresa", DetEmpleadoDTO.class)
    			.setParameter("idEmpresa", idEmpresa)
    			.getResultList();
    	
    	return lista;
    }

}
