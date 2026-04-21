package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.dianolaboral.DiasNoLaboralesBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.DateUtil;

@Named(value = "regVacacionesBean")
@ViewScoped
public class RegistroVacacionesBean implements Serializable {

	private static final long serialVersionUID = -1867635090603750012L;
	private static final Logger log = LogManager.getLogger(RegistroVacacionesBean.class);
	
	private EmpleadoDAO empleadoDAO;
	private List<DetEmpleado> empleados;
	private DetEmpleado empleado = null;
	
	private DetVacaciones periodoVacacional;
	
	private List<Date> diasDeshabilitados;
	private Boolean desbloquearDiasDeDescanso;
    private Boolean desbloquearDiasNoLaborales;
    private List<Date> diasNoLaborales;
    private List<Integer> invalidDays;
	
	public RegistroVacacionesBean() {
		log.info("Entrando al registro de vacaciones de los empleados...");
		empleadoDAO = new EmpleadoDAO();
		
		this.periodoVacacional = new DetVacaciones();
		this.desbloquearDiasDeDescanso = Boolean.FALSE;
		this.desbloquearDiasNoLaborales = Boolean.FALSE;
		
		log.info("Termina el constructor.");
	}
	
	public List<DetEmpleado> buscarEmpleado(String query) {
    	List<DetEmpleado> empleados = null;
    	
    	try {
    		if(query == null)
    			return new ArrayList<DetEmpleado>();
    		
    		if(query.trim().equals(""))
    			return new ArrayList<DetEmpleado>();
    		
    		log.info("Buscando empleados por: {}", query);
    		empleados = empleadoDAO.buscarPorNombrePrimerSegundoApellido(query, DateUtil.now());
    		
    	} catch(Exception ex) {
    		empleados = new ArrayList<DetEmpleado>();
    	}
    	return empleados;
    }
	
	public void asignarEmpleado() {
    	
        try {
        	log.info("Empleado: {}", this.empleado);
        	
        	this.periodoVacacional.setEmpleado(this.empleado);
        	
        	
        	this.mostrarDiasLaborales();
        	this.mostrarDiasDeDescanso();
        	
        } catch(Exception ex) {
        	log.warn("Problema para asignar al empleado: {}", ex.getMessage());
        } finally {
        	PrimeFaces.current().ajax().update("form:messages");
        }
    }
	
	public void mostrarDiasDeDescanso() {
		if(this.desbloquearDiasDeDescanso.booleanValue()) {
			log.debug("Ocultar días de descanso...");
			this.invalidDays = new ArrayList<Integer>();
		} else {
			log.debug("Mostrar días de descanso...");
			this.invalidDays = SolicitudPermisoBL.obtenerDiasSeleccionados(this.empleado.getDatoEmpresa());
			log.info("Días de descanso: {}", this.invalidDays);
		}
	}
    
    public void mostrarDiasLaborales() {
    	Date fechaInicio = DateUtil.now();
    	Date fechaFin = DateUtil.now();
    	
    	fechaInicio = DateUtil.addMonth(fechaInicio, -6);
    	fechaFin = DateUtil.addMonth(fechaFin, 1);
    	
		if(this.desbloquearDiasNoLaborales.booleanValue()) {
			log.debug("Ocultar días dias no laborales...");
			this.diasNoLaborales = new ArrayList<Date>();
		} else {
			log.debug("Mostrar días no laborales...");
			this.diasNoLaborales = DiasNoLaboralesBL.buscarPorPeriodo(new Date(fechaInicio.getTime()), new Date(fechaFin.getTime()));
			log.info("Días no laborales: {}", this.diasNoLaborales);
		}
		this.actualizarPeriodo();
	}
    
    public void actualizarPeriodo() {
    	if(this.diasDeshabilitados == null)
    		this.diasDeshabilitados = new ArrayList<Date>();
    	
    	this.diasDeshabilitados.clear();
    	
    	if(this.diasNoLaborales != null)
    		this.diasDeshabilitados.addAll(this.diasNoLaborales);
    	
//    	if(this.registrosEmpleado != null)
//    		this.diasDeshabilitados.addAll(this.registrosEmpleado);
    }
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	public List<DetEmpleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<DetEmpleado> empleados) {
		this.empleados = empleados;
	}

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public DetVacaciones getPeriodoVacacional() {
		return periodoVacacional;
	}

	public void setPeriodoVacacional(DetVacaciones periodoVacacional) {
		this.periodoVacacional = periodoVacacional;
	}

	public List<Date> getDiasDeshabilitados() {
		return diasDeshabilitados;
	}

	public void setDiasDeshabilitados(List<Date> diasDeshabilitados) {
		this.diasDeshabilitados = diasDeshabilitados;
	}

	public Boolean getDesbloquearDiasDeDescanso() {
		return desbloquearDiasDeDescanso;
	}

	public void setDesbloquearDiasDeDescanso(Boolean desbloquearDiasDeDescanso) {
		this.desbloquearDiasDeDescanso = desbloquearDiasDeDescanso;
	}

	public Boolean getDesbloquearDiasNoLaborales() {
		return desbloquearDiasNoLaborales;
	}

	public void setDesbloquearDiasNoLaborales(Boolean desbloquearDiasNoLaborales) {
		this.desbloquearDiasNoLaborales = desbloquearDiasNoLaborales;
	}

	public List<Date> getDiasNoLaborales() {
		return diasNoLaborales;
	}

	public void setDiasNoLaborales(List<Date> diasNoLaborales) {
		this.diasNoLaborales = diasNoLaborales;
	}

	public List<Integer> getInvalidDays() {
		return invalidDays;
	}

	public void setInvalidDays(List<Integer> invalidDays) {
		this.invalidDays = invalidDays;
	}
	

}
