package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.model.CatArea;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPerfil;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.CatPuesto;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabo
 */
@Stateless
@LocalBean
public class EmpleadoDAO extends IBaseDAO<DetEmpleadoDTO, Integer> implements Serializable{
    
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
    
    @Override
    public List<DetEmpleadoDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<DetEmpleadoDTO> buscarActivo() {
        return emSGP.createNamedQuery("DetEmpleado.findByActive", DetEmpleadoDTO.class).getResultList();
    }
    
    @Override
    public List<DetEmpleadoDTO> buscarPorCriterios(DetEmpleadoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void actualizar(DetEmpleadoDTO e)throws SGPException {
        final DetEmpleado empleado = new DetEmpleado();
        try {
            emSGP.getTransaction().begin();
            empleado.setIdEmpleado(e.getIdEmpleado());
            empleado.setNombre(e.getNombre());
            empleado.setPrimerAp(e.getPrimerAp());
            empleado.setSegundoAp(e.getSegundoAp());
            empleado.setCurp(e.getCurp());
            empleado.setFechaNacimiento(e.getFechaNacimiento());
            empleado.setCorreo(e.getCorreo());
            empleado.setRfc(e.getRfc());
            empleado.setNss(e.getNss());
            empleado.setFechaIngreso(e.getFechaIngreso());
            empleado.setIdPerfil(e.getCatPerfilDTO() != null ? emSGP.getReference(CatPerfil.class, e.getCatPerfilDTO().getIdPerfil()) : null);
            empleado.setIdEmpresa(e.getCatEmpresaDTO() != null ? emSGP.getReference(CatEmpresa.class, e.getCatEmpresaDTO().getIdEmpresa()) : null);
            empleado.setIdPuesto(e.getCatPuestoDTO() != null ? emSGP.getReference(CatPuesto.class, e.getCatPuestoDTO().getIdPuesto()) : null);
            empleado.setIdArea(e.getCatAreaDTO() != null ? emSGP.getReference(CatArea.class, e.getCatAreaDTO().getIdArea()) : null);
            empleado.setIdPlanta(e.getCatPlantaDTO() != null ? emSGP.getReference(CatPlanta.class, e.getCatPlantaDTO().getIdPlanta()) : null);
            empleado.setFechaRegistro(e.getFechaRegistro());
            empleado.setFechaModificacion(new Date());
            empleado.setActivo(e.getActivo());
            empleado.setNumEmpleado(e.getNumEmpleado());
            empleado.setFotografia(e.getFotografia());
            empleado.setSueldoDiario(e.getSueldoDiario());
            emSGP.merge(empleado);
            emSGP.getTransaction().commit();
            emSGP.detach(empleado);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0011: " + ex.getMessage() + ". Error al actualizar el empleado " + e.getNumEmpleado() != null ? e.getNumEmpleado() : null);
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
        final DetEmpleado empleado = new DetEmpleado();
        try {
            emSGP.getTransaction().begin();
            empleado.setNombre(e.getNombre());
            empleado.setPrimerAp(e.getPrimerAp());
            empleado.setSegundoAp(e.getSegundoAp());
            empleado.setCurp(e.getCurp());
            empleado.setFechaNacimiento(e.getFechaNacimiento());
            empleado.setCorreo(e.getCorreo());
            empleado.setRfc(e.getRfc());
            empleado.setNss(e.getNss());
            empleado.setFechaIngreso(e.getFechaIngreso());
            empleado.setIdPerfil(e.getCatPerfilDTO() != null ? emSGP.getReference(CatPerfil.class, e.getCatPerfilDTO().getIdPerfil()) : null);
            empleado.setIdEmpresa(e.getCatEmpresaDTO() != null ? emSGP.getReference(CatEmpresa.class, e.getCatEmpresaDTO().getIdEmpresa()) : null);
            empleado.setIdPuesto(e.getCatPuestoDTO() != null ? emSGP.getReference(CatPuesto.class, e.getCatPuestoDTO().getIdPuesto()) : null);
            empleado.setIdArea(e.getCatAreaDTO() != null ? emSGP.getReference(CatArea.class, e.getCatAreaDTO().getIdArea()) : null);
            empleado.setIdPlanta(e.getCatPlantaDTO() != null ? emSGP.getReference(CatPlanta.class, e.getCatPlantaDTO().getIdPlanta()) : null);
            empleado.setFechaRegistro(new Date());
            empleado.setActivo((short) 1);
            empleado.setFotografia(e.getFotografia());
            empleado.setNumEmpleado(String.format("%0" + 4 + "d", (Integer) emSGP.createNamedQuery("DetEmpleado.getNumEmpleado").getSingleResult() + 1));
           
            empleado.setSueldoDiario(e.getSueldoDiario());
            emSGP.persist(empleado);
            emSGP.getTransaction().commit();
            e.setIdEmpleado(empleado.getIdEmpleado());
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
    
    public DetEmpleadoDTO buscarPorNumEmplAndBiometricos(String numEmpl) {
    	List<DetEmpleadoDTO> empleado = emSGP.createNamedQuery("DetEmpleado.findByNumEmpl", DetEmpleadoDTO.class).setParameter("numEmpl", numEmpl).getResultList();
    	return !empleado.isEmpty() ? empleado.get(0) : null;
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
