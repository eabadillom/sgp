package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.SGPException;

@Deprecated
@LocalBean
@Stateless
public class RegistroDAO extends IBaseDAO<DetRegistroDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(RegistroDAO.class);
    
    public static synchronized DetRegistroDTO getDTO(DetRegistro model) {
    	DetRegistroDTO dto = null;
    	try {
    		dto = new DetRegistroDTO();
    		dto.setIdRegistro(model.getIdRegistro());
    		dto.setDetEmpleadoDTO(new EmpleadoDAO().getDTO(model.getIdEmpleado()));
    		dto.setFechaEntrada(model.getFechaEntrada());
    		dto.setFechaSalida(model.getFechaSalida());
    		dto.setCatEstatusRegistroDTO(EstatusRegistroDAO.getDTO(model.getIdEstatus()));
    	} catch(Exception ex) {
    		log.warn("No es posible convertir Model a DTO: {}", ex.getMessage());
    	}
    	return dto;
    }
    
    public static synchronized DetRegistro getModel(DetRegistroDTO dto) {
    	DetRegistro model = null;
    	try {
    		model = new DetRegistro();
    		model.setIdRegistro(dto.getIdRegistro());
    		model.setIdEmpleado(new EmpleadoDAO().getModel(dto.getDetEmpleadoDTO()));
    		model.setFechaEntrada(dto.getFechaEntrada());
    		model.setFechaSalida(dto.getFechaSalida());
    		model.setIdEstatus(EstatusRegistroDAO.getModel(dto.getCatEstatusRegistroDTO()));
    	} catch(Exception ex) {
    		log.warn("No es posible convertir DTO a Model: {}", ex.getMessage());
    	}
    	return model;
    }


    @Override
    public DetRegistroDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetRegistroDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetRegistroDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetRegistroDTO> buscarPorCriterios(DetRegistroDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(DetRegistroDTO e) throws SGPException {
        DetRegistro registro = new DetRegistro();
        try {
            emSGP.getTransaction().begin();
            registro = emSGP.getReference(DetRegistro.class, e.getIdRegistro());
            registro.setFechaSalida(e.getFechaSalida());
            log.info("Empleado: {}", e);
            emSGP.merge(registro);
            emSGP.getTransaction().commit();
            emSGP.detach(registro);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            throw new SGPException("Error al actualizar al fecha de salida del empleado");
        }
    }

    @Override
    public void eliminar(DetRegistroDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(DetRegistroDTO e) throws SGPException {
        final DetRegistro registro = new DetRegistro();
        try {
            emSGP.getTransaction().begin();
            registro.setIdEmpleado(e.getDetEmpleadoDTO() != null ? emSGP.getReference(DetEmpleado.class, e.getDetEmpleadoDTO().getIdEmpleado()) : null);
            registro.setFechaEntrada(e.getFechaEntrada());
            registro.setFechaSalida(e.getFechaSalida());
            registro.setIdEstatus(e.getDetEmpleadoDTO() != null ? emSGP.getReference(CatEstatusRegistro.class, e.getCatEstatusRegistroDTO().getIdEstatus()) : null);
            
            log.info("Empleado: {}", e);
            emSGP.persist(registro);
            emSGP.getTransaction().commit();
            emSGP.detach(registro);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0003: " + ex.getMessage() + ". Error al guardar el registro del empleado: " + e.getDetEmpleadoDTO().getNumEmpleado() != null ? e.getDetEmpleadoDTO().getNumEmpleado() : null);
            throw new SGPException("Problema para guardar el registro de asistencia.");
        }
    }
    
    public List<DetRegistroDTO> consultaRegistrosPorIdEmp(Integer idEmpleado) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByIdEmp", DetRegistroDTO.class).setParameter("idEmp", idEmpleado)
                .getResultList();
        return registros;
    }

    public List<DetRegistroDTO> buscarPorIdFechaEntrada(Integer id, String fechaEntrada) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByIdEmplActivo", DetRegistroDTO.class).setParameter("idEmp", id).setParameter("fechaEntrada", fechaEntrada).getResultList();
        return registros;
    }
    
    public DetRegistroDTO buscarPorEmpleadoFechaEntrada(Integer idEmpleado, Date fechaEntradaInicio, Date fechaEntradaFin) {
    	DetRegistroDTO dto = null;
    	DetRegistro model = null;
    	try {
    		model = emSGP.createNamedQuery("DetRegistro.findByIdEmpleadoAndFecha", DetRegistro.class)
    				.setParameter("idEmpleado", idEmpleado)
    				.setParameter("fechaEntradaInicio", fechaEntradaInicio)
    				.setParameter("fechaEntradaFin", fechaEntradaFin)
    				.getSingleResult()
    				;
    		log.debug("IdEmpleado: {}", model.getIdEmpleado().getIdEmpleado());
    		log.debug("Estatus: {}", model.getIdEstatus().getIdEstatus());
    		dto = getDTO(model);
    	} catch(Exception ex) {
    		dto = null;
    	}
    	
    	return dto;
    }
    
    public List<DetRegistroDTO> buscarRegistroPorAnio(String year) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByYear", DetRegistroDTO.class).setParameter("fechaEntrada", year).getResultList();
        return registros;
    }
    
    public List<DetRegistroDTO> buscarRegistroNomina(Date fechaEntrada, Date fechaSalida) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByNomina", DetRegistroDTO.class).setParameter("fechaEntrada", fechaEntrada).setParameter("fechaSalida", fechaSalida).getResultList();
        return registros;
    }
    
    public DetRegistroDTO buscarEntradaHoy(Integer id, Date today) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findToday", DetRegistroDTO.class)
                .setParameter("idEmp", id)
                .setParameter("today", today, TemporalType.TIMESTAMP).getResultList();
        return registros.isEmpty() ? null : registros.get(0);
    }
    
    @Deprecated
    public List<DetRegistroDTO> buscar(Integer idEmpleado, Date fechaEntrada, Date fechaSalida) {
    	List<DetRegistroDTO> lista = null;
    	lista = emSGP.createNamedQuery("DetRegistro.findByIdEmpleadoPeriodo", DetRegistroDTO.class)
    			.setParameter("idEmpleado", idEmpleado)
    			.setParameter("fechaEntrada", fechaEntrada)
    			.setParameter("fechaSalida", fechaSalida)
    			.getResultList()
    			;
    	
    	return lista;
    }

	public List<DetRegistroDTO> buscarPorPlantaPeriodo(Integer idPlanta, Date fechaInicio, Date fechaFin) {
		List<DetRegistroDTO> registros = null;
		List<DetRegistro> list = null;
		try {
			list = emSGP.createNamedQuery("DetRegistro.findByPlantaPeriodo", DetRegistro.class)
			.setParameter("idPlanta", idPlanta)
			.setParameter("fechaInicio", fechaInicio)
			.setParameter("fechaFin", fechaFin)
			.getResultList()
			;
			registros = new ArrayList<DetRegistroDTO>();
			for(DetRegistro model : list) {
				log.debug("IdEmpleado: {}", model.getIdEmpleado().getIdEmpleado());
				log.debug("Id Planta: {}", model.getIdEmpleado().getDatoEmpresa().getPlanta().getIdPlanta());
				DetRegistroDTO dto = getDTO(model);
				registros.add(dto);
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de registros...", ex);
			registros = new ArrayList<DetRegistroDTO>();
		}
		return registros;
	}
}
