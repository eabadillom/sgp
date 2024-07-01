package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.util.SGPException;

@Stateless
@LocalBean
public class EmpleadoDAO extends DAO<DetEmpleadoDTO, DetEmpleado, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LogManager.getLogger(EmpleadoDAO.class);
    
    public synchronized DetEmpleadoDTO getDTO(DetEmpleado model) {
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
    		dto.setCatAreaDTO(CatAreaDAO.getDTO(model.getDatoEmpresa().getArea()));
    		dto.setFotografia(null);
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
    		//TODO deshabilitar la extracción de fotografía...
    		dto.setFotografia(model.getFotografia());
    		dto.setSueldoDiario(model.getSueldoDiario());
    		
    		if(isFullInfo == false)
    			return dto;
    		
//    		dto.setCatAreaDTO(CatAreaDAO.getDTO(model.getDatoEmpresa().getArea()));
    		dto.setDatoEmpresa(new DatoEmpresaDAO().getDTO(model.getDatoEmpresa()));
    		
    	} catch(Exception ex) {
    		log.warn("Problema para obtener la información del empleado...", ex);
    		dto = null;
    	}
    	return dto;
    }
    
    public synchronized DetEmpleado getModel(DetEmpleadoDTO dto) {
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
    		//TODO deshabilitar la extracción de fotografía...
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
    		model.setSueldoDiario(dto.getSueldoDiario());
    		
    		if(isFullInfo == false)
    			return model;
    		
    		model.setDatoEmpresa(new DatoEmpresaDAO().getModel(dto.getDatoEmpresa()));
    		
    	} catch(Exception ex) {
    		model = null;
    	}
    	return model;
    }
    
    
    
    @Override
    public DetEmpleadoDTO buscarPorId(Integer id) {
    	EntityManager emSGP = null;
    	List<DetEmpleadoDTO> empleados = null;
    	DetEmpleadoDTO empleado = null;
    	try {
    		emSGP = getEntityManager();
    		empleados = emSGP.createNamedQuery("DetEmpleado.findByID", DetEmpleadoDTO.class).setParameter("idEmp", id).getResultList();
    	} catch(Exception ex) {
    		
    	} finally {
    		close(emSGP);
    		empleado = empleados.isEmpty() ? new DetEmpleadoDTO() : empleados.get(0);
    	}
        
        return empleado;
    }
    
    public DetEmpleadoDTO buscarPorId(Integer id, boolean isFullInfo) {
    	DetEmpleadoDTO dto = null;
    	DetEmpleado model = null;
    	EntityManager emSGP = null;
    	
    	try {
    		emSGP = getEntityManager();
    		model = emSGP.find(DetEmpleado.class, id);
    		if(isFullInfo) {
    			log.info("id dato empresa: {}", model.getDatoEmpresa().getId() == null ? null : model.getDatoEmpresa().getId());
//    			log.info("id foto: {}", model.getEmpleadoFoto().getId() == null ? null : model.getEmpleadoFoto().getId());
    		}
    		dto = getDTO(model, isFullInfo);
    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado con id " + id, ex);
    	} finally {
    		close(emSGP);
    	}
    	
    	return dto;
    }
    
    public DetEmpleadoDTO buscarPorId(Integer id, boolean isFullInfo, boolean isGetFoto) {
    	DetEmpleadoDTO dto = null;
    	DetEmpleado model = null;
    	EntityManager emSGP = null;
    	
    	try {
    		emSGP = getEntityManager();
    		model = emSGP.find(DetEmpleado.class, id);
    		if(isFullInfo) {
    			log.info("id dato empresa: {}", model.getDatoEmpresa().getId() == null ? null : model.getDatoEmpresa().getId());
    		}
    		dto = getDTO(model, isFullInfo);
    		
    		if(isGetFoto) {
    			dto.setEmpleadoFoto(new EmpleadoFotoDAO().getDTO(model.getEmpleadoFoto()));
    			log.info("id foto: {}", model.getEmpleadoFoto() == null ? null : model.getEmpleadoFoto().getId());
    		}
    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado con id " + id, ex);
    	} finally {
    		close(emSGP);
    	}
    	
    	return dto;
    }
    
    @Override
    public List<DetEmpleadoDTO> buscarTodos() {
        List<DetEmpleadoDTO> dtoList = null;
        List<DetEmpleado> modelList = null;
        EntityManager emSGP = null;
        
        try {
        	emSGP = getEntityManager();
        	modelList = emSGP.createNamedQuery("DetEmpleado.getAll", DetEmpleado.class)
        			.getResultList();
        	
        	dtoList = new ArrayList<DetEmpleadoDTO>();
        	for(DetEmpleado model : modelList) {
        		DetEmpleadoDTO dto = getDTO(model);
        		dtoList.add(dto);
        	}
        } catch(Exception ex) {
        	log.error("Problema para obtener el listado de empleados...", ex);
        } finally {
        	close(emSGP);
        }
        
        return dtoList;
    }
    
    public List<DetEmpleadoDTO> buscarTodos(boolean isFullInfo) {
        List<DetEmpleadoDTO> dtoList = null;
        List<DetEmpleado> modelList = null;
        EntityManager emSGP = null;
        
        try {
        	emSGP = getEntityManager();
        	modelList = emSGP.createNamedQuery("DetEmpleado.getAll", DetEmpleado.class)
        			.getResultList();
        	
        	dtoList = new ArrayList<DetEmpleadoDTO>();
        	for(DetEmpleado model : modelList) {
        		DetEmpleadoDTO dto = getDTO(model, isFullInfo);
        		dtoList.add(dto);
        		
        		if(isFullInfo == false) {
        			continue;
        		}
        		
        		log.info("id area: {}", model.getDatoEmpresa().getArea().getIdArea());
        		log.info("id dato empresa: {}", model.getDatoEmpresa());
        	}
        } catch(Exception ex) {
        	log.error("Problema para obtener el listado de empleados...", ex);
        } finally {
        	close(emSGP);
        }
        
        return dtoList;
    }
    
    @Override
    public List<DetEmpleadoDTO> buscarActivo() {
    	EntityManager emSGP = null;
    	List<DetEmpleadoDTO> empleados = null;
    	try {
    		emSGP = getEntityManager();
    		empleados = emSGP.createNamedQuery("DetEmpleado.findByActive", DetEmpleadoDTO.class).getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener el listado de empleados...", ex);
    	}
        return empleados;
    }
    
    public List<DetEmpleadoDTO> buscarActivo(boolean isFullInfo) {
    	List<DetEmpleadoDTO> dtoList = null;
    	List<DetEmpleado> modelList = null;
    	EntityManager emSGP = null;
    	
    	try {
    		emSGP = this.getEntityManager();
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
    	} finally {
    		close(emSGP);
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
        EntityManager emSGP = null;
        try {
        	emSGP = this.getEntityManager();
        	model = getModel(e);
            emSGP.getTransaction().begin();
            emSGP.merge(model);
            emSGP.getTransaction().commit();
            emSGP.detach(model);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0011: " + ex.getMessage() + ". Error al actualizar el empleado " + e.getNumEmpleado() != null ? e.getNumEmpleado() : null);
            log.error("Problema al actualizar el empleado...", ex);
        } finally {
        	close(emSGP);
        }
    }
    
    @Override
    public void eliminar(DetEmpleadoDTO e) throws SGPException {
        DetEmpleado empleado = new DetEmpleado();
        EntityManager emSGP = null;
        try {
        	emSGP = this.getEntityManager();
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
        } finally {
        	close(emSGP);
        }
    }
    
    @Override
    public void guardar(DetEmpleadoDTO e) throws SGPException {
        DetEmpleado model = null;
        EntityManager emSGP = null;
        try {
        	emSGP = this.getEntityManager();
        	model = getModel(e);
        	
            emSGP.getTransaction().begin();
            emSGP.persist(model);
            emSGP.getTransaction().commit();
            e.setIdEmpleado(model.getIdEmpleado());
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            ex.printStackTrace();
            log.warn("EX-0009: " + ex.getMessage() + ". Error al guardar empleado " + e.getNumEmpleado() != null ? e.getNumEmpleado() : null);
        } finally {
        	close(emSGP);
        }
    }
    
    public DetEmpleadoDTO buscarPorNumEmpl(String numEmpl) {
    	List<DetEmpleadoDTO> empleados = null;
    	DetEmpleadoDTO empleado = null;
    	EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		empleados = emSGP.createNamedQuery("DetEmpleado.findByNumEmpl", DetEmpleadoDTO.class).setParameter("numEmpl", numEmpl).getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener el empleado...", ex);
    	} finally {
    		close(emSGP);
    		empleado = !empleados.isEmpty() ? empleados.get(0) : null;
    	}
    	
    	return empleado;
    }
    
    public DetEmpleadoDTO buscarPorNumeroEmpleado(String numeroEmpleado, boolean isFullInfo) {
    	DetEmpleadoDTO dto = null;
    	DetEmpleado model = null;
    	EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		model = emSGP.createNamedQuery("DetEmpleado.findByNumero", DetEmpleado.class)
    				.setParameter("numero",numeroEmpleado)
    				.getSingleResult()
    				;
    		
    		dto = getDTO(model, isFullInfo);
    	} catch(Exception ex) {
    		log.error("Problema para obtener el número de empleado " + numeroEmpleado, ex);
    		dto = null;
    	} finally {
    		close(emSGP);
    	}
    	
    	return dto;
    }
    
    public DetEmpleadoDTO buscarPorNumEmplAndBiometricos(String numEmpl) {
    	List<DetEmpleadoDTO> empleados = null;
    	DetEmpleadoDTO empleado = null;
    	EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		empleados = emSGP.createNamedQuery("DetEmpleado.findByNumEmpl", DetEmpleadoDTO.class).setParameter("numEmpl", numEmpl).getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener el listado de empleados...", ex);
    	} finally {
    		close(emSGP);
    		empleado = (!empleados.isEmpty()) ? empleados.get(0) : null;
    	}
    	
    	return empleado;
    }
    
    public DetEmpleadoDTO buscarConFoto(Integer idEmpleado) {
    	DetEmpleadoDTO dto = null;
    	DetEmpleado model = null;
    	EntityManager emSGP = null;
    	
    	try {
    		emSGP = this.getEntityManager();
    		model = emSGP.find(DetEmpleado.class, idEmpleado);
    		
    		if(model.getEmpleadoFoto() != null)
    			log.info("Id foto empleado: {}",model.getEmpleadoFoto().getId());
    		
    		dto = getDTO(model,false);
    		dto.setEmpleadoFoto(new EmpleadoFotoDAO().getDTO(model.getEmpleadoFoto()));
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener la foto del empleado...", ex);
    	} finally {
    		close(emSGP);
    	}
    	
    	return dto;
    }
    
    public DetEmpleadoDTO buscarPorNumEmplFechaRegistro(String numEmpl, String fechaEntrada) {
    	List<DetEmpleadoDTO> empleados = null;
    	DetEmpleadoDTO empleado = null;
    	EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		empleados = emSGP.createNamedQuery("DetEmpleado.findByNumEmpl", DetEmpleadoDTO.class).setParameter("numEmpl", numEmpl).getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener el empleado...", ex);
    	} finally {
    		close(emSGP);
    		empleado = !empleados.isEmpty() ? empleados.get(0) : null;
    	}
    	
    	
    	return empleado;
    }
    
    public DetEmpleadoDTO buscarPorIdSDI(Integer id) {
    	List<DetEmpleadoDTO> empleados = null;
    	DetEmpleadoDTO empleado = null;
    	EntityManager emSGP = null;
    	try {
    		emSGP = this.getEntityManager();
    		empleados = emSGP.createNamedQuery("DetEmpleado.findByNumEmplSD", DetEmpleadoDTO.class).setParameter("idEmp", id).getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener el empleado...", ex);
    	} finally {
    		close(emSGP);
    		empleado = empleados.isEmpty() ? new DetEmpleadoDTO() : empleados.get(0);
    	}
        
        return empleado;
    }
    
    public List<DetEmpleadoDTO> buscarActivoConSDI() {
    	EntityManager emSGP = null;
    	List<DetEmpleadoDTO> dtoList = null;
    	try {
    		emSGP = this.getEntityManager();
    		dtoList = emSGP.createNamedQuery("DetEmpleado.findByActiveSDI", DetEmpleadoDTO.class).getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener la lista de empleados...", ex);
    	} finally {
    		close(emSGP);
    	}
        return dtoList;
    }
    
    public List<DetEmpleadoDTO> buscarActivoAndEmpresa(Integer idEmpresa) {
    	EntityManager emSGP = null;
    	List<DetEmpleadoDTO> lista = null;
    	try {
    		emSGP = this.getEntityManager();
    		lista = emSGP.createNamedQuery("DetEmpleado.findByActiveAndIdEmpresa", DetEmpleadoDTO.class)
        			.setParameter("idEmpresa", idEmpresa)
        			.getResultList();
    	} catch(Exception ex) {
    		log.warn("Problema para obtener la lista de empleados...", ex);
    	} finally {
    		close(emSGP);
    	}
    	return lista;
    }
}
