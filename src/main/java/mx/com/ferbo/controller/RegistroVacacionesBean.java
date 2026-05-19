package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import mx.com.ferbo.business.dianolaboral.DiasNoLaboralesBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.incidencia.IncidenciaBL;
import mx.com.ferbo.business.incidencia.IncidenciaVacacionesBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.business.incidencia.SolicitudVacacionesBL;
import mx.com.ferbo.business.vacaciones.VacacionesBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaPermisoDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetDiaPermiso;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

@Named(value = "regVacacionesBean")
@ViewScoped
public class RegistroVacacionesBean implements Serializable {

	private static final long serialVersionUID = -1867635090603750012L;
	private static final Logger log = LogManager.getLogger(RegistroVacacionesBean.class);
	
	private HttpServletRequest request;
	private DetEmpleado empleado = null;
	private DetEmpleado autorizador = null;
	private List<DetEmpleado> empleados;
	private EmpleadoDAO empleadoDAO;
	private Date periodoInicio;
	private Date periodoFin;
	private DetVacaciones periodoVacacional;
	private IncidenciaPermisoDAO incidenciaDAO;
	private CatTipoSolicitud tipoSolicitud;
	private TipoSolicitudDAO tipoSolicitudDAO;
	private List<Date> diasDeshabilitados;
	private Boolean mostrarCanceladas;
	private Boolean desbloquearDiasDeDescanso;
    private Boolean desbloquearDiasNoLaborales;
    private List<Date> diasNoLaborales;
    private List<Integer> invalidDays;
    private List<Date> diasDeAsueto;
    private List<DetIncidencia> permisos;
    private DetIncidencia permiso;
    private List<DetVacaciones> periodosVacacionales;
    private List<Date> diasSolicitados;
    
    private String mensajeConfirmacion;
    private ManageStatus status;
	
	public RegistroVacacionesBean() throws SGPException {
		log.info("Entrando al registro de vacaciones de los empleados...");
		this.request           = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.autorizador       = (DetEmpleado) request.getSession(false).getAttribute("empleado");
		this.empleadoDAO       = new EmpleadoDAO();
		this.periodoVacacional = new DetVacaciones();
		this.periodoInicio     = DateUtil.now();
		this.periodoFin        = DateUtil.now();
		this.incidenciaDAO     = new IncidenciaPermisoDAO();
		this.status            = new ManageStatus();
		this.tipoSolicitudDAO  = new TipoSolicitudDAO();
		this.tipoSolicitud     = this.tipoSolicitudDAO.buscarPorClave(SolicitudPermisoBL.TP_PERMISO)
				.orElseThrow(() -> new SGPException("Tipo de solicitud no encontrada"));
		this.mostrarCanceladas = Boolean.FALSE;
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
        	this.cargarPermisos();
        	this.mostrarDiasLaborales();
        	this.mostrarDiasDeDescanso();
        	
        } catch(Exception ex) {
        	log.warn("Problema para asignar al empleado: {}", ex.getMessage());
        } finally {
        	PrimeFaces.current().ajax().update("form:messages");
        }
    }
	
	public void cargarPermisos() {
		log.info("Cargando lista de permisos del empleado...");
		if(this.permisos == null)
			this.permisos = new ArrayList<DetIncidencia>();
		
		this.permisos.clear();
		this.permisos.addAll(IncidenciaVacacionesBL.buscarPendientes(this.empleado));
		this.permisos.addAll(IncidenciaVacacionesBL.buscarAprobadas(empleado, periodoInicio, periodoFin));
		this.permisos.addAll(IncidenciaVacacionesBL.buscarRechazadas(empleado, periodoInicio, periodoFin));
		this.permisos.addAll(IncidenciaVacacionesBL.buscarCanceladas(empleado, periodoInicio, periodoFin));
		
		if(this.mostrarCanceladas == false)
			permisos = permisos.stream()
			.filter(item -> !IncidenciaBL.ST_CANCELADA.equalsIgnoreCase(item.getSolPermiso().getEstatus().getClave()) )
			.collect(Collectors.toList())
			;
	}
	
	public void nuevoPermiso() {
		FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Cargar informacion";
        
		try {
			if(this.empleado == null)
				throw new SGPException("Debe seleccionar un empleado.");
			
			this.permiso = IncidenciaBL.create(IncidenciaBL.TP_VACACIONES, this.empleado);
			DiasNoLaboralesBL.diasDescansoEstanActualizados();
            EmpleadoBL.empleadoTieneDiasLaborales(this.empleado);
            this.periodosVacacionales = VacacionesBL.cargarPeriodosConSaldo(this.empleado.getIdEmpleado(), DateUtil.now());
            this.diasSolicitados = new ArrayList<Date>();
            PrimeFaces.current().executeScript("PF('dgVacaciones').show()");
		} catch(Exception ex) {
			log.error("Problema para crear una solicitud de vacaciones...", ex);
			mensaje = "Problema para generar la solicitud de vacaciones.";
            severity = FacesMessage.SEVERITY_ERROR;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void mostrarDiasDeDescanso() {
		if(this.desbloquearDiasDeDescanso.booleanValue()) {
			this.invalidDays = new ArrayList<Integer>();
		} else {
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
    }
    
    public void cargarPermiso(DetIncidencia permiso) {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		try {
			log.info("Cargando permiso...");
			
			this.permiso = incidenciaDAO.cargar(permiso.getIdIncidencia())
					.orElseThrow(() -> new SGPException("Incidencia no encontrada."));
			
			this.periodosVacacionales = VacacionesBL.cargarPeriodosConSaldo(this.empleado.getIdEmpleado(), DateUtil.now());
			if(this.periodosVacacionales.contains(this.permiso.getSolPermiso().getVacaciones()) == false)
				this.periodosVacacionales.add(this.permiso.getSolPermiso().getVacaciones());
			
			if(this.permiso.getSolPermiso().getDiasPermiso() == null
					|| this.permiso.getSolPermiso().getDiasPermiso().size() == 0)
				this.diasSolicitados = DateUtil.generarArreglosFechas(permiso.getSolPermiso().getFechaInicio(), permiso.getSolPermiso().getFechaFin());
			else
				this.diasSolicitados = this.permiso.getSolPermiso().getDiasPermiso().stream()
				.map(DetDiaPermiso::getFecha)
				.collect(Collectors.toList())
				;
			
			this.desbloquearDiasDeDescanso = new Boolean(false);
			this.desbloquearDiasNoLaborales = new Boolean(false);
		} catch(SGPException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al cargar la solicitud.";
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:dg-vacaciones", "form:cmdSolicitar");
		}
	}
    
    public void cargarPeriodoEnCurso() {
		List<DetVacaciones> cargarPeriodosConSaldoEnCurso = VacacionesBL.cargarPeriodosConSaldoEnCurso(this.empleado.getIdEmpleado(), DateUtil.now());
		this.periodosVacacionales.addAll(cargarPeriodosConSaldoEnCurso);
	}
    
    public void onPeriodoSelect(SelectEvent<DetVacaciones> event) {
    	FacesMessage.Severity severity = null;
    	log.debug("Seleccionando periodo vacacional: {}", event.getObject().getIdVacaciones());
    	
    	DetVacaciones periodo = (DetVacaciones) event.getObject();
    	
    	if(this.diasSolicitados == null)
    		this.diasSolicitados = new ArrayList<Date>();
    	
    	this.diasSolicitados.clear();
    	severity = FacesMessage.SEVERITY_INFO;
    	String mensaje = String.format("Puede seleccionar %d días", periodo.getDiasDisponibles());
    	FacesMessage msg = new FacesMessage(severity, mensaje, null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        PrimeFaces.current().ajax().update("form:messages");
    }
    
    public void guardar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = null;
		
		try {
			log.info("Guardando solicitud de permiso...");
			if(this.permiso.getIdIncidencia() != null)
				throw new SGPException("El periodo solicitado ya se encuentra registrado.");
			
			this.permiso.getSolPermiso()
				.setDiasPermiso(SolicitudVacacionesBL.toDiaPermisoList(this.permiso.getSolPermiso(), this.diasSolicitados));
			
			SolicitudVacacionesBL.validator()
				.incidencia(this.permiso)
				.validate();
			
			IncidenciaBL.guardar(this.permiso);
			IncidenciaBL.enviarNotificacion(this.permiso);
			PrimeFaces.current().executeScript("PF('dgVacaciones').hide();");
			
			this.cargarPermisos();
			titulo = "Solicitud correcta";
			mensaje = "La solicitud se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex){
			log.warn(ex.getMessage());
			titulo = "Verifique su solicitud";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para guardar la solicitud...", ex);
			titulo = "Error del sistema";
			mensaje = "Ocurrió un problema al guardar la solicitud del permiso.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			this.mensajeConfirmacion = mensaje;
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:resumen");
		}
	}
    
    public void actualizar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		
		try {
			log.info("Actualizando solicitud de permiso...");
			
			this.permiso.getSolPermiso().setFechaFin(this.permiso.getSolPermiso().getFechaInicio());
			this.permiso.getSolPermiso().setTipoSolicitud(tipoSolicitud);
			
			IncidenciaBL.guardar(this.permiso);
			this.cargarPermisos();
			
			PrimeFaces.current().executeScript("PF('dgVacaciones').hide();");
			mensaje = "Tu solicitud de vacaciones se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex){
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al guardar la solicitud del permiso.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-permisos");
		}
	}
    
    public void cancelar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		
		try {
			log.info("Cancelando solicitud de vacaciones...");
			IncidenciaBL.cancelar(this.permiso);
			this.cargarPermisos();
			PrimeFaces.current().executeScript("PF('dgVacaciones').hide();");
			mensaje = "La solicitud se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex){
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al guardar la solicitud de vacaciones.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-vacaciones");
		}
		
		
	}
    
    public boolean permiteActualizar() {
		boolean respuesta = false;
		
		if(permiso == null)
			return respuesta;
		
		if(permiso.getIdIncidencia() == null)
			return respuesta;
		
		switch(permiso.getSolPermiso().getEstatus().getClave()) {
			case SolicitudPermisoBL.STATUS_ENVIADA:
				respuesta = true;
				break;
			case SolicitudPermisoBL.STATUS_APROBADA:
			case SolicitudPermisoBL.STATUS_CANCELADA:
			case SolicitudPermisoBL.STATUS_RECHAZADA:
				respuesta = false;
				break;
		}
		
		return respuesta;
	}
	
	public boolean permiteCancelar() {
		boolean respuesta = false;
		
		if(permiso == null)
			return respuesta;
		
		if(permiso.getIdIncidencia() == null)
			return respuesta;
		
		switch(permiso.getSolPermiso().getEstatus().getClave()) {
			case SolicitudPermisoBL.STATUS_ENVIADA:
			case SolicitudPermisoBL.STATUS_APROBADA:
				respuesta = true;
				break;
			case SolicitudPermisoBL.STATUS_RECHAZADA:
			case SolicitudPermisoBL.STATUS_CANCELADA:
				respuesta = false;
				break;
		}
		
		return respuesta;
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

	public List<DetVacaciones> getPeriodosVacacionales() {
		return periodosVacacionales;
	}

	public void setPeriodosVacacionales(List<DetVacaciones> periodosVacacionales) {
		this.periodosVacacionales = periodosVacacionales;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(Date periodoFin) {
		this.periodoFin = periodoFin;
	}

	public ManageStatus getStatus() {
		return status;
	}

	public void setStatus(ManageStatus status) {
		this.status = status;
	}

	public List<DetIncidencia> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<DetIncidencia> permisos) {
		this.permisos = permisos;
	}

	public DetIncidencia getPermiso() {
		return permiso;
	}

	public void setPermiso(DetIncidencia permiso) {
		this.permiso = permiso;
	}

	public List<Date> getDiasSolicitados() {
		return diasSolicitados;
	}

	public void setDiasSolicitados(List<Date> diasSolicitados) {
		this.diasSolicitados = diasSolicitados;
	}

	public String getMensajeConfirmacion() {
		return mensajeConfirmacion;
	}

	public void setMensajeConfirmacion(String mensajeConfirmacion) {
		this.mensajeConfirmacion = mensajeConfirmacion;
	}

	public List<Date> getDiasDeAsueto() {
		return diasDeAsueto;
	}

	public void setDiasDeAsueto(List<Date> diasDeAsueto) {
		this.diasDeAsueto = diasDeAsueto;
	}
	

}
